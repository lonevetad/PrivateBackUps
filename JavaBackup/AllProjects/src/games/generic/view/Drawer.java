package games.generic.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import geometry.pointTools.PolygonUtilities;

/**
 * Adapter and Proxy patterns implementation.<br>
 * Defines a way to draw {@link Drawable} stuffs, wrapping other classes like
 * {@link Graphics} or {@link BufferedImage} for example.
 */
public interface Drawer {
//	public Point getLeftTopCornerCameraLocation() // to be moved in a class using this one

	public void drawImage(Image im, int x, int y);

	public void drawString(String str, int x, int y);

	public void drawRectangle(Rectangle rect);

//	public default void drawDrawable(DrawableObj drawable) { drawDrawable(drawable, drawable.getX(), drawable.getY()); }

	public default void drawDrawable(DrawableObj drawable) {
		drawImage(drawable.getCurrentImage(), drawable.getX(), drawable.getY());
	}

	public void drawLine(int x1, int y1, int x2, int y2);

	public default void drawPolygon(Polygon polygon) {
		PolygonUtilities.forEachEdge(polygon, (p1, p2) -> drawLine(p1.x, p1.y, p2.x, p2.y));
	}

}