package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Categorizer;
import controller.ClientController;

import model.ColorScheme;
import models.Brush;
import models.MultiCastGroup;
import models.MultiCastSockets;

@SuppressWarnings("serial")
public class GamePanel extends JPanel 
{
		//components
		private JButton start;
		private JLabel back,
					   turn,
				   	   score,
				   	   time;
		
		//panels
		private JPanel statusPanel;
		private JPanel colorPanel;
		private JPanel memberPanel;
		private NextPlayerDialog nextPlayerDialog;
		
		//classes
		private Dialog dialog;
		private BoardPanel boardPanel;
		private ChatPanel chatPanel;
		private Brush brush;
		private MulticastSocket sendSocket;
		
		//arraylists for synchronizing
		private ArrayList<MultiCastGroup> groupList;
		private ArrayList<Brush> brushProperties = new ArrayList<Brush>();
		
		//process variables
		public static int timeLeft                = 60;
		private int groupIndex, 
					maxPlayers              = 5, 
					playerTurn              = 0, 
					ctr                     = 0,
					roundCtr                = 1,
					MAX_ROUNDS              = 5;
		
		private Boolean signal              = false;
		private boolean isPressed           = false,
						flag                = false,
						IS_DRAWING_TURN     = false;
		public static boolean GAME_STARTED  = false;
		
		public static String player         = "";	
		private Thread timer;
		
		public GamePanel()
		{
			initPanel();
			initComponents();
			startTimer();
			addListeners();
		}
		
		private void initPanel()
		{
			setLayout(null);
		}
		
		private void initComponents()
		{		
			nextPlayerDialog = new NextPlayerDialog( this );
			nextPlayerDialog.setVisible( false );
			
			statusPanel = new JPanel();
			statusPanel.setLayout(null);
			statusPanel.setBounds(0,0,900,80);
			statusPanel.setBackground(Color.decode("#252525"));
			statusPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			add(statusPanel);
			
			colorPanel = new JPanel();
			colorPanel.setLayout(null);
			colorPanel.setBounds(20,100,110,372);
			colorPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
			add(colorPanel);
			
			boardPanel = new BoardPanel();
			boardPanel.setBounds(150,100,725,372);
			boardPanel.setFocusable(true);
			add(boardPanel);
			
			chatPanel = new ChatPanel();
			chatPanel.setBounds(900,0,295,600);
			chatPanel.setFocusable(true);
			chatPanel.setBackground(Color.decode("#252525"));
			chatPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			add(chatPanel);
			
			memberPanel = new JPanel();
			memberPanel.setLayout(null);
			memberPanel.setBounds(0,492,900,80);
			memberPanel.setBackground(Color.decode("#252525"));
			memberPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			add(memberPanel);	
			
			back = new JLabel("Back to Main Menu");
			back.setIcon(new ImageIcon("images/back.png"));
			back.setCursor(new Cursor(Cursor.HAND_CURSOR));
			back.setVerticalTextPosition(JLabel.CENTER);
			back.setFont(new Font("Segoe UI", 0, 18));
			back.setForeground(Color.WHITE);
			back.setBounds(10,25,200,32);
			statusPanel.add(back);
			
			start = new JButton( "START" );
			start.setBounds( 230, 25, 70, 32 );
			statusPanel.add( start );
			
			time = new JLabel( ""+timeLeft );
			time.setFont(new Font("Segoe UI", 0, 24));
			time.setHorizontalAlignment(JLabel.CENTER);
			time.setForeground(Color.WHITE);
			time.setBounds( 230, 25, 70, 32 );
			time.setVisible(false);
			statusPanel.add( time );
	
			turn = new JLabel("");
			turn.setFont(new Font("Segoe UI", 0, 24));
			turn.setHorizontalAlignment(JLabel.CENTER);
			turn.setForeground(Color.WHITE);
			turn.setBounds(320,23,300,32);
			statusPanel.add(turn);
			
			score = new JLabel("0 pts");
			score.setFont(new Font("Segoe UI", 0, 24));
			score.setHorizontalAlignment(JLabel.RIGHT);
			score.setForeground(Color.WHITE);
			score.setBounds(580,23,300,32);
			statusPanel.add(score);
			
			for(int ctr = 0, row = 0, y = 10; row < 13 && ctr < 18; row++, y+=40){
				for(int col = 0, x = 15; col < 2; col++, x+=50, ctr++){
					final JPanel color = new JPanel();
					color.setBackground(ColorScheme.getInstance().getColorPalette(ctr));
					color.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
					color.setCursor(new Cursor(Cursor.HAND_CURSOR));
					color.setBounds(x,y,30,30);
					colorPanel.add(color);
					color.addMouseListener(new MouseListener() {					
						@Override
						public void mouseReleased(MouseEvent arg0) {}					
						@Override
						public void mousePressed(MouseEvent arg0) {}					
						@Override
						public void mouseExited(MouseEvent arg0) {}					
						@Override
						public void mouseEntered(MouseEvent arg0) {}					
						@Override
						public void mouseClicked(MouseEvent arg0) {
							boardPanel.setCurColor(color.getBackground());
						}
					});
				}
			}
			
			dialog = new Dialog();
			dialog.setLocationRelativeTo(null);
		}
		
