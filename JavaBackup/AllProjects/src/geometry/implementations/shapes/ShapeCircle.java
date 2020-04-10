package geometry.implementations.shapes;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.ShapeFillableImpl;

public class ShapeCircle extends ShapeFillableImpl {
	private static final long serialVersionUID = -53268436103028L;
	public static final int DIAMETER_DIVIDER_LOG_FACTOR = 3, MIN_AMOUNT_SIDES_POLYGON_APPROXIMATOR = 8,
			MAX_AMOUNT_SIDES_POLYGON_APPROXIMATOR = 360;

	public ShapeCircle(boolean isFilled) {
		super((isFilled ? ShapeRunnersImplemented.Disk : ShapeRunnersImplemented.Circumference));
		super.isFilled = isFilled;
	}

	public ShapeCircle(ShapeCircle s) {
		super(s);
//		this.radius = s.radius;
		this.diameter = s.diameter;
	}

	public ShapeCircle(int x, int y, boolean isFilled, int diameter) {
		super((isFilled ? ShapeRunnersImplemented.Disk : ShapeRunnersImplemented.Circumference), 0.0, x, y, isFilled);
		setDiameter(diameter);
	}

	protected int // radius, /** Used as cache */
	diameter;

	//

	/**
	 * Get the radius. A circle with zero radius is just its center, occupying that
	 * point. The diameter will always be odd.
	 */
//	@Override
//	public int getRadius() {
//		return radius;
//	}
	@Override
	public ShapeRunnersImplemented getShapeImplementing() {
		return isFilled ? ShapeRunnersImplemented.Disk : ShapeRunnersImplemented.Circumference;
	}

	/** {@inheritDoc} */
	@Override
	public int getDiameter() {
		return diameter;
	}

	@Override
	public int getWidth() {
		return diameter;
	}

	@Override
	public int getHeight() {
		return diameter;
	}

	@Override
	public final boolean isRegular() {
		return true;
	}

	@Override
	public final int getCornersAmounts() {
		// each side must be ong at least 20 pixels
//		return Math.min(MAX_AMOUNT_SIDES_POLYGON_APPROXIMATOR, //
//				Math.max(MIN_AMOUNT_SIDES_POLYGON_APPROXIMATOR, radius >> DIAMETER_DIVIDER_LOG_FACTOR));
		int n; // equivalent to comment above, but faster (no functions calls)
		return (((n = diameter >> DIAMETER_DIVIDER_LOG_FACTOR) < MIN_AMOUNT_SIDES_POLYGON_APPROXIMATOR)//
				? MIN_AMOUNT_SIDES_POLYGON_APPROXIMATOR
				: (n > MAX_AMOUNT_SIDES_POLYGON_APPROXIMATOR ? MAX_AMOUNT_SIDES_POLYGON_APPROXIMATOR : n));

	}

	@Override
	public Point getLeftTopCorner() {
		int radius;
		radius = diameter >> 1;
		return new Point(xCenter - radius, yCenter - radius);
	}

	//

	/** See {@link #getDiameter()}. */
	@Override
	public AbstractShape2D setDiameter(int diameter) {
		if (diameter >= 0)
			this.diameter = diameter;
		return this;
	}

	@Override
	public AbstractShape2D setCornersAmounts(int cornersAmounts) {
		return this;
	}

//

	@Override
	public boolean contains(int x, int y) {
		return (this.xCenter == x && this.yCenter == y) //
				|| (diameter / 2.0) <= Math.hypot(this.xCenter - x, this.yCenter - y);
	}

	@Override
	public Point2D[] getBoundingBoxCorners() {
		int radius, d, d_1, xp, xm, y;
		radius = (d = diameter) >> 1;
		d_1 = d + (d & 0x1);
		return new Point2D[] { //
				new Point2D.Double(xm = xCenter - radius, y = yCenter - radius), //
				new Point2D.Double(xp = xCenter + d_1, y), //
				new Point2D.Double(xm, y = yCenter + d_1), //
				new Point2D.Double(xp, y) };
	}

	@Override
	public Rectangle getBoundingBox() {
		int radius, d;
		radius = (d = diameter) >> 1;
		return new Rectangle(xCenter - radius, yCenter - radius, d, d);
	}

//	@Override
//	public Polygon toPolygon() {
//		if (polygonCache != null)
//			return polygonCache;
//	return polygonCache = new Polygon(
//			new int[] { xCenter - radius, xCenter + radius, xCenter - radius, xCenter + radius },
//			new int[] { yCenter - radius, yCenter - radius, yCenter + radius, yCenter + radius }, 4);
//	}

	@Override
	public AbstractShape2D clone() {
		return new ShapeCircle(this);
	}
}