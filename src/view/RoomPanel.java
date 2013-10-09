package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import models.MultiCastGroup;

import controller.ClientController;

@SuppressWarnings("serial")
public class RoomPanel extends JPanel {
	JLabel showGroups;
	JLabel join;
	JLabel create;
	JLabel back;	
	JPanel sidebar;
	JScrollPane scroll;
	
	ImagePanel inner;	
	BufferedImage background;
	CreateDialog createDialog;
	ClientController cc;
	
	public ArrayList<RoomView> roomList = new ArrayList<RoomView>();
	
	int x = 50, y = 75;
	
	public RoomPanel(){
		initPanel();
		initComponents();
		addListeners();
	}
	
	private void initPanel(){
		setLayout(null);
	}
	
	private void initComponents(){
        try{
        	background = javax.imageio.ImageIO.read(new java.io.File ("images/paper-bg.png"));
        }catch(Exception e){}
		
		inner = new ImagePanel(background);
		inner.setLayout(null);
		inner.setOpaque(false);
//		inner.setBounds(0,0,700,600);
//		add(inner);
		
		sidebar = new JPanel();
		sidebar.setLayout(null);
		sidebar.setBounds(1000,0,200,600);
		sidebar.setBackground(Color.decode("#252525"));
		sidebar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		add(sidebar);
		
		showGroups = new JLabel("Show Groups");
		showGroups.setOpaque(true);
		showGroups.setForeground(Color.WHITE);
		showGroups.setBounds(25, 20, 150, 50);
		showGroups.setFont(new Font("Segoe UI", 0, 24));
		showGroups.setHorizontalAlignment(JLabel.CENTER);
		showGroups.setBackground(Color.decode("#2773ed"));
		showGroups.setCursor(new Cursor(Cursor.HAND_CURSOR));
		showGroups.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
//		sidebar.add(showGroups);
		
		join = new JLabel("Join");
		join.setOpaque(true);
		join.setForeground(Color.WHITE);
		join.setBounds(25, 140, 150, 50);
		join.setFont(new Font("Segoe UI", 0, 24));
		join.setHorizontalAlignment(JLabel.CENTER);
		join.setBackground(Color.decode("#2773ed"));
		join.setCursor(new Cursor(Cursor.HAND_CURSOR));
		join.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		sidebar.add(join);
				
		create = new JLabel("Create");
		create.setOpaque(true);
		create.setForeground(Color.WHITE);
		create.setBounds(25, 260, 150, 50);
		create.setFont(new Font("Segoe UI", 0, 24));
		create.setHorizontalAlignment(JLabel.CENTER);
		create.setBackground(Color.decode("#2773ed"));
		create.setCursor(new Cursor(Cursor.HAND_CURSOR));
		create.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		sidebar.add(create);
				
		back = new JLabel("Back");
		back.setOpaque(true);
		back.setForeground(Color.WHITE);
		back.setBounds(25, 380, 150, 50);
		back.setFont(new Font("Segoe UI", 0, 24));
		back.setHorizontalAlignment(JLabel.CENTER);
		back.setBackground(Color.decode("#2773ed"));
		back.setCursor(new Cursor(Cursor.HAND_CURSOR));
		back.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		sidebar.add(back);			
		
		scroll = new JScrollPane(inner);
		scroll.setBorder(null);
		scroll.setBounds(0,0,700,572);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scroll, 0);
		
		createDialog = new CreateDialog(this, new ClientController(this));
		createDialog.setLocationRelativeTo(null);
	}
	
	private void addListeners(){
		create.addMouseListener(new MouseListener() {			
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
				createDialog.setVisible(true);
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
				GUI.getInstance().roomPanel.setVisible(false);
				GUI.getInstance().homePanel.setVisible(true);
			}
		});				
	}
	
	public void addRoom(String name){
		RoomView room = new RoomView(this, name);
		room.setBounds(x,y,275,100);
		inner.add(room);
		roomList.add(room);
		room.isVisible = true;
		inner.setPreferredSize(new Dimension(x+325, 600));
		inner.repaint();
		scroll.revalidate();
		
		if((inner.getComponentCount() % 3) == 0){
			x += 325;
			y = 75;
		}
		else{
			y += 150;
		}
	}
	
	public void joinRoom( ArrayList<MultiCastGroup> groupList, ClientController cc ) {
		this.cc = cc;
		for( int i = 0; i < groupList.size(); i++ ) {
			if( roomList.get( i ).isSelected ) {
				cc.joinGroup( i );
			}
		}
	}
	
	public void deselectAll(){
		RoomView room;
		for(int ctr = 0; ctr < inner.getComponentCount(); ctr++){
			room = (RoomView)inner.getComponent(ctr);
			room.deselect();
			roomList.get( ctr ).isSelected = false;
		}
//		inner.repaint();
	}
	
	public int getSelectedRoom() {
		for ( int i = 0; i < roomList.size(); i++ ) {
			if( roomList.get( i ).isSelected ) {
				return i;
			}
//			System.out.println("room: "+roomList.get( i ).title.getText()+" -- "+roomList.get( i ).isSelected);
		}
		return 0;
	}
	
	public JLabel getShowGroups() {
		return showGroups;
	}
	
	public JLabel getJoinGroup() {
		return join;
	}

}
