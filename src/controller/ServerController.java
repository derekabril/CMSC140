package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

import models.MultiCastGroup;

public class ServerController {
	private ArrayList<MultiCastGroup> multicastGroups;
	
	private MulticastSocket groupListSocket;
	private MulticastSocket receiveSocket;
	private MulticastSocket sendSocket;
	
	public ServerController() {
		createClasses();
	}
	
	private void createClasses() {
		multicastGroups = new ArrayList<MultiCastGroup>();
		
		try {
			InetAddress send = InetAddress.getByName("225.2.2.6");
			InetAddress receive = InetAddress.getByName("224.2.2.5");
			InetAddress groupList = InetAddress.getByName("224.2.2.4");
			
			sendSocket = new MulticastSocket(1500);
			receiveSocket = new MulticastSocket(1234);
			groupListSocket = new MulticastSocket(1000);

			groupListSocket.joinGroup(groupList);
			receiveSocket.joinGroup(receive);
			sendSocket.joinGroup(send);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}		
		
		new GroupListThread();
		new GroupReceiveThread(this);
		new GroupSendThread(this);
	}
	
	private class GroupListThread implements Runnable {
		
		public GroupListThread() {
			Thread thread = new Thread(this);
			thread.start();
		}
		
		@Override
		public void run() {
			while(true) {
				byte buffer[] = new byte[1024];
				DatagramPacket data = new DatagramPacket(buffer, buffer.length);
				try {
					groupListSocket.receive(data);
					getGroupList(data.getData());
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
				
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {}
			}
		}
		
		private void getGroupList(byte[] data) throws IOException {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream in = new ObjectInputStream(bais);
			try {
				multicastGroups = (ArrayList<MultiCastGroup>) in.readObject();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
		}
	}
	
	private class GroupReceiveThread implements Runnable {
		private ServerController controller;
		
		public GroupReceiveThread(ServerController c) {
			controller = c;
			Thread thread = new Thread(this);
			thread.start();
		}
		
		@Override
		public void run() {
			while(true) {
				byte buffer[] = new byte[1024];
				DatagramPacket data = new DatagramPacket(buffer, buffer.length);
				try {
					receiveSocket.receive(data);
					createNewGroup(data.getData());
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
				
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {}
			}
		}
		
		private void createNewGroup(byte[] data) throws IOException {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream in = new ObjectInputStream(bais);
			MultiCastGroup group = null;
			try {
				group = (MultiCastGroup) in.readObject();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			synchronized (controller) {
				multicastGroups.add(group);
			}
		}
	}
	
	private class GroupSendThread implements Runnable {
		private ServerController controller;
		
		public GroupSendThread(ServerController c) {
			controller = c;
			Thread thread = new Thread(this);
			thread.start();
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					sendMulticastGroups();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
			}
		}
		
		private void sendMulticastGroups() throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			
			synchronized (controller) {
				out.writeObject(multicastGroups);
				out.close();
			}
			
			byte[] bytes = baos.toByteArray();
			try {
				DatagramPacket data = new DatagramPacket(bytes, bytes.length,
						InetAddress.getByName("225.2.2.6"), 1500);
				sendSocket.send(data);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

}
