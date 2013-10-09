package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import models.MultiCastGroup;
import models.MultiCastSockets;

import view.GUI;
import view.RoomPanel;
import view.CreateDialog;
import view.Username;

public class ClientController {
	public static String user = "";
	public ArrayList<MultiCastGroup> groupList;
	private ArrayList<MultiCastSockets> joinedList;
	
	private MulticastSocket groupListSocket;
	private MulticastSocket receiveSocket;
	private MulticastSocket sendSocket;
	
	private RoomPanel roomPanel;
	private CreateDialog createDialog;
	private Username username;
	
	public ClientController(RoomPanel cv) {
		roomPanel = cv;
		createClasses();
		createListeners();
	}
	
	private void createClasses() {
		username = new Username();
		createDialog = new CreateDialog( roomPanel, this );
		joinedList = new ArrayList<MultiCastSockets>();
		
		try {
			InetAddress send = InetAddress.getByName("224.2.2.5");
			InetAddress receive = InetAddress.getByName("225.2.2.6");
			InetAddress groupList = InetAddress.getByName("224.2.2.4");
			
			sendSocket = new MulticastSocket(1234);
			receiveSocket = new MulticastSocket(1500);
			groupListSocket = new MulticastSocket(1000);

			groupListSocket.joinGroup(groupList);
			receiveSocket.joinGroup(receive);
			sendSocket.joinGroup(send);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}	
		
		new DisplayGroups();
	}
	
	private void createListeners() 
	{		
		roomPanel.getShowGroups().addMouseListener(new MouseListener() 
		{
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
				updateRooms();
				System.out.println("SHOW GROUPS");
			}
		});
		
