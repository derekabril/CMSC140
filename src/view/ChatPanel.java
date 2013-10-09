package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import models.MultiCastGroup;
import models.MultiCastSockets;

import controller.Categorizer;
import controller.ClientController;

public class ChatPanel extends JPanel 
{
		//components
		public JLabel wordLabel, categoryLabel;
		private JTextArea chatArea;
		private JTextField msgBox;
		private JButton send;
		private JScrollPane scPane;
		
		private Thread accept;
		public MultiCastSockets socket;
		private MulticastSocket s;
		private static InetAddress host;
		private static int PORT;
		private ArrayList<String> memberList;
		private ArrayList<Member> members = new ArrayList<Member>();
		private String current_word = "!@47#$24%^39&*23()";
		
		private Categorizer word;
		
		public ChatPanel() 
		{
			setLayout( null );
			initComponents();
			addListeners();
			repaint();
		}
	
		private void initSocket() {
			
			try {
				s = new MulticastSocket( PORT );
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	
		private void initComponents() {
			word = new Categorizer();
			
			categoryLabel = new JLabel("CATEGORY");
			categoryLabel.setFont(new Font("Segoe UI", 0, 20));
			categoryLabel.setHorizontalAlignment(JLabel.CENTER);
			categoryLabel.setForeground(Color.WHITE);
			categoryLabel.setBounds(0,25,300,32);
			add( categoryLabel );
			
			wordLabel = new JLabel("WORD");
			wordLabel.setFont(new Font("Segoe UI", 0, 24));
			wordLabel.setHorizontalAlignment(JLabel.CENTER);
			wordLabel.setForeground(Color.WHITE);
			wordLabel.setBounds(0,50,300,32);
			add( wordLabel );
			
			chatArea = new JTextArea("");		
			chatArea.setEditable(false);
	//		chatArea.setLineWrap(true);
			
			scPane = new JScrollPane( chatArea );
			scPane.setBounds(20, 101, 250, 345);
			scPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
			scPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
			add( scPane );
			
			msgBox = new JTextField("Enter chat here.");
			msgBox.setBounds(20, 451, 200, 40);
			add( msgBox );
			
			send = new JButton("SEND");
			send.setBounds(220, 451, 50, 40);
			send.setFont(new Font("Segoe UI", 0, 7));
			add( send );
		}
		
	
		public void setSocket( MultiCastSockets s ) {
			socket = s;
			PORT = socket.getGroupInfo().getMessagePort();
			initSocket();
			initHost();
			acceptThread();
		}
		
		private void initHost() {
			try {
				host = InetAddress.getByName(socket.getGroupInfo().getAddress());
				s.joinGroup(host);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	
		private void addListeners() {
			send.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					sendMessage();
				}
			});
			
			msgBox.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					msgBox.setText("");
				}
			});
			
			msgBox.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent arg0) {}
				
				@Override
				public void keyReleased(KeyEvent arg0) {}
				
				@Override
				public void keyPressed(KeyEvent arg0) {
					if( arg0.getKeyCode() == KeyEvent.VK_ENTER && 
						!msgBox.getText().trim().equals("") )
					{
						sendMessage();
					}
					
				}
			});
		}
		
		private void sendMessage() {
	//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	//		Calendar cal = Calendar.getInstance();
			
			String message = "<"+ClientController.user+">: " +
					""+msgBox.getText();
			message = message.replaceAll("\n", "");
			message += "\n";
			
			if(message.isEmpty())
				return;
			
			try {
				DatagramPacket data = new DatagramPacket(message.getBytes(), message.length(), 
					host, PORT);
				s.send(data);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			chatArea.requestFocus();
			msgBox.setText("");
			msgBox.requestFocus();
			
		}
		
		private void acceptThread() {
			accept = new Thread( new Runnable() {
				public void run() {
					while(true) {
						try {
							byte[] buffer = new byte[1024];
							s.setTimeToLive(1);
							DatagramPacket data = new DatagramPacket(buffer, buffer.length);
							s.receive(data);
							
							String message = new String(data.getData(), 0, data.getLength());
							getMessage(message);
							
						} catch (IOException e) {
							e.printStackTrace();
							System.exit(-1);
						}
						
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {}
					}
				}
			});
			accept.start();
		}
		
		private void getMessage( String message ) 
		{
			if ( message.charAt(0) == ':' ) {
				String cat = "";
				for( int i = 1; i < message.length(); i++ )
					cat += message.charAt(i);
				categoryLabel.setText( cat );
			}
			else if( message.charAt(0) != '<' ) {
				current_word = message;
				if( GamePanel.player.equals(ClientController.user) ) {
					wordLabel.setFont(new Font("Segoe UI", 0, 24));
					wordLabel.setText(""+current_word);
				}
				else {
					String numOfLetters = "";
					char current_word_char[] = current_word.toCharArray();
					for( int ctr = 0; ctr < current_word.length(); ctr++ ) {
						if( current_word_char[ctr] != ' ' )
							numOfLetters += "_ ";
						else numOfLetters += " ";
					}
	//				numOfLetters = "<html>"+numOfLetters+"</html>";
					wordLabel.setFont(new Font("Segoe UI", 0, 15));
					wordLabel.setText(numOfLetters);
				}
			}
			else {
				checkAnswer( message );
			}
		}
		
		private void checkAnswer( String answer ) 
		{
			answer.trim();
			current_word.trim();
			if( answer.contains( current_word ) || 
				answer.toLowerCase().contains( current_word.toLowerCase() ) ||
				answer.toUpperCase().contains( current_word.toUpperCase() ) || 
				answer.toLowerCase().contains( current_word.toUpperCase() ) ||
				answer.toUpperCase().contains( current_word.toLowerCase() ) )
			{
//				chatArea.setText( chatArea.getText()+"Somebody got the correct answer!"+System.getProperty("line.separator") );
//				chatArea.setCaretPosition(chatArea.getDocument().getLength());
				for( int i = 0; i < memberList.size(); i++ ) {
					System.out.println("member: "+memberList.get(i));
					if( answer.contains( memberList.get(i) ) ) {
						chatArea.setText( chatArea.getText()+memberList.get(i)+" got the correct answer!"+System.getProperty("line.separator") );
						chatArea.setCaretPosition( chatArea.getDocument().getLength() );
						if( memberList.get(i).equals( ClientController.user ) ) {
							System.out.println("KOREK");
							members.get( i ).setScore( members.get(i).getScore() + 5 );
							send.setEnabled( false );
							msgBox.setEnabled( false );
							GamePanel.timeLeft = 1;
						}
						else {
							members.get( i ).setScore( members.get(i).getScore() + 5 );
							GamePanel.timeLeft = 1;
						}
					}
				}
			}
			
			else {
				chatArea.setText( chatArea.getText()+" "+answer+System.getProperty("line.separator") );
				chatArea.setCaretPosition( chatArea.getDocument().getLength() );
			}
		}
		
		public void setMemberList( ArrayList<String> members ) {
			memberList = members;
		}
		
		public void initMemberInfo() {
			for( int i = 0; i < memberList.size(); i++ ) {
				Member member = new Member( memberList.get( i ), 0 );
				members.add( member );
			}
			
		}

		public MulticastSocket getChatSocket() {
			return s;
		}
		
		public DatagramPacket setWord( String word ) {
			DatagramPacket data = new DatagramPacket(word.getBytes(), word.length(), 
					host, PORT);
			return data;
		}
		
		public ArrayList<Member> getMemberList() {
			return members;
		}
		
		public Categorizer getWord() {
			return word;
		}
		
		public JLabel getCategoryLabel() {
			return categoryLabel;
		}
		
		public JButton getSendBtn() {
			return send;
		}
		
		public JTextField getMsgBox() {
			return msgBox;
		}

}
