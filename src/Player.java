import java.awt.Point;

import core.Drawing;

public class Player {
	Point position;
	int width, height;
	
	int[] color = { 50, 125, 255, 255 };
	
	Player() {
		position = new Point(50, 50);
		width = 100;
		height = 100;
	}
	
	public void update(long elapsedTime) {
	
	}
	
	public void draw() {
		Drawing.drawRect(position, width, height, color);
	}
}
