package main;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import engine.Drawing;

public class Tilemap {
	int[][] tileList;
	static int tileWidth;
	static int tileHeight;
	
	int[] tileColor = { 0, 0, 0, 255 };
	int[] goalColor = { 255, 255, 255, 255 };
	
	Tilemap(int width, int height) {
		tileList = new int[width][height];
		
		tileWidth = 50;
		tileHeight = 50;
	}
	
	public int getTile(int x, int y) {
		return tileList[x][y];
	}
	
	public Point[] getGoals() {
		List<Point> points = new ArrayList<Point>();
		for (int x = 0; x < tileList.length; x++) {
			for (int y = 0; y < tileList[0].length; y++) {
				if (tileList[x][y] == 2) {
					points.add(new Point(x,y));
				}
			}
		}
		
		Point[] pointArray = new Point[points.size()];
		pointArray = points.toArray(pointArray);
		
		return pointArray;
	}
	
	public Point[] getNegativeGoals() {
		List<Point> points = new ArrayList<Point>();
		for (int x = 0; x < tileList.length; x++) {
			for (int y = 0; y < tileList[0].length; y++) {
				if (tileList[x][y] == 3) {
					points.add(new Point(x,y));
				}
			}
		}
		
		Point[] pointArray = new Point[points.size()];
		pointArray = points.toArray(pointArray);
		
		return pointArray;
	}
	
	public void grabFromFile(String fileName) {
		int col = 0;
		try {
			File file = new File("res/" + fileName + ".txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String[] lineSplit;
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				lineSplit = line.split("\\s+");
				
				for (int x = 0; x < lineSplit.length; x++) {
					tileList[x][col] = Integer.valueOf(lineSplit[x]);
				}
				col += 1;
			}
			
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw() {
		for (int x = 0; x < tileList.length; x++) {
			for (int y = 0; y < tileList[x].length; y++) {
				if (tileList[x][y] == 1) {
					Drawing.drawRect(new Point(x * tileWidth, y * tileHeight), tileWidth, tileHeight, tileColor);
				}
				if (tileList[x][y] == 2) {
					Drawing.drawRect(new Point(x * tileWidth, y * tileHeight), tileWidth, tileHeight, goalColor);
				}
				if (tileList[x][y] == 3) {
					Drawing.drawRect(new Point(x * tileWidth, y * tileHeight), tileWidth, tileHeight, goalColor);
				}
			}
		}
	}
}
