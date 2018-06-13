package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import engine.*;

public class Player {
	QNetwork network;
	int networkAction = -1;
	
	Point position, gridPosition;
	int width, height;
	int[] color = { 255, 125, 150, 255 };
	
	int movementSpeed = 50;
	int newSpeed = -1;
	
	boolean keyDown = false;
	boolean changedSpeed = false;
	
	int motionState = -1;
	
	List<List<Point>> trailList = new ArrayList<List<Point>>();
	List<Point> tempTrail = new ArrayList<Point>();
	int[] trailColor = { 0, 255, 0, 1 };
	
	Player() {		
		position = new Point(50, 50);
		gridPosition = new Point(1, 1);
		
		width = 50;
		height = 50;
		
		tempTrail.add(getMidpoint());
		
		network = new QNetwork(20, 14, 4);
		network.setAgentPosition(gridPosition.x, gridPosition.y);
		
		network.setGoal(13, 10);
		network.addNegativeGoal(12, 9);
		network.addNegativeGoal(7, 5);
		network.addNegativeGoal(9, 3);
		network.addNegativeGoal(13, 9);
		network.addNegativeGoal(14, 9);
		network.addNegativeGoal(12, 9);
		network.addNegativeGoal(12, 10);
		network.addNegativeGoal(15, 6);
		network.addNegativeGoal(11, 12);
		networkAction = network.getAction();
	}
	
	public void reset() {
		if (gridPosition.x != 1 || gridPosition.y != 1) {
			position = new Point(50, 50);
			gridPosition = new Point(1, 1);
			
			keyDown = false;
			motionState = -1;
			
			trailList.add(new ArrayList<Point>(tempTrail));
			tempTrail.clear();
			tempTrail.add(getMidpoint());
			networkAction = network.getAction();
		}
	}
	
	public void update(long elapsedTime) {
		//float elapsedTimeMS = (float)elapsedTime / 100000000;
		
		movement();
		
		if (checkIfOnGrid() && (tempTrail.get(tempTrail.size() - 1).x != getMidpoint().getX() ||
				tempTrail.get(tempTrail.size() - 1).y != getMidpoint().getY())) {
			tempTrail.add(getMidpoint());
			if (newSpeed != -1) {
				movementSpeed = newSpeed;
				newSpeed = -1;
			}
		}
		
		if (changedSpeed == false) {
			if (Input.checkKeyDown(262)) {
				switch (movementSpeed) {
				case 5:
					newSpeed = 10;
					break;
				case 10:
					newSpeed = 25;
					break;
				case 25:
					newSpeed = 50;
					break;
				}
				
				changedSpeed = true;
			}
			
			if (Input.checkKeyDown(263)) {
				switch (movementSpeed) {
				case 10:
					newSpeed = 5;
					break;
				case 25:
					newSpeed = 10;
					break;
				case 50:
					newSpeed = 25;
					break;
				}
				
				changedSpeed = true;
			}
		}
		
		if (keyDown == false) {
			changedSpeed = false;
		}
		
		keyDown = checkArrowPressed();
	}
	
	public void draw() {		
//		if (tempTrail.size() > 1 ) {
//			Drawing.drawLineSegmented(tempTrail, 5, trailColor);
//		}
//		
//		for (List<Point> trail : trailList) {
//			Drawing.drawLineSegmented(trail, 5, trailColor);
//		}
		
		network.draw();
		
		Drawing.drawRect(position, width, height, color);
	}
	
	private Point getMidpoint() {
		return new Point(position.x + (width / 2), position.y + (height / 2));
	}
	
	private boolean checkIfOnGrid() {
		if (position.x % 50 == 0 && position.y % 50 == 0) {
			return true;
		}
		
		return false;
	}
	
	private boolean checkArrowPressed() {
		if (Input.checkKeyDown(262) || Input.checkKeyDown(263) || Input.checkKeyDown(264) || Input.checkKeyDown(265)) {
			return true;
		}
		
		return false;
	}
	
