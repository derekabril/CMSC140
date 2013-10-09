package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import controller.ClientController;

@SuppressWarnings("serial")
public class CreateDialog extends JDialog {
	String groupName = "";
	String address = "";
	int port, msgPort;
	
	JLabel title;
	JLabel ok;
	JLabel cancel;
	public JTextField nameField;
	ClientController cc;
	
	RoomPanel roomPanel;
	
	public CreateDialog(RoomPanel roomPanel, ClientController cc){
		this.roomPanel = roomPanel;
		this.cc = cc;
		initDialog();
		initComponents();
		addListeners();
	}
	
	private void initDialog(){
		setModal(true);
		setLayout(null);
		setUndecorated(true);
		setBounds(0,0,1194,200);
		getContentPane().setBackground(Color.decode("#252525"));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	private void initComponents(){
		title = new JLabel("Enter room name: ");
		title.setFont(new Font("Segoe UI", 0, 20));
		title.setHorizontalAlignment(JLabel.LEFT);
		title.setForeground(Color.WHITE);
		title.setBounds(150,35,594,30);
		add(title);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Segoe UI", 0, 14));
//		desc.setHorizontalAlignment(JLabel.LEFT);
//		desc.setForeground(Color.WHITE);
		nameField.setBounds(150,75,844,25); //297
		add(nameField);
		
		ok = new JLabel("Create");
		ok.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		ok.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ok.setBackground(Color.decode("#2773ed"));
		ok.setHorizontalAlignment(JLabel.CENTER);
		ok.setFont(new Font("Segoe UI", 0, 16));
		ok.setForeground(Color.WHITE);
		ok.setBounds(774,125,100,35);
		ok.setOpaque(true);
		add(ok);
		
		cancel = new JLabel("Cancel");
		cancel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cancel.setBackground(Color.decode("#2773ed"));
		cancel.setHorizontalAlignment(JLabel.CENTER);
		cancel.setFont(new Font("Segoe UI", 0, 16));
		cancel.setForeground(Color.WHITE);
		cancel.setBounds(894,125,100,35);
		cancel.setOpaque(true);
		add(cancel);		
	}
	
	private void addListeners(){
		ok.addMouseListener(new MouseListener() {			
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
//				cc = new ClientController(roomPanel);
				cc.createMulticastGroup(nameField.getText());
				System.out.println("size: "+cc.groupList.size());
				System.out.println(nameField.getText()+" -- "+getAddress()+" -- "+getPort());
//				roomPanel.addRoom(nameField.getText());
				nameField.setText("");
				dispose();
			}
		});
		cancel.addMouseListener(new MouseListener() {			
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
				nameField.setText("");
				dispose();
			}
		});
	}
	
	public JLabel getCreate() {
		return ok;
	}
	
	public String getGroupName(){
		return nameField.getText();
	}
	
	public String getAddress() {
		address = "224.";
		Random rand = new Random();
		address += ""+rand.nextInt(255)+".";
		address += ""+rand.nextInt(255)+".";
		address += ""+rand.nextInt(255);
		
		return address;
	}
	
	public int getPort(){
		Random rand = new Random();
		while( true ) {
			port = rand.nextInt(65535);
			if( port > 32767 ) 
				break;
		}
		
		return port;
	}
	
	public int getMessagePort() {
		Random rand = new Random();
		while( true ) {
			msgPort = rand.nextInt(65535);
			if( msgPort < 32767 ) 
				break;
		}
		
		return msgPort;
	}

}
