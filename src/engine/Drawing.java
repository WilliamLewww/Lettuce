package engine;

import java.awt.Point;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Drawing {
	public static int screenWidth = 1000, screenHeight = 700;
	
	public static float convertColorIntToRGB(int rgbValue) {
		return (float)rgbValue / 255;
	}
	
	public static void drawLineSegmented(List<Point> points, int width, int[] color) {
		glLineWidth(width);
		glEnable(GL_LINE_WIDTH);
		glBegin(GL_LINE_STRIP);
		glColor4f(convertColorIntToRGB(color[0]), convertColorIntToRGB(color[1]), convertColorIntToRGB(color[2]), convertColorIntToRGB(color[3]));
		for (int x = 0; x < points.size(); x++) { 
		glVertex2f(points.get(x).x - (screenWidth / 2), 
					points.get(x).y - (screenHeight / 2)); }
		glEnd();
		glDisable(GL_LINE_WIDTH);
	}
	
	public static void drawRect(Point position, int width, int height) {
		Point[] vectors = {
				new Point(0, 0), new Point(1, 0),
				new Point(1, 1), new Point(0, 1)
		};
		
		glBegin(GL_QUADS);
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		for (int x = 0; x < 4; x++) {
			vectors[x].x *= width;
			vectors[x].y *= height;
			vectors[x].x += position.x;
			vectors[x].y += position.y;
			vectors[x].x -= screenWidth / 2;
			vectors[x].y -= screenHeight / 2;
			
			glVertex2d(vectors[x].x, vectors[x].y);
		}
		glEnd();
	}
	public static void drawRect(Point position, int width, int height, int[] color) {
		Point[] vectors = {
				new Point(0, 0), new Point(1, 0),
				new Point(1, 1), new Point(0, 1)
		};
		
		glBegin(GL_QUADS);
		glColor4f(convertColorIntToRGB(color[0]), convertColorIntToRGB(color[1]), convertColorIntToRGB(color[2]), convertColorIntToRGB(color[3]));
		for (int x = 0; x < 4; x++) {
			vectors[x].x *= width;
			vectors[x].y *= height;
			vectors[x].x += position.x;
			vectors[x].y += position.y;
			vectors[x].x -= screenWidth / 2;
			vectors[x].y -= screenHeight / 2;
			
			glVertex2d(vectors[x].x, vectors[x].y);
		}
		glEnd();
	}
	
	public static void drawNumber(int number, float x, float y, float scale) {
		glLineWidth(1);
		glBegin(GL_LINE_STRIP);
		glEnable(GL_LINE_WIDTH);
		glColor4f(1, 1, 1, 1);
		glVertex2d((x - 3) * scale - (screenWidth / 2), (y + 3) * scale - (screenHeight / 2));
		glVertex2d(x * scale - (screenWidth / 2), y * scale - (screenHeight / 2));
		glVertex2d(x * scale - (screenWidth / 2), (y + 10) * scale - (screenHeight / 2));
		glVertex2d((x + 3) * scale - (screenWidth / 2), (y + 10) * scale - (screenHeight / 2));
		glVertex2d((x - 3) * scale - (screenWidth / 2), (y + 10) * scale - (screenHeight / 2));
        glEnd();
        glDisable(GL_LINE_WIDTH);
	}
}
