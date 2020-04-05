package geometry;

import java.awt.Point;

import tools.ObjectWithID;

public interface ObjectLocated extends ObjectWithID {

	/** The location is referred to be the center of this object */
	public Point getLocation();
//	public Point2D.Double getLocation();

	/** See {@link #getLocation()}. */
	public void setLocation(Point location);
//	public void setLocation(Point2D.Double location);

	/** See {@link #getLocation()}. */
	public void setLocation(int x, int y);
}