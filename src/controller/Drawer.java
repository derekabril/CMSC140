package controller;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import view.BoardPanel;
import view.GamePanel;


public class Drawer implements MouseMotionListener, MouseListener{	
	private BoardPanel boardPanel;
	private boolean flag = false, IS_DRAWING_TURN = false;

	public Drawer(BoardPanel boardPanel){
		this.boardPanel = boardPanel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	    Point p = e.getPoint(); 
	    flag = GamePanel.player.equals(ClientController.user);
	    IS_DRAWING_TURN = (boardPanel.mouseButtonDown && GamePanel.GAME_STARTED && flag);
	    
	    if ( !IS_DRAWING_TURN ) {
	    	return;	    	
	    }
	    
    	boardPanel.addPointToStroke(p);
    	Graphics2D g2 = (Graphics2D)boardPanel.getGraphics();
    	g2.setStroke(new BasicStroke(5));
    	g2.setColor(boardPanel.getCurColor());
    	g2.drawLine(boardPanel.x, boardPanel.y, p.x, p.y); 
    	boardPanel.x = p.x; 
    	boardPanel.y = p.y;
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
	    Point p = e.getPoint(); 
	    boardPanel.mouseButtonDown = true;
	    boardPanel.startStroke(p);    
	    boardPanel.x = p.x;
	    boardPanel.y = p.y;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    Point p = e.getPoint(); 
	    boardPanel.endStroke(p);       
	    boardPanel.mouseButtonDown = false;       
	}
}
