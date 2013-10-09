package models;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

public class Brush implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point coord;
	private Color background;
	
	public Brush( Point coord, Color background ) {
		this.coord = coord;
		this.background = background;
	}
	
	public Point getCoord() {
		return coord;
	}
	
	public Color getBG() {
		return background;
	}

}
