package geometry;

import java.awt.Point;
import java.io.Serializable;

public interface ObjectLocated extends Serializable {

	/** The location is referred to be the centre of this object */
	public Point getLocation();
//	public Point2D.Double getLocation();

	/** See {@literal #getLocation()}. */
	public void setLocation(Point location);
//	public void setLocation(Point2D.Double location);
}