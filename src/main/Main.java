package main;

import javax.swing.UIManager;

import model.Server;

import view.GUI;

public class Main {

	public static void main (String args[]){
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    
	    catch(Exception ex){}
		GUI gui = GUI.getInstance();
		Server.getInstance();
		gui.setVisible(true);		
	}
}
