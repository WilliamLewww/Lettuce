package main;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import engine.Drawing;

//QNetwork -> setGoal -> setAgentPosition ||| getAction -> setAgentPosition -> movePlayer -> setQ
public class QNetwork {
	Point agentPosition;
	List<Point> goals;
	List<Point> negativeGoals;
	
	double[][][] table;
	
	float gamma = 0.8f;
	float delta = 1.2f;
	
	float negativeFeedback = -0.5f;
	
	int[] color = { 0, 255, 0 , 255 };
	int[] colorN = { 255, 0, 0, 255 };
	
	boolean negativeChain = false;
	
	QNetwork(int width, int height, int actions, boolean negativeFeedback, boolean negativeChain) {
		table = new double[width][height][actions];
		agentPosition = new Point();
		goals = new ArrayList<Point>();
		negativeGoals = new ArrayList<Point>();
		
		this.negativeChain = negativeChain;
		if (negativeFeedback == false) {
			this.negativeFeedback = 0.0f;
		}
	}
	
	public void addGoal(Point point) {
		goals.add(new Point(point.x, point.y));
	}
	
	public void addGoals(Point[] points) {
		for (int x = 0; x < points.length; x++) {
			goals.add(new Point(points[x].x, points[x].y));
		}
	}
	
	public void addNegativeGoal(Point point) {
		negativeGoals.add(new Point(point.x, point.y));
	}
	
	public void addNegativeGoal(Point[] points) {
		for (int x = 0; x < points.length; x++) {
			negativeGoals.add(new Point(points[x].x, points[x].y));
		}
	}
	
	public void setAgentPosition(int x, int y) {
		agentPosition.setLocation(x, y);
	}
	
	public int getActionProb(List<Integer> availableDirections) {
		if (availableDirections.size() == 1) {
			return availableDirections.get(0);
		}
		
		double max = table[agentPosition.x][agentPosition.y][availableDirections.get(0)];
		double min = table[agentPosition.x][agentPosition.y][availableDirections.get(0)];
		List<Point2D.Double> points = new ArrayList<Point2D.Double>();
		
		for (int x = 0; x < availableDirections.size(); x++) {
			points.add(new Point2D.Double(availableDirections.get(x), table[agentPosition.x][agentPosition.y][availableDirections.get(x)]));
			
			if (table[agentPosition.x][agentPosition.y][availableDirections.get(x)] > max) {
				max = table[agentPosition.x][agentPosition.y][availableDirections.get(x)];
			}
			if (table[agentPosition.x][agentPosition.y][availableDirections.get(x)] < min ) {
				min = table[agentPosition.x][agentPosition.y][availableDirections.get(x)];
			}
		}
		
		Collections.sort(points, new Comparator<Point2D>() {
			@Override
			public int compare(Point2D o1, Point2D o2) {
				return Double.compare(o1.getY(), o2.getY());
			}
		});
		
		double total = 0;
		for (int x = 0; x < points.size(); x++) {
			points.get(x).y -= min;
			points.get(x).y += 1;
			total += points.get(x).y;
		}
		
		Random random = new Random();
		int randomInt = random.nextInt(101);
		double cumulative = 0;
		
		for (int x = 0; x < points.size(); x++) {
			cumulative += (points.get(x).y / total) * 100;
			
			if (randomInt < cumulative) {
				return (int)points.get(x).x;
			}
		}
		
		return -50;
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
		
		boolean isNegativeGoal = false;
		if (highestValue < 0) {
			for (Point negativeGoal : negativeGoals) {
				if (agentPosition.equals(negativeGoal)) {
					table[prePosition.x][prePosition.y][preAction] += (gamma * highestValue);
					isNegativeGoal = true;
				}
			}
			
			if (isNegativeGoal == false && negativeChain) {
				table[prePosition.x][prePosition.y][preAction] += (gamma * highestValue) / 8;
			}
		}
		else {
			table[prePosition.x][prePosition.y][preAction] += gamma * highestValue;
		}
		
		for (Point goal : goals) {
			if (agentPosition.equals(goal)) {
				if (table[goal.x][goal.y][0] == 0) {
					table[goal.x][goal.y][0] = 100;
				}
				else {
					table[goal.x][goal.y][0] = table[goal.x][goal.y][0] * delta;
				}
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
							boolean isGoal = false;
							
							for (Point goal : goals) {
								if (x == goal.x && y == goal.y) {
									isGoal = true;
								}
							}
							
							if (isGoal) {
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
		double greatestValue = 0;
		for (Point goal : goals) {
			if (table[goal.x][goal.y][0] < greatestValue) {
				greatestValue = table[goal.x][goal.y][0];
			}
		}
		
		int[] color = new int[4];
		color[0] = 0;
		color[1] = (int)((value / greatestValue) * 255);
		color[2] = 0;
		color[3] = (int)(255 - (greatestValue / value));
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
