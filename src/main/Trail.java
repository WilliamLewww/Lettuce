package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import engine.Drawing;

public class Trail {
	List<List<Point>> trailList;
	List<Point> tempTrail;
	int[] trailColor = { 0, 255, 0, 1 };
	
	Trail(Point point) {
		trailList = new ArrayList<List<Point>>();
		tempTrail = new ArrayList<Point>();
		tempTrail.add(point);
	}
	
	public void reset(Point point) {
		trailList.add(new ArrayList<Point>(tempTrail));
		tempTrail.clear();
		tempTrail.add(point);
	}
	
	public void addPoint(Point point) {
		if (!point.equals(tempTrail.get(tempTrail.size() - 1))) {
			tempTrail.add(point);
		}
	}
	
	public void draw() {
		if (tempTrail.size() > 1 ) {
			Drawing.drawLineSegmented(tempTrail, 5, trailColor);
		}
		
		for (List<Point> trail : trailList) {
			Drawing.drawLineSegmented(trail, 5, trailColor);
		}
	}
}