	private void movement() {
		handleRandomAction();
		
		// (!keyDown && Input.checkKeyDown(262) && !Input.checkKeyDown(263))
		if ((motionState == 0 || networkAction == 0) && gridPosition.x < 19) {
			moveRight();
			keyDown = true;
		}
		
		// (!keyDown && !Input.checkKeyDown(262) && Input.checkKeyDown(263))
		if ((motionState == 1 || networkAction == 1) && gridPosition.x > 0) { 
			moveLeft();
			keyDown = true;
		}
		
		// (!keyDown && Input.checkKeyDown(264) && !Input.checkKeyDown(265))
		if ((motionState == 2 || networkAction == 2) && gridPosition.y < 13) {
			moveDown();
			keyDown = true;
		}
		
		// (!keyDown && !Input.checkKeyDown(264) && Input.checkKeyDown(265))
		if ((motionState == 3 || networkAction == 3) && gridPosition.y > 0) { 
			moveUp();
			keyDown = true;
		}
	}
	
	private void handleRandomAction() {
		Random random = new Random();
		
		if (networkAction == -1) {
			networkAction = random.nextInt((3 - 0) + 1);
		}
		
		if (gridPosition.x == 19 && networkAction == 0) {
			networkAction = random.nextInt((3 - 0) + 1);
		}
		
		if (gridPosition.x == 0 && networkAction == 1) {
			networkAction = random.nextInt((3 - 0) + 1);
		}
		
		if (gridPosition.y == 13 && networkAction == 2) {
			networkAction = random.nextInt((3 - 0) + 1);
		}
		
		if (gridPosition.y == 0 && networkAction == 3) {
			networkAction = random.nextInt((3 - 0) + 1);
		}
	}
	
	private void handleGoal() {
		if (gridPosition.equals(network.goal)) {
			tempTrail.add(getMidpoint());
			reset();
		}
		else {
			for (Point negativeGoal : network.negativeGoals) {
				if (gridPosition.equals(negativeGoal)) {
					tempTrail.add(getMidpoint());
					reset();
				}
			}
		}
	}
	
	private void moveRight() {
		position.x += movementSpeed;
		
		if (position.x % 50 == 0) {
			gridPosition.x += 1;
			network.setAgentPosition(gridPosition.x, gridPosition.y);
			network.setQ(new Point(gridPosition.x - 1, gridPosition.y), 0);
			handleGoal();
			
			networkAction = network.getAction();
			motionState = -1;
		}
		else {
			motionState = 0;
		}
	}
	
	private void moveLeft() {
		position.x -= movementSpeed;
		
		if (position.x % 50 == 0) {
			gridPosition.x -= 1;
			network.setAgentPosition(gridPosition.x, gridPosition.y);
			network.setQ(new Point(gridPosition.x + 1, gridPosition.y), 1);
			handleGoal();
			
			networkAction = network.getAction();
			motionState = -1;
		}
		else {
			motionState = 1;
		}
	}
	
	private void moveDown() {
		position.y += movementSpeed;
		
		if (position.y % 50 == 0) {
			gridPosition.y += 1;
			network.setAgentPosition(gridPosition.x, gridPosition.y);
			network.setQ(new Point(gridPosition.x, gridPosition.y - 1), 2);
			handleGoal();
			
			networkAction = network.getAction();
			motionState = -1;
		}
		else {
			motionState = 2;
		}
	}
	
	private void moveUp() {
		position.y -= movementSpeed;
		
		if (position.y % 50 == 0) {
			gridPosition.y -= 1;
			network.setAgentPosition(gridPosition.x, gridPosition.y);
			network.setQ(new Point(gridPosition.x, gridPosition.y + 1), 3);
			handleGoal();
			
			networkAction = network.getAction();
			motionState = -1;
		}
		else {
			motionState = 3;
		}
	}
}
