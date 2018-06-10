import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import core.Drawing;

public class Main {
	Joiner joiner = new Joiner();
	
	private long window;
	private long frameStart = 0, frameEnd = 0, deltaTime = 0;
	
	public static void main(String[] args) {
		new Main().start();
	}

	public void start() {
		initialize();
		updateAndRender();
		
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void initialize() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit()) { throw new IllegalStateException("Unable to initialize GLFW"); }
			
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(Drawing.screenWidth, Drawing.screenHeight, "Lettuce", NULL, NULL);
		if ( window == NULL ) { throw new RuntimeException("Failed to create the GLFW window"); }

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true);
			}
		});

		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}

		glfwMakeContextCurrent(window);
		// v-sync
		glfwSwapInterval(1);
		glfwShowWindow(window);
		
		joiner.initialize();
	}
	
	private void updateAndRender() {
		GL.createCapabilities();
		glOrtho(-Drawing.screenWidth / 2, Drawing.screenWidth / 2, Drawing.screenHeight / 2, -Drawing.screenHeight / 2, 0, 1);
		
		while (!glfwWindowShouldClose(window)) {
			frameStart = System.nanoTime();
			update();
			render();
			frameEnd = System.nanoTime();
			deltaTime = frameStart - frameEnd;
		}
	}
	
	private void update() {
		joiner.update(deltaTime);
	}
	
	private void render() {
		glClearColor(0.13f, 0.44f, 0.65f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glMatrixMode(GL_MODELVIEW);
		
		glPushMatrix();
		joiner.draw();
		glPopMatrix();
		
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
}
