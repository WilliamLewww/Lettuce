package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import engine.*;

public class Player {
	Point position, gridPosition;
	int width, height;
	int[] color = { 255, 125, 150, 255 };
	
	int movementSpeed = 5;
	
	boolean keyDown = false;
	int motionState = -1;
	
	List<List<Point>> trailList = new ArrayList<List<Point>>();
	List<Point> tempTrail = new ArrayList<Point>();
	int[] trailColor = { 0, 255, 0, 25 };
	
	Player() {
		position = new Point(50, 50);
		gridPosition = new Point(1, 1);
		
		width = 50;
		height = 50;
		
		tempTrail.add(new Point(position.x + (width / 2), position.y + (height / 2)));
	}
	
	public void reset() {
		if (gridPosition.x != 1 || gridPosition.y != 1) {
			position = new Point(50, 50);
			gridPosition = new Point(1, 1);
			
			keyDown = false;
			motionState = -1;
			
			trailList.add(new ArrayList<Point>(tempTrail));
			tempTrail.clear();
			tempTrail.add(new Point(position.x + (width / 2), position.y + (height / 2)));
		}
	}
	
	public void update(long elapsedTime) {
		//float elapsedTimeMS = (float)elapsedTime / 100000000;
		
		horizontalMovement();
		verticalMovement();
		
		keyDown = checkArrowPressed();
	}
	
	private boolean checkArrowPressed() {
		if (Input.checkKeyDown(262) || Input.checkKeyDown(263) || Input.checkKeyDown(264) || Input.checkKeyDown(265)) {
			return true;
		}
		
		return false;
	}
	
	private void horizontalMovement() {
		if ((!keyDown && Input.checkKeyDown(262) && !Input.checkKeyDown(263)) || motionState == 0) {
			moveRight();
			keyDown = true;
		}
		
		if ((!keyDown && !Input.checkKeyDown(262) && Input.checkKeyDown(263)) || motionState == 1) { 
			moveLeft();
			keyDown = true;
		}
	}
	
	private void moveRight() {
		position.x += movementSpeed;
		
		if (position.x % 50 == 0) {
			tempTrail.add(new Point(position.x + (width / 2), position.y + (height / 2)));
			gridPosition.x += 1;
			motionState = -1;
		}
		else {
			motionState = 0;
		}
	}
	
	private void moveLeft() {
		position.x -= movementSpeed;
		
		if (position.x % 50 == 0) {
			tempTrail.add(new Point(position.x + (width / 2), position.y + (height / 2)));
			gridPosition.x -= 1;
			motionState = -1;
		}
		else {
			motionState = 1;
		}
	}
	
	private void verticalMovement() {
		if ((!keyDown && Input.checkKeyDown(264) && !Input.checkKeyDown(265)) || motionState == 2) {
			moveDown();
			keyDown = true;
		}
		
		if ((!keyDown && !Input.checkKeyDown(264) && Input.checkKeyDown(265)) || motionState == 3) { 
			moveUp();
			keyDown = true;
		}
	}
	
	private void moveDown() {
		position.y += movementSpeed;
		
		if (position.y % 50 == 0) {
			tempTrail.add(new Point(position.x + (width / 2), position.y + (height / 2)));
			gridPosition.y += 1;
			motionState = -1;
		}
		else {
			motionState = 2;
		}
	}
	
	private void moveUp() {
		position.y -= movementSpeed;
		
		if (position.y % 50 == 0) {
			tempTrail.add(new Point(position.x + (width / 2), position.y + (height / 2)));
			gridPosition.y -= 1;
			motionState = -1;
		}
		else {
			motionState = 3;
		}
	}
	
	public void draw() {
		Drawing.drawRect(position, width, height, color);
		
		for (List<Point> trail : trailList) {
			Drawing.drawLineSegmented(trail, trailColor);
		}
	}
}
