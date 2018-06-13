package main;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import engine.Drawing;

public class QNetwork {
	Point agentPosition, goal;
	List<Point> negativeGoals;
	
	double[][][] table;
	
	float gamma = 0.8f;
	float delta = 1.2f;
	
	float negativeFeedback = -0.05f;
	
	int[] color = { 0, 255, 0 , 255 };
	int[] colorN = { 255, 0, 0, 255 };
	
	QNetwork(int width, int height, int actions) {
		table = new double[width][height][actions];
		agentPosition = new Point();
		goal = new Point();
		negativeGoals = new ArrayList<Point>();
	}
	
	public void setGoal(int x, int y) {
		goal.setLocation(x, y);
	}
	
	public void addNegativeGoal(int x, int y) {
		negativeGoals.add(new Point(x, y));
	}
	
	// QNetwork -> setGoal -> setAgentPosition ||| getAction -> setAgentPosition -> movePlayer -> setQ
	
	// 0 = Right
	// 1 = Left
	// 2 = Down
	// 3 = Up
	
	public void setAgentPosition(int x, int y) {
		agentPosition.setLocation(x, y);
	}
	
	public int getAction(List<Integer> availableDirections) {
		int highestIndex = availableDirections.get(0);
		double highestValue = table[agentPosition.x][agentPosition.y][availableDirections.get(0)];
		List<Integer> similar = new ArrayList<Integer>();
		
		if (availableDirections.size() == 1) {
			return availableDirections.get(0);
		}
		
		for (int x = 1; x < availableDirections.size(); x++) {
			if (table[agentPosition.x][agentPosition.y][availableDirections.get(x)] > highestValue) {
				highestIndex = availableDirections.get(x);
				highestValue = table[agentPosition.x][agentPosition.y][availableDirections.get(x)];
			}
		}
		
		similar.add(highestIndex);
		for (int x = 0; x < availableDirections.size(); x++) {
			if (table[agentPosition.x][agentPosition.y][availableDirections.get(x)] == highestValue &&
					availableDirections.get(x) != highestIndex) {
				similar.add(availableDirections.get(x));
			}
		}
		
		if (similar.size() > 1) {
			Random random = new Random();
			return similar.get(random.nextInt(similar.size()));
		}
		
		return highestIndex;
	}
	
	public int getAction() {
		int highestIndex = 0;
		double highestValue = table[agentPosition.x][agentPosition.y][0];
		List<Integer> similar = new ArrayList<Integer>();
		
		for (int x = 1; x < table[0][0].length; x++) {
			if (table[agentPosition.x][agentPosition.y][x] > highestValue) {
				highestIndex = x;
				highestValue = table[agentPosition.x][agentPosition.y][x];
			}
		}
		
		similar.add(highestIndex);
		for (int x = 0; x < table[0][0].length; x++) {
			if (table[agentPosition.x][agentPosition.y][x] == highestValue && x != highestIndex) {
				similar.add(x);
			}
		}
		
		if (similar.size() > 1) {
			Random random = new Random();
			return similar.get(random.nextInt(similar.size()));
		}
		
		return highestIndex;
	}
	
	public void setQ(Point prePosition, int preAction) {
		double highestValue = 0;
		for (int x = 0; x < table[0][0].length; x++) {
			if (Math.abs(table[agentPosition.x][agentPosition.y][x]) > Math.abs(highestValue)) {
				highestValue = table[agentPosition.x][agentPosition.y][x];
			}
		}
		
		table[prePosition.x][prePosition.y][preAction] += negativeFeedback;
		
		if (highestValue < 0) {
			table[prePosition.x][prePosition.y][preAction] += (gamma * (highestValue / 2));
		}
		else {
			table[prePosition.x][prePosition.y][preAction] += gamma * highestValue;
		}
		
		if (agentPosition.equals(goal)) {
			if (table[goal.x][goal.y][0] == 0) {
				table[goal.x][goal.y][0] = 100;
			}
			else {
				table[goal.x][goal.y][0] = table[goal.x][goal.y][0] * delta;
			}
		}
		
		for (Point negativeGoal : negativeGoals) {
			if (agentPosition.equals(negativeGoal)) {
				if (table[negativeGoal.x][negativeGoal.y][0] == 0) {
					table[negativeGoal.x][negativeGoal.y][0] = -100;
				}
				else {
					table[negativeGoal.x][negativeGoal.y][0] *= delta + 0.3f;
				}
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
								Drawing.drawTriangle(new Point2D.Double((x * 50) + 30, (y * 50) + 15), 20, 20, 90, getColorRelativeToGoal(table[x][y][z]));
							}
							break;
						case 1:
							Drawing.drawTriangle(new Point2D.Double(x * 50, (y * 50) + 15), 20, 20, 270, getColorRelativeToGoal(table[x][y][z]));
							break;
						case 2:
							Drawing.drawTriangle(new Point2D.Double((x * 50) + 15, (y * 50) + 30), 20, 20, 0, getColorRelativeToGoal(table[x][y][z]));
							break;
						case 3:
							Drawing.drawTriangle(new Point2D.Double((x * 50) + 15, y * 50), 20, 20, 180, getColorRelativeToGoal(table[x][y][z]));
							break;
						}
					}
					
					if (table[x][y][z] < 0) {
						switch (z) {
						case 0:
							boolean isGoal = false;
							
							for (Point negativeGoal : negativeGoals) {
								if (x == negativeGoal.x && y == negativeGoal.y) {
									isGoal = true;
								}
							}
							
							if (isGoal) {
								Drawing.drawRect(new Point(x * 50, y * 50), 50, 50, colorN);
							}
							else {
								Drawing.drawTriangle(new Point2D.Double((x * 50) + 30, (y * 50) + 15), 20, 20, 90, getColorRelativeToNegativeGoal(table[x][y][z]));
							}
							break;
						case 1:
							Drawing.drawTriangle(new Point2D.Double(x * 50, (y * 50) + 15), 20, 20, 270, getColorRelativeToNegativeGoal(table[x][y][z]));
							break;
						case 2:
							Drawing.drawTriangle(new Point2D.Double((x * 50) + 15, (y * 50) + 30), 20, 20, 0, getColorRelativeToNegativeGoal(table[x][y][z]));
							break;
						case 3:
							Drawing.drawTriangle(new Point2D.Double((x * 50) + 15, y * 50), 20, 20, 180, getColorRelativeToNegativeGoal(table[x][y][z]));
							break;
						}
					}
				}
			}
		}
	}
	
	private int[] getColorRelativeToGoal(double value) {
		int[] color = new int[4];
		color[0] = 0;
		color[1] = (int)((value / table[goal.x][goal.y][0]) * 255);
		color[2] = 0;
		color[3] = (int)(255 - (table[goal.x][goal.y][0] / value));
		return color;
	}
	
	private int[] getColorRelativeToNegativeGoal(double value) {
		double greatestValue = 0;
		for (Point negativeGoal : negativeGoals) {
			if (table[negativeGoal.x][negativeGoal.y][0] < greatestValue) {
				greatestValue = table[negativeGoal.x][negativeGoal.y][0];
			}
		}
		
		int[] color = new int[4];
		color[0] = (int)((value / greatestValue) * 255);
		color[1] = 0;
		color[2] = 0;
		color[3] = (int)((value / greatestValue) * 255);
		return color;
	}
}
