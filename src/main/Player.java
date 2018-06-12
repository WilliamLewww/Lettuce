package main;

import java.awt.Point;

import engine.*;

public class Player {
	Point position;
	int width, height;
	int[] color = { 255, 125, 150, 255 };
	
	float velocityX = 0, velocityY = 0;
	
	Player() {
		position = new Point(100, 100);
		width = 50;
		height = 50;
	}
	
	public void update(long elapsedTime) {
		float elapsedTimeMS = (float)elapsedTime / 100000000;
		
		horizontalMovement();
		verticalMovement();
		
		position.x += velocityX * elapsedTimeMS;
		position.y += velocityY * elapsedTimeMS;
	}
	
	private void horizontalMovement() {
		velocityX = 0;
		
		if (Input.checkKeyDown(262) && !Input.checkKeyDown(263)) {
			velocityX = 25;
		}
		
		if (!Input.checkKeyDown(262) && Input.checkKeyDown(263)) { 
			velocityX = -25;
		}
	}
	
	private void verticalMovement() {
		velocityY = 0;
		
		if (Input.checkKeyDown(264) && !Input.checkKeyDown(265)) {
			velocityY = 25;
		}
		
		if (!Input.checkKeyDown(264) && Input.checkKeyDown(265)) { 
			velocityY = -25;
		}
	}
	
	public void draw() {
		Drawing.drawRect(position, width, height, color);
	}
}
