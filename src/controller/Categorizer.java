package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class Categorizer {
	public static String category = "";

//	public static void main (String args[]){
//		for(int ctr = 0; ctr < 50; ctr++){
//			File file = new File("database/" + getCategory() + ".txt");
//			System.out.println(", " + getWord(file, getWordCount(file)));
//		}
//	}
	
	public static String getCategory(){
		String category = "";
		Random rand = new Random();
		int n = rand.nextInt(11) + 1;
		
		if(n == 1)
			category = "ANIME";
		else if(n == 2)
			category = "ANIMECHAR";
		else if(n == 3)
			category = "CARTOON";
		else if(n == 4)
			category = "CARTOONCHAR";
		else if(n == 5)
			category = "INTLTV";
		else if(n == 6)
			category = "PHILTV";
		else if(n == 7)
			category = "GAMES";
		else if(n == 8)
			category = "HOLIDAYS";
		else if(n == 9)
			category = "LANDMARKS";
		else if(n == 10)
			category = "MOVIES";
		else if(n == 11)
			category = "SPORTS";
		return category;
	}
	
	public static int getWordCount(File file){
		int count = 0;
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if(count == 0){
					category = line;
					System.out.print("Category: " + category);
				}
				count++;
			}
			br.close();		
		}catch(Exception ex){}
		return count;
	}
	
	public static String getWord(File file, int max){
		String word = "";
		Random rand = new Random();
		int count = 0;
		int n = 0;
		do{
			n = rand.nextInt(max) + 1;
			System.out.print("(" + n + ")");
		}while(n == 1);
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				count++;
				if(count == n){
					word = line;
					break;
				}
			}
			br.close();		
		}catch(Exception ex){}
		return word;
	}
	
}
