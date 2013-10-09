package model;

import java.util.LinkedList;

import view.RoomView;

public class Server {
	public static Server instance;
	LinkedList<RoomView> roomList = new LinkedList<RoomView>();
	
	public static Server getInstance(){
		if(instance == null)
			instance = new Server();
		return instance;
	}

	public Server(){
		
	}
	
	public LinkedList<RoomView> getRoomList(){
		return roomList;
	}
	
	public void addRoom(){
//		RoomView room = new RoomView();
//		roomList.add(room);
	}
	
	public void deleteRoom(RoomView room){
		roomList.remove(room);
	}
	
	public int getRoomCount(){
		return roomList.size();
	}
}
