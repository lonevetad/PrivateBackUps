package geometry;

import java.awt.Point;

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