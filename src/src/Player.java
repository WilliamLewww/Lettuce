package src;

import java.awt.Point;

import core.*;

public class Player {
	Point position;
	int width, height;
	int[] color = { 255, 125, 150, 255 };
	
	float velocityX = 0, velocityY = 0;
	
	boolean onGround = false;
	boolean release = false;
	
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
	}
	
	private void verticalMovement() {
		if (position.y + height >= Ground.position.y) { onGround = true; }
		
		if (onGround == false) {
			if (!Input.checkKeyDown(32) && velocityY < 0) {
				velocityY += 2;
			}
			velocityY += 1;
		}
		else {
			velocityY = 0;
			position.y = Ground.position.y - height;
			
			if (!Input.checkKeyDown(32)) {
				release = true;
			}
			
			if (Input.checkKeyDown(32) && release == true) {
				onGround = false;
				release = false;
				velocityY = -40;
			}
		}
	}
	
	public void draw() {
		Drawing.drawRect(position, width, height, color);
	}
}
