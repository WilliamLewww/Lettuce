package main;

import engine.Input;

public class Joiner {
	Player player;
	
	public void initialize() {
		player = new Player();
	}
	
	public void update(long elapsedTime) {
		player.update(elapsedTime);
		
		if (Input.checkKeyDown(32)) {
			player.reset();
		}
	}
	
	public void draw() {
		player.draw();
	}
}
