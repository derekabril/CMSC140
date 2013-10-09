package main;

import controller.ServerController;

public class Server {
	
	public Server() {
		new ServerController();
		System.out.println("Server is running...");
	}
	
	public static void main(String[] args) {
		new Server();
	}

}