		roomPanel.getJoinGroup().addMouseListener(new MouseListener() {
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
				System.out.println("JOIN");
				callJoinRoom();
			}
		});
		
		username.getProceed().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setUsername();
				GUI.getInstance().setTitle("Doodle My Thing! - "+user);
			}
		});
	}
	
	public void callJoinRoom() {
		roomPanel.joinRoom( groupList, this );
	}
	
	public void createMulticastGroup(String name) {
		System.out.println("name: "+name);
		String groupName = name;
		String address = createDialog.getAddress();
		int port = createDialog.getPort();
		
		if(groupName.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please enter the group name.", 
					"Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		for(int i = 0; i < groupList.size(); i++) {
			InetAddress add = null;
			MultiCastGroup m = groupList.get(i);
			if(m.getGroupName().equals(groupName)) {
				JOptionPane.showMessageDialog(null, "Group name is already taken.", 
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if( m.getAddress().equals(address) && m.getPort() == port ) {
				JOptionPane.showMessageDialog(null, "A group with this IP Address and " +
						"port number already exists.",	"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				add = InetAddress.getByName(address.trim());
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(null, "Please enter a valid IP Address.",	
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(!add.isMulticastAddress()) {
				JOptionPane.showMessageDialog(null, "The IP Address is not a valid multicast " +
						"address.",	"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		MultiCastGroup group = new MultiCastGroup(name, createDialog.getAddress(), createDialog
			.getPort(), createDialog.getMessagePort());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(baos);
			outputStream.writeObject(group);
			outputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		byte[] bytes = baos.toByteArray();
		try {
			DatagramPacket data = new DatagramPacket(bytes, bytes.length,
					InetAddress.getByName("224.2.2.5"), 1234);
			sendSocket.send(data);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		JOptionPane.showMessageDialog(null, "Group successfully created.");
//		createGroup.clear();
	}
	
	private void updateRooms()
	{
		for(int i = 0; i < groupList.size(); i++) 
		{
			try{
				if( roomPanel.roomList.get(i).isVisible == false )
					roomPanel.addRoom(groupList.get(i).getGroupName());
			}catch( IndexOutOfBoundsException ex ) {
				roomPanel.addRoom(groupList.get(i).getGroupName());
			}
				
		}
	}
	
	
	public synchronized void joinGroup( int selectedRow ) 
	{
		int row = selectedRow;
//		System.out.println("room name: "+groupList.get(row).getGroupName());
		System.out.println("GAME_STATE: "+groupList.get(row).getGameState());
		if( groupList.get(row).getMembers().size() < 5 && !groupList.get(row).getGameState() ) {
			GUI.getInstance().gamePanel.setRoomIndex( row );
	
			joinedList.add(new MultiCastSockets(groupList.get(row)));
			System.out.println("joinedList size: "+joinedList.size());
			MultiCastSockets s = joinedList.get(joinedList.size() - 1);
			
			try {
				InetAddress address = InetAddress.getByName(s.getGroupInfo().getAddress());
				s.getSocket().joinGroup(address);
				JOptionPane.showMessageDialog(null, "Successfully joined group: "+groupList.get(row).getGroupName());
				GUI.getInstance().gamePanel.callReceive(s.getSocket());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			GUI.getInstance().gamePanel.setSocket( s );
			
			System.out.println("user: "+user);
			groupList.get(row).getMembers().add(user);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ObjectOutputStream outputStream = new ObjectOutputStream(baos);
				outputStream.writeObject(groupList);
				outputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(-1);
			}
			
			byte[] bytes = baos.toByteArray();
			try {
				DatagramPacket data = new DatagramPacket(bytes, bytes.length,
						InetAddress.getByName("224.2.2.4"), 1000);
				sendSocket.send(data);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		else if ( groupList.get(row).getGameState() ) {
			JOptionPane.showMessageDialog(null, "Sorry! The game has already started.", 
					"Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		else {
			JOptionPane.showMessageDialog(null, "Sorry! The room is full.", 
					"Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		enterRoom();
	}
	
	private void enterRoom() {
		GUI.getInstance().roomPanel.setVisible(false);
		GUI.getInstance().gamePanel.setGroupList( groupList );
		GUI.getInstance().gamePanel.addMembers();
		GUI.getInstance().gamePanel.setVisible(true);
	}
	
	public synchronized void leaveGroup( int roomIndex ) {
//		int yes = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave this " +
//				"group?", "Message", JOptionPane.YES_NO_OPTION);
//		if(yes == JOptionPane.NO_OPTION)
//			return;
//		
		int row = roomIndex;
		if(row == -1) {
			JOptionPane.showMessageDialog(null, "Please select a group.",
					"Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
//		if(roomPanel.getGroupTable().getValueAt(row, 1) != null && !roomPanel.
//				getGroupTable().getValueAt(row, 1).toString().equals("Joined")) {
//			JOptionPane.showMessageDialog(null, "You are not a member of this group.", 
//					"Message", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
		groupList.get(row).getMembers().remove(user);
//		String groupName = roomPanel.getGroupTable().getValueAt(row, 0).toString();
//		for(int i = 0; i < groupList.size(); i++) {
//			if(groupList.get(i).getGroupName().equals(groupName)) {
//				groupList.get(i).getMembers().remove(user);
//				break;
//			}
//		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(baos);
			outputStream.writeObject(groupList);
			outputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		byte[] bytes = baos.toByteArray();
		try {
			DatagramPacket data = new DatagramPacket(bytes, bytes.length,
					InetAddress.getByName("224.2.2.4"), 1000);
			sendSocket.send(data);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		MultiCastSockets s = joinedList.get(row);
		InetAddress add = null;
		try {
			add = InetAddress.getByName(s.getGroupInfo().getAddress());
			s.getSocket().leaveGroup(add);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
//		roomPanel.getGroupTable().setValueAt("", row, 1);
		joinedList.remove(row);
		
//		for(int i = 0; i < joinedList.size(); i++) {
//			if(joinedList.get(i).getGroupInfo().getGroupName().equals(groupName)) {
//				MultiCastSockets s = joinedList.get(i);
//				InetAddress add = null;
//				try {
//					add = InetAddress.getByName(s.getGroupInfo().getAddress());
//					s.getSocket().leaveGroup(add);
//				} catch (IOException e) {
//					e.printStackTrace();
//					System.exit(-1);
//				}
////				roomPanel.getGroupTable().setValueAt("", row, 1);
//				joinedList.remove(i);
//				break;
//			}
//		}
//		
//		updateSpecificGroupList();
	}
	
	public synchronized void exitClient() {
		for(int i = 0; i < joinedList.size(); i++) {
			MultiCastSockets s = joinedList.get(i);
			InetAddress add = null;
			try {
				add = InetAddress.getByName(s.getGroupInfo().getAddress());
				s.getSocket().leaveGroup(add);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			joinedList.remove(i);
		}
		
		for(int i = 0; i < groupList.size(); i++) {
			boolean isBreak = false;
			for(int j = 0; j < groupList.get(i).getMembers().size(); j++) {
				if(user.equals(groupList.get(i).getMembers().get(j))) {
					groupList.get(i).getMembers().remove(j);
					isBreak = true;
					break;
				}
			}
			if(isBreak)
				break;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(baos);
			outputStream.writeObject(groupList);
			outputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		byte[] bytes = baos.toByteArray();
		try {
			DatagramPacket data = new DatagramPacket(bytes, bytes.length,
					InetAddress.getByName("224.2.2.4"), 1000);
			sendSocket.send(data);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
//		System.exit(-1);
	}
	
	
	private void setUsername() {
		String name = username.getUsername().getText();
		System.out.println("name: "+name);
		if(name.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please enter a name.", "Message", 
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!checkIfNameExists(name)) {
			JOptionPane.showMessageDialog(null, "Username is already taken.", "Message", 
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		this.user = name;
		username.dispose();
	}
	
	private synchronized boolean checkIfNameExists(String name) {
		for(int i = 0; i < groupList.size(); i++) {
			for(int j = 0; j < groupList.get(i).getMembers().size(); j++) {
				if(name.equals(groupList.get(i).getMembers().get(j)))
					return false;
			}
		}
		
		return true;
	}
	
	public Username getUsername() {
		return username;
	}
	
	private class DisplayGroups implements Runnable {
		private int ctr = 0;
		
		public DisplayGroups() {
			Thread thread = new Thread(this);
			thread.start();
		}
		
		@Override
		public void run() {
			while(true) {
				try{
					updateRooms();
				}catch( Exception ex ) {}
				
				byte[] buffer = new byte[1024];
				DatagramPacket data = new DatagramPacket(buffer, buffer.length);
				
				try {
					receiveSocket.receive(data);
					displayGroups(data.getData());
				} catch (IOException e1) {
					e1.printStackTrace();
					System.exit(-1);
				}
				
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {}
			}
		}
		
		private synchronized void displayGroups(byte[] data) {
			ctr++;
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(bais);
				groupList = (ArrayList) in.readObject();
				try{
					if( ctr % 10 == 0 ){
						GUI.getInstance().gamePanel.setGroupList( groupList );
//						GUI.getInstance().gamePanel.addMembers();
//						System.out.println("list:" +groupList.get(groupList.size()-1).getMembers().size());
					}
				}catch(Exception ex){}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

}
