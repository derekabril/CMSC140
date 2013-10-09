package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HomePanel extends JPanel {
	JLabel exit;
	JLabel help;
	JLabel start;
	JLabel title;
	JLabel subject;
	JLabel developer1;
	JLabel developer2;
	
	JPanel sidebar;
	Dialog dialog;
	ImagePanel inner;
	BufferedImage background;

	public HomePanel(){
		initPanel();
		initComponents();
		addListeners();
	}
	
	private void initPanel(){
		setLayout(null);
	}
	
	private void initComponents(){
        try{
        	background = javax.imageio.ImageIO.read(new java.io.File ("images/paper-bg.png"));
        }catch(Exception e){
//        	e.printStackTrace();
        }
        		
		inner = new ImagePanel(background);
		inner.setLayout(null);
		inner.setOpaque(false);
		inner.setBounds(0,0,1000,600);
		add(inner);
		
		title = new JLabel(new ImageIcon("images/title.png"));
		title.setBounds(250,150,title.getIcon().getIconWidth(),title.getIcon().getIconHeight());
		inner.add(title);

		subject = new JLabel(new ImageIcon("images/subject.png"));
		subject.setBounds(50,480,subject.getIcon().getIconWidth(),subject.getIcon().getIconHeight());
		inner.add(subject);

		developer1 = new JLabel(new ImageIcon("images/abril.png"));
		developer1.setBounds(450,480,developer1.getIcon().getIconWidth(),developer1.getIcon().getIconHeight());
		inner.add(developer1);

		developer2 = new JLabel(new ImageIcon("images/saborrido.png"));
		developer2.setBounds(820,480,developer2.getIcon().getIconWidth(),developer2.getIcon().getIconHeight());
		inner.add(developer2);
		
		sidebar = new JPanel();
		sidebar.setLayout(null);
		sidebar.setBounds(1000,0,200,600);
		sidebar.setBackground(Color.decode("#252525"));
		sidebar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		add(sidebar);
		
		start = new JLabel("Start");
		start.setOpaque(true);
		start.setForeground(Color.WHITE);
		start.setBounds(25, 140, 150, 50);
		start.setFont(new Font("Segoe UI", 0, 24));
		start.setHorizontalAlignment(JLabel.CENTER);
		start.setBackground(Color.decode("#2773ed"));
		start.setCursor(new Cursor(Cursor.HAND_CURSOR));
		start.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		sidebar.add(start);
				
		help = new JLabel("Help");
		help.setOpaque(true);
		help.setForeground(Color.WHITE);
		help.setBounds(25, 260, 150, 50);
		help.setFont(new Font("Segoe UI", 0, 24));
		help.setHorizontalAlignment(JLabel.CENTER);
		help.setBackground(Color.decode("#2773ed"));
		help.setCursor(new Cursor(Cursor.HAND_CURSOR));
		help.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		sidebar.add(help);
				
		exit = new JLabel("Exit");
		exit.setOpaque(true);
		exit.setForeground(Color.WHITE);
		exit.setBounds(25, 380, 150, 50);
		exit.setFont(new Font("Segoe UI", 0, 24));
		exit.setHorizontalAlignment(JLabel.CENTER);
		exit.setBackground(Color.decode("#2773ed"));
		exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		exit.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		sidebar.add(exit);		
		
		dialog = new Dialog();
		dialog.setLocationRelativeTo(null);
	}
	
	private void addListeners(){
		start.addMouseListener(new MouseListener() {			
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
				GUI.getInstance().homePanel.setVisible(false);
				GUI.getInstance().roomPanel.setVisible(true);
			}
		});
		help.addMouseListener(new MouseListener() {			
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
				
			}
		});
		exit.addMouseListener(new MouseListener() {			
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
				dialog.showDialog(0);
				dialog.setVisible(true);
			}
		});		
	}
}
