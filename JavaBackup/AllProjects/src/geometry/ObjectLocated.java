package geometry;

import java.awt.Point;
import java.util.Comparator;
import java.util.function.Function;

import tools.Comparators;
import tools.ObjectWithID;

/**
 * Object that is located into some <i>space</i> concept and this is described
 * by {@link ObjectLocated#getLocation()}: as this method's documentation
 * explains, that point is the center.
 */
public interface ObjectLocated extends ObjectWithID {
	public static final Function<ObjectLocated, Integer> KEY_EXTRACTOR = o -> o.getID();
	public static final Comparator<ObjectLocated> COMPARATOR = (ol1, ol2) -> {
		int c;
		Integer id1, id2;
		if (ol1 == ol2)
			return 0;
		if (ol1 == null)
			return -1;
		if (ol2 == null)
			return 1;
		id1 = ol1.getID(); // from KEY_EXTRACTOR
		id2 = ol2.getID(); // from KEY_EXTRACTOR
		c = Comparators.INTEGER_COMPARATOR.compare(id1, id2);
		if (c != 0)
			return c;
		return Comparators.POINT_2D_COMPARATOR_LOWEST_LEFTMOST_FIRST.compare(ol1.getLocation(), ol2.getLocation());
	};

	/**
	 * As described by {@link #getLocation()}, it's the center's <code>x</code>
	 * coordinate value.
	 */
	public default int getx() { return getLocation().x; }

	/**
	 * As described by {@link #getLocation()}, it's the center's <code>y</code>
	 * coordinate value.
	 */
	public default int gety() { return getLocation().y; }

	/**
	 * The location is referred to be the center of this object.
	 * <p>
	 * {@inheritDoc}
	 */
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
		public Integer getID() { return null; }

		@Override
		public int getx() { return x; }

		@Override
		public int gety() { return y; }
	}
}