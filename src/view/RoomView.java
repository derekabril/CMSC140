package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ColorScheme;

@SuppressWarnings("serial")
public class RoomView extends JPanel{
	JLabel title;
	JLabel selected;
	
	String name;
	RoomPanel roomPanel;
	public boolean isSelected = false, isVisible = false;

	public RoomView(RoomPanel roomPanel, String name){
		this.name = name;
		this.roomPanel = roomPanel;
		initRoom();
		initComponents();
		addListeners();
	}
	
	private void initRoom(){
		setLayout(null);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBackground(ColorScheme.getInstance().randomizeColor());
	}
	
	private void initComponents(){
		selected = new JLabel(new ImageIcon("images/check2.png"));
		selected.setBounds(270-selected.getIcon().getIconWidth(),5,selected.getIcon().getIconWidth(),selected.getIcon().getIconHeight());
		selected.setVisible(false);
		add(selected);
		
		title = new JLabel(name);
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Segoe UI", 0, 24));
		title.setBounds(10,10,255,20);
		add(title);
	}
	
	private void addListeners(){
		addMouseListener(new MouseListener() {			
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
				roomPanel.deselectAll();
				isSelected = true;
//				roomPanel.showSelectedRoom();
				selected.setVisible(true);
				setBorder(BorderFactory.createLineBorder(Color.decode("#252525"), 5));		
			}
		});
	}
	
	public void deselect(){
		setBorder(null);
		selected.setVisible(false);
	}
}
