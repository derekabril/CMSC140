package main;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import model.Server;

import controller.ClientController;

//import view.ClientView;
import view.GUI;
import view.RoomPanel;

public class Client {
	private RoomPanel clientView;
	private ClientController clientController;
	private GUI gui;
	
	public Client() {
		setLookAndFeel();
		createClasses();
		createInterface();
		clientController.getUsername().setVisible(true);
	}
	
	private void createClasses() {
//		frame = new JFrame();
		gui = GUI.getInstance();
		Server.getInstance();
		gui.setVisible(true);
		
		clientView = new RoomPanel();
		clientController = new ClientController(clientView);
	}
	
	private void createInterface() {
		setIcon();
		decorateWindow();
	}
	
	private void setIcon() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("images/icon.png");
		gui.setIconImage(image);
	}
	
	private void decorateWindow() {
		gui.getGamePanel().getBackBtn().addMouseListener( new MouseListener() {
			
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
				System.out.println("BACK");
//				clientController.leaveGroup( gui.getGamePanel().getRoomIndex() );
				clientController.exitClient();
				
			}
		});
		
		gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		gui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int value = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to quit?", "Message",
						JOptionPane.YES_NO_OPTION);
				if (value == JOptionPane.YES_OPTION) {
					clientController.exitClient();
					System.exit(-1);
				}
			}
		});

		gui.setVisible(true);
		gui.add(clientView);
	}
	
	private void setLookAndFeel() {
		
	}
	
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    
	    catch(Exception ex){}
		new Client();
	}

}
