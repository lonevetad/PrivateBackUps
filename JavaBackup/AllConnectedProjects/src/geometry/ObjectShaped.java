package geometry;

import java.awt.Point;

public interface ObjectShaped extends ObjectLocated {

	public AbstractShape2D getAbstractShape();

	public void setAbstractShape(AbstractShape2D shape);

	@Override
	public default Point getLocation() {
		AbstractShape2D s;
		s = this.getAbstractShape();
		if (s == null)
			return null;
		return s.getCenter();
	}

	/** See {@literal #getLocation()}. */
	@Override
	public default void setLocation(Point location) {
		AbstractShape2D s;
		s = this.getAbstractShape();
		if (s == null)
			return;
		s.setCenter(location);
	}

}