		private void addListeners()
		{
			start.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					signal = true;
					try {
						sendSignal( signal );
					} catch (IOException e) {}
				}
			});
			
			back.addMouseListener(new MouseListener() {			
				@Override
				public void mouseReleased(MouseEvent arg0) {}			
				@Override
				public void mousePressed(MouseEvent arg0) {}			
				@Override
				public void mouseExited(MouseEvent arg0) {}			
				@Override
				public void mouseEntered(MouseEvent arg0) {}			
				@Override
				public void mouseClicked(MouseEvent arg0) {
					dialog.showDialog(1);
					dialog.setVisible(true);
				}
			});
			
			boardPanel.addMouseListener( new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					if( GAME_STARTED ){
						isPressed = false;
					}
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					if( GAME_STARTED ){
						isPressed = true;
					}
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {}
			});
			
			boardPanel.addMouseMotionListener( new MouseMotionListener() {
				
				@Override
				public void mouseMoved(MouseEvent e) {}
				
				@Override
				public void mouseDragged(MouseEvent e) {
					
					IS_DRAWING_TURN = GamePanel.player.equals(ClientController.user);
					if( isPressed ) {
						try {
							if( GAME_STARTED && IS_DRAWING_TURN ){
								brush = new Brush( e.getPoint(), boardPanel.getCurColor() );
								sendCoordinates( brush );
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
				}
	
			});
		}
		
		public JLabel getBackBtn() {
			return back;
		}
		
		private void sendSignal ( Boolean signal ) throws IOException 
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			
			synchronized (this) {
				out.writeBoolean( signal );
				out.close();
			}
			
			byte[] bytes = baos.toByteArray();
			try {
				DatagramPacket data = new DatagramPacket(bytes, bytes.length,
						InetAddress.getByName(groupList.get(groupIndex).getAddress()), groupList.get(groupIndex).getPort());
				sendSocket.send(data);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		private void startTimer() 
		{
			timer = new Thread( new Runnable() {
				
				@Override
				public void run() {
					while( timeLeft > 0 && roundCtr <= MAX_ROUNDS ) 
					{
						try {
							timeLeft--;
							if( timeLeft!=0 ) {
								time.setText(""+timeLeft);
								Thread.sleep(1000);
							}
							else {
								time.setFont(new Font("Segoe UI", 0, 14));
								time.setForeground(Color.RED);
								time.setText("TIME'S UP!");
								setPlayerTurn();
								
								if( roundCtr <= MAX_ROUNDS ) {
//									boardPanel.paintComponents(getGraphics());
									showCurrentScores();
									showNextPlayerDialog();
									Thread.sleep(3000);
									setNextWord();
									boardPanel.reset();
									nextPlayerDialog.setVisible(false);
									time.setFont(new Font("Segoe UI", 0, 24));
									time.setForeground(Color.WHITE);
								}
								
								else {
									nextPlayerDialog.getPlayerLabel().setVisible( false );
									nextPlayerDialog.getRoundNumberLabel().setVisible( false );
									nextPlayerDialog.getGameOverLabel().setVisible( true );
									nextPlayerDialog.getOkayLabel().setVisible( true );
									nextPlayerDialog.getShowScoresLabel().setVisible( true );
									nextPlayerDialog.setVisible(true);
								}
							}
//							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
			});
		}
		
		private void showNextPlayerDialog() {
			System.out.println("playerTurn: "+playerTurn);
			System.out.println("size: "+(groupList.get(groupIndex).getMembers().size()-1));
			if( playerTurn < groupList.get(groupIndex).getMembers().size() || playerTurn == 0 ) {
				nextPlayerDialog.setPlayerName(groupList.get(groupIndex).getMembers().get(playerTurn));
				nextPlayerDialog.setRoundNumber( roundCtr );
				nextPlayerDialog.setVisible(true);
			}
		}
		
		private void showCurrentScores() {
			System.out.println("size: "+chatPanel.getMemberList().size());
			for( int i = 0; i < chatPanel.getMemberList().size(); i++ ) {
				System.out.println("member: "+chatPanel.getMemberList().get(i).getMemberName()+
						" --- "+chatPanel.getMemberList().get(i).getScore());
			}
		}
		
		private void setNextWord() {
	//		String category = Categorizer.getCategory();
			if( playerTurn < groupList.get(groupIndex).getMembers().size() || playerTurn == 0 ) {
				String category = Categorizer.getCategory();
				File file = new File("database/" + category + ".txt");
				category = ":"+category;
				try {
					chatPanel.getChatSocket().send(chatPanel.setWord(category));
					chatPanel.getChatSocket().send(chatPanel.setWord(Categorizer.getWord(file, Categorizer.getWordCount(file))));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		private void setPlayerTurn() 
		{
			if( playerTurn < groupList.get(groupIndex).getMembers().size()-1 ) {		
				playerTurn++;
				turn.setText(groupList.get(groupIndex).getMembers().get(playerTurn)+"'s turn");
				player = groupList.get(groupIndex).getMembers().get(playerTurn);
				timeLeft = 60;
			}
			
			else {
				roundCtr++;
				playerTurn = 0;
				turn.setText(groupList.get(groupIndex).getMembers().get(playerTurn)+"'s turn");
				player = groupList.get(groupIndex).getMembers().get(playerTurn);
				timeLeft = 60;
			}
			
			if( GamePanel.player.equals(ClientController.user) ) {
				chatPanel.getSendBtn().setEnabled(false);
				chatPanel.getMsgBox().setEnabled(false);
			} else {
				chatPanel.getSendBtn().setEnabled(true);
				chatPanel.getMsgBox().setEnabled(true);
			}
		}
		
		
		public void callReceive( MulticastSocket s ) 
		{
			new ReceiveCoords( s );
		}
		
		private class ReceiveCoords implements Runnable 
		{
			private MulticastSocket socket;
			
			public ReceiveCoords(MulticastSocket s) {
				socket = s;
				
				Thread thread = new Thread(this);
				thread.start();
			}
			
			@Override
			public void run() {
				while(true) {
					try {
						byte[] buffer = new byte[1024];
						socket.setTimeToLive(1);
						DatagramPacket data = new DatagramPacket(buffer, buffer.length);
						
						if( !GAME_STARTED ) {
							socket.receive( data );
							getSignal( data.getData() );
						}
						else {
							socket.receive(data);
							getPoints(data.getData());
						}
						
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
			
			private void getSignal ( byte[] data ) 
			{
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				ObjectInputStream in;
				Boolean signal = false;
				
				try {
					in = new ObjectInputStream(bais);
					signal = in.readBoolean();
					in.close();
					
					if( signal == true ) {
						addMembers();
						chatPanel.initMemberInfo();
						start.setVisible( false );
						time.setVisible( true );
						timer.start();
						String category = Categorizer.getCategory();
						if( GamePanel.player.equals(ClientController.user) ) {
							File file = new File("database/" + category + ".txt");
							category = ":"+category;
							chatPanel.getChatSocket().send(chatPanel.setWord(category));
							chatPanel.getChatSocket().send(chatPanel.setWord(Categorizer.getWord(file, Categorizer.getWordCount(file))));
							chatPanel.getSendBtn().setEnabled(false);
							chatPanel.getMsgBox().setEnabled(false);
						}
						else {
							chatPanel.getSendBtn().setEnabled(true);
							chatPanel.getMsgBox().setEnabled(true);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
				groupList.get(groupIndex).setGameState( true );
				System.out.println("group name: "+groupList.get(groupIndex).getGroupName());
				System.out.println("GAME_STATE: "+groupList.get(groupIndex).getGameState());
				GAME_STARTED = true;
			}
	
			private void getPoints(byte[] data) 
			{
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				ObjectInputStream in;
				ArrayList<Brush> savedCoords = null;
				try {
					in = new ObjectInputStream(bais);
					savedCoords = (ArrayList) in.readObject();
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
				for( int i = 0; i < savedCoords.size(); i++ ){
	//			    boardPanel.startStroke(savedCoords.get(i));
				    boardPanel.x = savedCoords.get(i).getCoord().x;
				    boardPanel.y = savedCoords.get(i).getCoord().y;
					drawPoints(savedCoords.get(i).getCoord(), savedCoords.get(i).getBG());
	//				boardPanel.endStroke(savedCoords.get(i));
				}
			}
	
			private void drawPoints(Point point, Color bg) 
			{
				boardPanel.addPointToStroke(point);
		    	Graphics2D g2 = (Graphics2D)boardPanel.getGraphics();
		    	g2.setStroke(new BasicStroke(5));
		    	g2.setColor(bg);
		    	g2.drawLine(boardPanel.x, boardPanel.y, point.x, point.y);
		    	boardPanel.x = point.x;
		    	boardPanel.y = point.y;
			}
		}
		
		private void sendCoordinates( Brush brush ) throws IOException 
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			
			// buffer = array of points, initially null
			// add recent to buffer
			if( !flag ) {
				brushProperties.add( brush );
				ctr++;
			} else {
				brushProperties.remove(0);
				brushProperties.add( brush );
			}
			
			if( ctr == 4 )
			{
				synchronized (this) {
					out.writeObject( brushProperties ); // buffer
					out.close();
				}
				
				byte[] bytes = baos.toByteArray();
				try {
					DatagramPacket data = new DatagramPacket(bytes, bytes.length,
							InetAddress.getByName(groupList.get(groupIndex).getAddress()), groupList.get(groupIndex).getPort());
					sendSocket.send(data);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
				flag = true;
			}
			
		}
	
		public void setGroupList(ArrayList<MultiCastGroup> groupList) 
		{
//			int ctr = 0;
			chatPanel.setMemberList(groupList.get(groupIndex).getMembers());
			this.groupList = groupList;
		}
		
		public void addMembers() 
		{
			memberPanel.removeAll();
			try{
				turn.setText( groupList.get(groupIndex).getMembers().get(playerTurn)+"'s turn" );
				player = groupList.get(groupIndex).getMembers().get(playerTurn);
			}catch( IndexOutOfBoundsException ex ) {}
			
			for(int ctr = 0, x = 100, y = 10, memberCtr = 0; ctr <= groupList.get(groupIndex).getMembers().size()-1; ctr++, x+=200){
				System.out.println("member#"+ctr+": "+groupList.get(groupIndex).getMembers().get(ctr));
				x = ( memberCtr == 0 && !groupList.get(groupIndex).getMembers().get(ctr).equals( ClientController.user ) ) ? 100 : x;
				if( !groupList.get(groupIndex).getMembers().get(ctr).equals( ClientController.user ) ){
					MemberView memberView = new MemberView( groupList.get(groupIndex).getMembers().get(ctr), 0 );
					memberView.setBounds(x,y,100,60);
					memberPanel.add(memberView);
					memberCtr++;
				}
			}
			
			IS_DRAWING_TURN = GamePanel.player.equals(ClientController.user);
			if( IS_DRAWING_TURN ){
				start.setVisible( true );
			}
			else{
				start.setVisible( false );
				time.setText("TIME");
				time.setVisible( true );
			}
			repaint();
	//		revalidate();
		}
		
		public void setRoomIndex( int index ) 
		{
			groupIndex = index;
		}
		
		public int getRoomIndex() {
			return groupIndex;
		}
	
		public void setSocket( MultiCastSockets s ) 
		{
			sendSocket = s.getSocket();
			chatPanel.setSocket(s);
		}
		
		public ChatPanel getChatPanel() {
			return chatPanel;
		}

}
