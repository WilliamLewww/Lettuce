public class Joiner {
	Player player;
	
	public void initialize() {
		player = new Player();
	}
	
	public void update(long elapsedTime) {
		player.update(elapsedTime);
	}
	
	public void draw() {
		player.draw();
	}
}
