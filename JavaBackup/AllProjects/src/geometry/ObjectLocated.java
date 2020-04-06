package geometry;

import java.awt.Point;
import java.util.function.Function;

import tools.ObjectWithID;

public interface ObjectLocated extends ObjectWithID {
	public static final Function<ObjectLocated, Integer> KEY_EXTRACTOR = o -> o.getID();

	public default int getx() {
		return getLocation().x;
	}

	public default int gety() {
		return getLocation().y;
	}

	/** The location is referred to be the center of this object */
	public Point getLocation();
//	public Point2D.Double getLocation();

	/** See {@link #getLocation()}. */
	public void setLocation(Point location);
//	public void setLocation(Point2D.Double location);

	/** See {@link #getLocation()}. */
	public void setLocation(int x, int y);

	//

	//

	public static class PointWrapper extends Point implements ObjectLocated {
		private static final long serialVersionUID = 84458516150L;

		@Override
		public Integer getID() {
			return null;
		}

		@Override
		public int getx() {
			return x;
		}

		@Override
		public int gety() {
			return y;
		}
	}
}