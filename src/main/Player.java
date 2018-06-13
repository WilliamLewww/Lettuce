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
	
	int motionState = -1;
	
	Trail trail;
	
	Player() {		
		position = new Point(50, 50);
		gridPosition = new Point(1, 1);
		
		width = 50;
		height = 50;
		
		trail = new Trail(getMidpoint());
		
		network = new QNetwork(20, 14, 4, true, false);
		network.setAgentPosition(gridPosition.x, gridPosition.y);
		
		network.setGoal(19, 9);
		network.addNegativeGoal(10, 10);
		network.addNegativeGoal(11, 10);
		network.addNegativeGoal(12, 10);
		network.addNegativeGoal(13, 10);
		network.addNegativeGoal(14, 10);
		network.addNegativeGoal(15, 10);
		network.addNegativeGoal(16, 10);
		network.addNegativeGoal(17, 10);
		network.addNegativeGoal(18, 10);
		network.addNegativeGoal(19, 10);
		
		network.addNegativeGoal(10, 5);
		network.addNegativeGoal(10, 6);
		network.addNegativeGoal(10, 7);
		network.addNegativeGoal(10, 9);
		
		network.addNegativeGoal(11, 9);
		
		network.addNegativeGoal(12, 5);
		network.addNegativeGoal(12, 7);
		network.addNegativeGoal(12, 8);
		network.addNegativeGoal(12, 9);
		
		network.addNegativeGoal(13, 5);
		
		network.addNegativeGoal(14, 5);
		network.addNegativeGoal(14, 7);
		
		network.addNegativeGoal(15, 5);
		network.addNegativeGoal(15, 7);
		
		network.addNegativeGoal(16, 7);
		
		network.addNegativeGoal(17, 5);
		network.addNegativeGoal(17, 7);
		
		network.addNegativeGoal(18, 5);
		network.addNegativeGoal(19, 5);
		
		networkAction = network.getAction();
	}
	
	public void reset() {
		if (gridPosition.x != 1 || gridPosition.y != 1) {
			trail.addPoint(getMidpoint());
			
			position = new Point(50, 50);
			gridPosition = new Point(1, 1);
			
			motionState = -1;
			
			trail.reset(getMidpoint());
			networkAction = network.getAction();
		}
	}
	
	public void update(long elapsedTime) {
		//float elapsedTimeMS = (float)elapsedTime / 100000000;
		
		movement();
		
		if (checkIfOnGrid()) {
			trail.addPoint(getMidpoint());
		}
	}
	
	public void draw() {		
		trail.draw();
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
	
	private void movement() {
		if (motionState == -1) {
			handleRandomAction();
		}
		
		// (!keyDown && Input.checkKeyDown(262) && !Input.checkKeyDown(263))
		if ((motionState == 0 || networkAction == 0) && gridPosition.x < 19) {
			moveRight();
		}
		
		// (!keyDown && !Input.checkKeyDown(262) && Input.checkKeyDown(263))
		if ((motionState == 1 || networkAction == 1) && gridPosition.x > 0) { 
			moveLeft();
		}
		
		// (!keyDown && Input.checkKeyDown(264) && !Input.checkKeyDown(265))
		if ((motionState == 2 || networkAction == 2) && gridPosition.y < 13) {
			moveDown();
		}
		
		// (!keyDown && !Input.checkKeyDown(264) && Input.checkKeyDown(265))
		if ((motionState == 3 || networkAction == 3) && gridPosition.y > 0) { 
			moveUp();
		}
	}
	
	private void handleRandomAction() {
		Random random = new Random();
		List<Integer> boundsList = new ArrayList<Integer>();
		boundsList.add(0);
		boundsList.add(1);
		boundsList.add(2);
		boundsList.add(3);
		
		if (gridPosition.x == 19) {
			boundsList.remove(Integer.valueOf(0));
		}
		
		if (gridPosition.x == 0) {
			boundsList.remove(Integer.valueOf(1));
		}
		
		if (gridPosition.y == 13) {
			boundsList.remove(Integer.valueOf(2));
		}
		
		if (gridPosition.y == 0) {
			boundsList.remove(Integer.valueOf(3));
		}
		
		if (networkAction == -1) {
			networkAction = random.nextInt((3 - 0) + 1);
		}
		else {
			if (motionState == -1) {
				networkAction = network.getAction(boundsList);
			}
		}
	}
	
	private void handleGoal() {
		if (gridPosition.equals(network.goal)) {
			reset();
		}
		else {
			for (Point negativeGoal : network.negativeGoals) {
				if (gridPosition.equals(negativeGoal)) {
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
			
			motionState = -1;
		}
		else {
			motionState = 3;
		}
	}
}
