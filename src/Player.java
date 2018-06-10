import java.awt.Point;

import core.*;

public class Player {
	Point position;
	int width, height;
	int[] color = { 255, 125, 150, 255 };
	
	float velocityX = 0, velocityY = 0;
	
	boolean onGround = false;
	
	Player() {
		position = new Point(100, 100);
		width = 50;
		height = 50;
	}
	
	public void update(long elapsedTime) {
		float elapsedTimeMS = (float)elapsedTime / 100000000;
		
		if (Input.checkKeyDown(262)) {
			velocityX = 25;
		}
		else {
			if (Input.checkKeyDown(263)) {
				velocityX = -25;
			}
			else {
				velocityX = 0;
			}
		}
		
		if (position.y + height >= Ground.position.y) { onGround = true; }
		
		if (onGround == false) {
			velocityY += 1;
		}
		else {
			velocityY = 0;
			position.y = Ground.position.y - height;
			
			if (Input.checkKeyDown(32)) {
				onGround = false;
				velocityY = -40;
			}
		}
		
		position.x += velocityX * elapsedTimeMS;
		position.y += velocityY * elapsedTimeMS;
	}
	
	public void draw() {
		Drawing.drawRect(position, width, height, color);
	}
}
