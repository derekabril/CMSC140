package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class NextPlayerDialog extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel player, 
				   roundNumber, 
				   okay, 
				   showScores, 
				   gameOver,
				   scoreLabel;
	private String playerName = "";
	private GamePanel gp;
	
	private Thread gameOverThread;
	private int y = 200;
	
	public NextPlayerDialog( GamePanel gp ) {
		this.gp = gp;
		setProperties();
		initComponents();
		addListeners();
	}	

	private void setProperties() {
		setLayout( null );
		setSize( 1194, 200 );
		setResizable( false );
		setLocationRelativeTo( null );
		getContentPane().setBackground(Color.decode("#252525"));
		setUndecorated( true );
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	private void initComponents() {
		player = new JLabel(playerName+"'s turn.");
		player.setFont(new Font("Segoe UI", 0, 34));
		player.setHorizontalAlignment(JLabel.CENTER);
		player.setForeground(Color.WHITE);
		player.setBounds( 0, 80, 1194, 50 );
		add( player );
		
		gameOver = new JLabel("GAME OVER!");
		gameOver.setFont(new Font("Segoe UI", 0, 34));
		gameOver.setHorizontalAlignment(JLabel.CENTER);
		gameOver.setForeground(Color.WHITE);
		gameOver.setBounds( 0, 50, 1194, 50 );
		gameOver.setVisible( false );
		add( gameOver );
		
		scoreLabel = new JLabel("");
		scoreLabel.setFont(new Font("Segoe UI", 0, 24));
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setBounds( 0, 0, 1194, 600 );
		scoreLabel.setVisible( false );
		add( scoreLabel );
		
		roundNumber = new JLabel("Round 1");
		roundNumber.setFont(new Font("Segoe UI", 0, 28));
		roundNumber.setHorizontalAlignment(JLabel.CENTER);
		roundNumber.setForeground(Color.decode("#2773ed"));
		roundNumber.setBounds( 0, 30, 1194, 50 );
		add( roundNumber );
		
		okay = new JLabel("OK");
		okay.setOpaque(true);
		okay.setForeground(Color.WHITE);
		okay.setBounds(630, 120, 150, 50);
		okay.setFont(new Font("Segoe UI", 0, 24));
		okay.setHorizontalAlignment(JLabel.CENTER);
		okay.setBackground(Color.decode("#2773ed"));
		okay.setCursor(new Cursor(Cursor.HAND_CURSOR));
		okay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		okay.setVisible( false );
		add(okay);
		
		showScores = new JLabel("SHOW SCORES");
		showScores.setOpaque(true);
		showScores.setForeground(Color.WHITE);
		showScores.setBounds(430, 120, 150, 50);
		showScores.setFont(new Font("Segoe UI", 0, 18));
		showScores.setHorizontalAlignment(JLabel.CENTER);
		showScores.setBackground(Color.decode("#2773ed"));
		showScores.setCursor(new Cursor(Cursor.HAND_CURSOR));
		showScores.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		showScores.setVisible( false );
		add(showScores);
	}
	
	private void addListeners() {
		okay.addMouseListener( new MouseListener() {
			
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
		
		showScores.addMouseListener( new MouseListener() {
			
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
				scoreLabel.setVisible( true );
				gameOverThread();
//				setSize(600, 600);
				setLocationRelativeTo( null );
				showScores();
			}
		});
		
	}
	
	private void gameOverThread() {
		gameOverThread = new Thread( new Runnable() {
			
			@Override
			public void run() {
				while( y <= 400 ) {
					setSize( 1194, y );
					setLocationRelativeTo( null );
					y++;
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		gameOverThread.start();
	}
	
	private void showScores() {
		String scores = "<html><body>";
		for( int i = 0; i < gp.getChatPanel().getMemberList().size(); i++ ) {
			System.out.println("member: "+gp.getChatPanel().getMemberList().get(i).getMemberName()+
					" --- "+gp.getChatPanel().getMemberList().get(i).getScore());
			
			scores += "<p style = 'text-align: center;'>"+gp.getChatPanel().getMemberList().get(i).getMemberName()+
					" --- <span style = 'text-align:center;'>"+gp.getChatPanel().getMemberList().get(i).getScore()+"</span></p><br>";			
		}
		
		scores += "</body></html>";
		scoreLabel.setText( scores );
	}
	
	public void setRoundNumber( int roundCtr ) {
		roundNumber.setText("Round "+roundCtr);
	}
	
	public void setPlayerName( String playerName ) {
		player.setText(playerName+"'s turn.");
	}
	
	public JLabel getGameOverLabel() {
		return gameOver;
	}
	
	public JLabel getPlayerLabel() {
		return player;
	}
	
	public JLabel getRoundNumberLabel() {
		return roundNumber;
	}
	
	public JLabel getOkayLabel() {
		return okay;
	}
	
	public JLabel getShowScoresLabel() {
		return showScores;
	}
}
