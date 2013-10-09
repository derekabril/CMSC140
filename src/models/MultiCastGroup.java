package models;

import java.io.Serializable;
import java.util.ArrayList;

public class MultiCastGroup implements Serializable {
	private static final long serialVersionUID = 8699715763893226746L;
	
	private ArrayList<String> members;
	private String name;
	private String address;
	private int port, msgPort;
	private boolean ROOM_FULL    = false;
	private boolean GAME_STARTED = false;
	
	public MultiCastGroup(String n, String a, int p, int mP) {
		name = n;
		address = a;
		port = p;
		msgPort = mP;
		members = new ArrayList<String>();
		ROOM_FULL = members.size() < 5 ? false : true;
	}
	
	public ArrayList<String> getMembers() {
		return members;
	}
	
	public String getGroupName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	public int getMessagePort() {
		return msgPort;
	}
	
	public boolean getRoomFull() {
		return ROOM_FULL;
	}
	
	public void setGameState( boolean flag) {
		this.GAME_STARTED = flag;
	}
	
	public boolean getGameState() {
		return GAME_STARTED;
	}

}
