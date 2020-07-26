package geometry.implementations.shapes.subHierarchy;

import java.awt.Point;
import java.awt.geom.Point2D;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;

public abstract class AbstractShapeImpl extends AbstractShapeRotated {
	private static final long serialVersionUID = -5641048035L;

	public AbstractShapeImpl(ShapeRunnersImplemented shapeImplementing) { this(shapeImplementing, 0.0, 0, 0); }

	public AbstractShapeImpl(AbstractShapeImpl s) {
		this(s.shapeImplementing, s.angleRotation, s.center.x, s.center.y);
	}

	public AbstractShapeImpl(ShapeRunnersImplemented shapeImplementing, double angleRotation, int xCenter,
			int yCenter) {
		super(shapeImplementing, angleRotation);
//		this.xCenter = xCenter;
//		this.yCenter = yCenter;
//		new Point(xCenter, yCenter)
		center = new Point();
		this.topLeftCorner = new PointTopLeftCorner();
	}

//	protected int xCenter,yCenter;
	protected final Point center;
	protected final Point2D topLeftCorner;

	//

	@Override
	public int getXCenter() { return center.x; }

	@Override
	public int getYCenter() { return center.y; }

	@Override
	public Point getLocation() { return center; }

	@Override
	public Point getCenter() { return getLocation(); }

	@Override
	public Point2D getTopLeftCorner() { return topLeftCorner; }

	/**
	 * Get the x-component of the of the {@link Point} returned by
	 * {@link #getTopLeftCorner()}.
	 */
	@Override
	public int getXTopLeft() {
		// return (int) getLeftTopCorner().getX();
		return getXCenter() - (getWidth() >> 1);
	}

	/**
	 * Get the y-component of the of the {@link Point} returned by
	 * {@link #getTopLeftCorner()}.
	 */
	@Override
	public int getYTopLeft() {
//		return (int) getLeftTopCorner().getY();
		return getYCenter() - (getHeight() >> 1);
	}

	//
	//

	@Override
	public AbstractShape2D setXCenter(int x) {
		if (this.center.x != x)
			this.polygonCache = null;
		super.setXCenter(x);
		this.center.x = x;
		return this;
	}

	@Override
	public AbstractShape2D setYCenter(int y) {
		if (this.center.y != y)
			this.polygonCache = null;
		super.setYCenter(y);
		this.center.y = y;
		return this;
	}

	//

	class PointTopLeftCorner extends Point2D {

		@Override
		public double getX() { return getXCenter() - (getWidth() >> 1); }

		@Override
		public double getY() { return getYCenter() - (getWidth() >> 1); }

		@Override
		public void setLocation(double x, double y) {
			center.x = ((int) x) + (getWidth() >> 1);
			center.y = ((int) y) + (getHeight() >> 1);
		}

	}
}