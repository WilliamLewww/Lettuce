package core;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

public class Input {
	private long window;
	private static List<Integer> keyDownList = new ArrayList<Integer>();
	
	public void linkWindow(long window) {
		this.window = window;
	}
	
	public void setKeyCallback() {
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true);
			}
			else {
				if (action == GLFW_PRESS) {
					keyDownList.add(key);
					//System.out.println(key);
				}
				if (action == GLFW_RELEASE) {
					keyDownList.remove(Integer.valueOf(key));
				}
			}
		});
	}
	
	public static boolean checkKeyDown(int keycode) {
		if (keyDownList.contains(Integer.valueOf(keycode))) {
			return true;
		}
		
		return false;
	}
}
