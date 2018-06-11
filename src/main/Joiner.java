package main;
public class Joiner {
	Player player;
	Ground ground;
	
	public void initialize() {
		player = new Player();
		ground = new Ground();
	}
	
	public void update(long elapsedTime) {
		player.update(elapsedTime);
	}
	
	public void draw() {
		player.draw();
		ground.draw();
	}
}
