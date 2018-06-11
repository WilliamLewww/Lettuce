package main;
import java.awt.Point;

import engine.*;

public class Ground {
	static Point position;
	static int width, height;
	
	int[] color = { 0, 0, 0, 0 };
	
	Ground() {
		position = new Point(0, 600);
		width = 1000;
		height = 100;
	}
	
	void draw() {
		Drawing.drawRect(position, width, height, color);
	}
	
	static Point getPosition() {
		return position;
	}
	
	static int getWidth() {
		return width;
	}
	
	static int getHeight() {
		return height;
	}
}
