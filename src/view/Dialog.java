package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Dialog extends JDialog {
	JLabel title;
	JLabel desc;
	JLabel ok;
	JLabel cancel;
	
	public Dialog(){
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
		title = new JLabel();
		title.setFont(new Font("Segoe UI", 0, 24));
		title.setHorizontalAlignment(JLabel.LEFT);
		title.setForeground(Color.WHITE);
		title.setBounds(300,35,594,30);
		add(title);
		
		desc = new JLabel();
		desc.setFont(new Font("Segoe UI", 0, 16));
		desc.setHorizontalAlignment(JLabel.LEFT);
		desc.setForeground(Color.WHITE);
		desc.setBounds(300,75,594,25);
		add(desc);
		
		ok = new JLabel();
		ok.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		ok.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ok.setBackground(Color.decode("#2773ed"));
		ok.setHorizontalAlignment(JLabel.CENTER);
		ok.setFont(new Font("Segoe UI", 0, 16));
		ok.setForeground(Color.WHITE);
		ok.setBounds(674,125,100,35);
		ok.setOpaque(true);
		add(ok);
		
		cancel = new JLabel("Cancel");
		cancel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cancel.setBackground(Color.decode("#2773ed"));
		cancel.setHorizontalAlignment(JLabel.CENTER);
		cancel.setFont(new Font("Segoe UI", 0, 16));
		cancel.setForeground(Color.WHITE);
		cancel.setBounds(794,125,100,35);
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
				if(ok.getText().equals("Exit"))
					System.exit(0);
				else{
					dispose();
					GUI.getInstance().gamePanel.setVisible(false);
					GUI.getInstance().homePanel.setVisible(true);
				}
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
				dispose();
			}
		});
	}
	
	public void showDialog(int type){
		if(type == 0){
			title.setText("Are you sure you want to exit?");
			desc.setText("It's such a fun game. You'll be missing out on all the fun. Please don't say goodbye :(");
			ok.setText("Exit");
		}
		else{
			title.setText("Do you really want to quit current game?");
			desc.setText("But it's more fun to play with friends. We will all miss you :(");
			ok.setText("Quit");
		}
	}

}
