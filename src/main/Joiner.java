package main;

import engine.Input;

public class Joiner {
	Player player;
	static Tilemap tilemap;
	
	public void initialize() {
		tilemap = new Tilemap(20, 14);
		tilemap.grabFromFile("map");
		
		player = new Player();
	}
	
	public void update(long elapsedTime) {
		player.update(elapsedTime);
		
		if (Input.checkKeyDown(32)) {
			player.reset();
		}
	}
	
	public void draw() {
		tilemap.draw();
		player.draw();
	}
}
