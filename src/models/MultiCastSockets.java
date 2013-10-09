package models;

import java.io.IOException;
import java.net.MulticastSocket;

public class MultiCastSockets {
	private MulticastSocket socket;
	private MultiCastGroup group;
	
	public MultiCastSockets(MultiCastGroup g) {
		group = g;
		createSocket();
	}
	
	private void createSocket() {
		try {
			socket = new MulticastSocket(group.getPort());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public MultiCastGroup getGroupInfo() {
		return group;
	}
	
	public MulticastSocket getSocket() {
		return socket;
	}

}
