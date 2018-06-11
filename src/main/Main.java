package main;

import engine.*;

public class Main {
	static Engine engine = new Engine();
	
	public static void main(String[] args) {
		engine.initialize();
		engine.start();
		engine.quit();
	}
}
