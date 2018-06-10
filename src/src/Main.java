package src;

import core.*;

public class Main {
	static Engine engine = new Engine();
	
	public static void main(String[] args) {
		
		engine.initialize();
		engine.start();
		engine.quit();
	}
}
