package main;

import java.awt.Point;
import java.awt.geom.Point2D;

import engine.Drawing;

public class QNetwork {
	Point agentPosition, goal;
	double[][][] table;
	
	float gamma = 0.5f;
	float delta = 1.2f;
	
	int[] color = { 255, 0, 0, 25 };
	
	QNetwork(int width, int height, int actions) {
		table = new double[width][height][actions];
		agentPosition = new Point();
		goal = new Point();
	}
	
	public void setGoal(int x, int y) {
		goal.setLocation(x, y);
	}
	
	// QNetwork -> setGoal -> setAgentPosition ||| getAction -> setAgentPosition -> movePlayer -> setQ
	
	// 0 = Right
	// 1 = Left
	// 2 = Down
	// 3 = Up
	
	public void setAgentPosition(int x, int y) {
		agentPosition.setLocation(x, y);
	}
	public int getAction() {
		int highestIndex = -1;
		double highestValue = 0;
		
		for (int x = 0; x < table[0][0].length; x++) {
			if (table[agentPosition.x][agentPosition.y][x] > highestValue) {
				highestIndex = x;
				highestValue = table[agentPosition.x][agentPosition.y][x];
			}
		}
		
		return highestIndex;
	}
	
	public void setQ(Point prePosition, int preAction) {
		double highestValue = 0;
		for (int x = 0; x < table[0][0].length; x++) {
			if (table[agentPosition.x][agentPosition.y][x] > highestValue) {
				highestValue = table[agentPosition.x][agentPosition.y][x];
			}
		}
		
		table[prePosition.x][prePosition.y][preAction] = gamma * highestValue;
		
		if (agentPosition.equals(goal)) {
			if (table[goal.x][goal.y][0] == 0) {
				table[goal.x][goal.y][0] = 100;
			}
			else {
				table[goal.x][goal.y][0] = table[goal.x][goal.y][0] * delta;
			}
		}
	}
	
	public void draw() {
		for (int x = 0; x < table.length; x++) {
			for (int y = 0; y < table[0].length; y++) {
				for (int z = 0; z < table[x][y].length; z++) {
					if (table[x][y][z] > 0) {
						switch (z) {
						case 0:
							if (x == goal.x && y == goal.y) {
								Drawing.drawRect(new Point(x * 50, y * 50), 50, 50, color);
							}
							else {
								Drawing.drawTriangle(new Point2D.Double(x * 50, y * 50), 50, 50, 90, color);
							}
							break;
						case 1:
							Drawing.drawTriangle(new Point2D.Double(x * 50, y * 50), 50, 50, 270, color);
							break;
						case 2:
							Drawing.drawTriangle(new Point2D.Double(x * 50, y * 50), 50, 50, 0, color);
							break;
						case 3:
							Drawing.drawTriangle(new Point2D.Double(x * 50, y * 50), 50, 50, 180, color);
							break;
						}
					}
				}
			}
		}
	}
}
