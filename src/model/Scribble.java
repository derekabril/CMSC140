package model;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

public class Scribble {
	Color color;
	public LinkedList<Point> points = new LinkedList<Point>();
	
	public Scribble(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColor(Color color){
		this.color = color;
	}

  public void addPoint(Point p) {
    if (p != null) { 
      points.add(p); 
    }
  }

  public LinkedList<Point> getPoints() { 
    return points; 
  }

}
