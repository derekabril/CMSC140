package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Scribble;

import controller.Drawer;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
	public Color color;
	public LinkedList<Scribble> scribbles = new LinkedList<Scribble>();
 
	public int x, y;
	public boolean mouseButtonDown = false;
	
	Scribble curStroke = null; 
	Color curColor = Color.black; 
	Graphics2D g2;
	
	public BoardPanel(){
		setLayout(null);
		setSize(725,372);
		setBackground(Color.WHITE);
		setColor(Color.decode("#252525"));
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		addMouseMotionListener(new Drawer(this));
		addMouseListener(new Drawer(this));
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	public void setCurColor(Color curColor) { 
	    this.curColor = curColor; 
	  }

	public Color getCurColor() { 
	    return curColor; 
	}

	public void startStroke(Point p) {
	    curStroke = new Scribble(curColor); 
	    curStroke.addPoint(p); 
	}

	public void addPointToStroke(Point p) {
	    if (curStroke != null) { 
	      curStroke.addPoint(p); 
	    }
	}

	public void endStroke(Point p) {
	    if (curStroke != null) { 
	      curStroke.addPoint(p); 
	      scribbles.add(curStroke); 
	      curStroke = null; 
	    }
	}
		
//	protected void paintComponent(Graphics g) {
//	    super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setStroke(new BasicStroke(5));
//        
//        for(int ctr = 0; ctr < scribbles.size(); ctr++){
//	        for(int i = 0; i < scribbles.get(ctr).points.size() - 2; i++){
//		        Color color = scribbles.get(ctr).getColor();
//		        Point p1 = scribbles.get(ctr).points.get(i);
//		    	Point p2 = scribbles.get(ctr).points.get(i + 1);
//		        g2.setColor(color);
//		        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
//	        }
//    	}
//        
//	    for (int i = 0; i < points.size() - 2; i++){
//	        Point p1 = points.get(i);
//	        Point p2 = points.get(i + 1);
//
////	        Graphics2D g2 = (Graphics2D) g;
//	        g2.setColor(color);
////	        g2.setStroke(new BasicStroke(5));
//	        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
//	    }
//	}
	
	public void paint(Graphics g){
        g2 = (Graphics2D) g;
        Dimension dim = getSize(); 
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, dim.width, dim.height);   
        g2.setColor(Color.white);
        g2.fillRect(1, 1, dim.width-2, dim.height-2);   
        g2.setColor(Color.decode("#252525"));
  
		for(int ctr = 0; ctr < scribbles.size(); ctr++){
			Scribble stroke = scribbles.get(ctr);
			if(stroke != null){
				g2.setColor(stroke.getColor());
				Point prev = null;
				LinkedList<Point> points = stroke.getPoints();
				for(int ctr2 = 0; ctr2 < points.size(); ctr2++){
					Point cur = points.get(ctr2);
					if(prev != null)
					g2.drawLine(prev.x, prev.y, cur.x, cur.y);
//					System.out.println("x: "+cur.x+" --- y: "+cur.y);
					prev = cur;
				}
			}
		}
	}
	
	public void reset(){
		scribbles.clear();
		g2.clearRect(0, 0, 725, 372);
		repaint();
	}


}
