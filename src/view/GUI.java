package view;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	public static GUI instance;
	
	public HomePanel homePanel;
	public RoomPanel roomPanel;
	public GamePanel gamePanel;

	public static GUI getInstance(){
		if(instance == null)
			instance = new GUI();
		return instance;
	}
	
	public GUI(){
		initFrame();
		initComponents();
	}
	
	private void initFrame(){
		setSize(1200,600);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Doodle My Thing!");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private void initComponents(){   
		
		homePanel = new HomePanel();
		homePanel.setBounds(0,0,1200,600);
		add(homePanel);
		
		roomPanel = new RoomPanel();
		roomPanel.setVisible(false);
		roomPanel.setBounds(0,0,1200,600);
		add(roomPanel);
		
		gamePanel = new GamePanel();
		gamePanel.setVisible(false);
		gamePanel.setBounds(0,0,1200,600);
		add(gamePanel);
	}
	
	public GamePanel getGamePanel() {
		return  gamePanel;
	}
}
