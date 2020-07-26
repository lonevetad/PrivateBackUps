package geometry;

import java.awt.Point;
import java.awt.geom.Point2D;

public interface ObjectShaped extends ObjectLocated {

	public AbstractShape2D getShape();

	public void setShape(AbstractShape2D shape);

	@Override
	public default Point getLocation() {
		AbstractShape2D s;
		s = this.getShape();
		if (s == null)
			return null;
		return s.getCenter();
	}

	public default Point getTopLetCorner() {
		AbstractShape2D s;
		Point2D p2d;
		s = this.getShape();
		if (s == null)
			return null;
		p2d = s.getTopLeftCorner();
		return (p2d instanceof Point) ? ((Point) p2d) : new Point((int) p2d.getX(), (int) p2d.getY());
	}

	/** See {@link #getLocation()}. */
	@Override
	public default void setLocation(Point location) {
		AbstractShape2D s;
		s = this.getShape();
		if (s == null)
			return;
		s.setCenter(location);
	}

	/** See {@literal #getLocation()}. */
	@Override
	public default void setLocation(int x, int y) {
		AbstractShape2D s;
		s = this.getShape();
		if (s == null)
			return;
		s.setCenter(x, y);
	}

	public default void setTopLeftCorner(int x, int y) {
		AbstractShape2D s;
		s = this.getShape();
		if (s == null)
			return;
		s.setLeftTopCorner(x, y);
	}

	public default void setTopLeftCorner(Point p) { setTopLeftCorner(p.x, p.y); }

}