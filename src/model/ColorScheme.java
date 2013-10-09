package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class ColorScheme {
	public static ColorScheme instance;
	private Random rand = new Random();
	private ArrayList<Integer> palette = new ArrayList<Integer>();
	private ArrayList<Integer> generated = new ArrayList<Integer>();
	private String[] colors= {
			"#F3B200", "#77B900", "#2572EB", "#AD103C", "#632F00",
			"#B01E00", "#C1004F", "#7200AC", "#4617B4", "#006AC1",
			"#008287", "#199900", "#00C13F", "#FF981D", "#FF2E12",
			"#FF1D77", "#AA40FF", "#1FAEFF", "#56C5FF", "#00D8CC",
			"#91D100", "#E1B700", "#FF76BC", "#00A3A3", "#FE7C22"
			};
	private String[] colorPalette= {
			"#252525", "#2572EB", "#AD103C", "#632F00", "#B01E00", "#4617B4", 
			"#008287", "#199900", "#00C13F", "#FF2E12", "#FF1D77", "#AA40FF", 
			"#56C5FF", "#91D100", "#FF76BC", "#00A3A3", "#FE7C22", "#F3B200"
			};
	
	public static ColorScheme getInstance(){
		if(instance == null)
			instance = new ColorScheme();
		return instance;
	}
	
	public ColorScheme(){
		
	}
	
	public Color randomizeColor() {
		return Color.decode(colors[rand.nextInt(25)]);
	}
	
	public Color getUniqueColor() {
		int index;		
		while(true){
			index = rand.nextInt(25);
			if(!generated.contains(index)){
				generated.add(index);
				break;
			}
		}
		return Color.decode(colors[index]);
	}
	
	public Color getColorPalette(int index) {
		return Color.decode(colorPalette[index]);
	}

	public void reset(){
		palette.clear();
		generated.clear();
	}

}
