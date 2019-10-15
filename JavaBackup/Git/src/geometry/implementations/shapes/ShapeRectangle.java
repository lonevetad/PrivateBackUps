package geometry.implementations.shapes;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.AbstractShapeFillableImpl;
import tools.MathUtilities;

public class ShapeRectangle extends AbstractShapeFillableImpl {
	private static final long serialVersionUID = 716984256106843689L;

	public ShapeRectangle() {
		this(true);
	}

	public ShapeRectangle(boolean isFilled) {
		super((isFilled ? ShapeRunnersImplemented.Rectangle : ShapeRunnersImplemented.RectangleBorder));
		this.width = 0;
		this.height = 0;
	}

	public ShapeRectangle(ShapeRectangle s) {
		super(s);
		this.width = s.width;
		this.height = s.height;
	}

	public ShapeRectangle(double angleRotation, int xCenter, int yCenter, boolean isFilled, int width, int height) {
		super((isFilled ? ShapeRunnersImplemented.Rectangle : ShapeRunnersImplemented.RectangleBorder), angleRotation,
				xCenter, yCenter, isFilled);
		this.width = width;
		this.height = height;
	}

	protected int width, height;
	protected Polygon polygonCache;

	//
	@Override
	public ShapeRunnersImplemented getShapeImplementing() {
		return isFilled ? ShapeRunnersImplemented.Rectangle : ShapeRunnersImplemented.RectangleBorder;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean isRegular() {
		return width == height;
	}

	@Override
	public int getDiameter() {
		return (int) Math.round(Math.hypot(width, height));
	}

	@Override
	public int getRadius() {
		return (int) Math.round(Math.hypot(width, height) / 2.0);
	}

	@Override
	public int getCornersAmounts() {
		return 4;
	}

	//

	public ShapeRectangle setWidth(int width) {
		if (width >= 0) {
			if (this.width != width)
				this.polygonCache = null;
			this.width = width;
		}
		return this;
	}

	public ShapeRectangle setHeight(int height) {
		if (height >= 0) {
			if (this.height != height)
				this.polygonCache = null;
			this.height = height;
		}
		return this;
	}

	@Override
	public AbstractShape2D setCornersAmounts(int cornersAmounts) {
		return this;
	}

	@Override
	public AbstractShape2D setRadius(int radius) {
		return this;
	}

	@Override
	public AbstractShape2D setDiameter(int radius) {
		return this;
	}

	//

	@Override
	public Polygon toPolygon() {
		boolean addingx, addingy;
		int counter;
		double tempx, tempy, rad, halfWidth, halfHeight, radius, angRotation;
		int[] xx, yy;

		if (polygonCache != null)
			return polygonCache;

		angRotation = this.getAngleRotation();
		{
			int minusw, plusw, minush, plush;
			minusw = width >> 1;
			minush = height >> 1;
			plusw = minusw + (width & 1);
			plush = minush + (height & 1);
			if (angRotation == 0.0 | angRotation == 180.0)
				return polygonCache = new Polygon(//
						new int[] { minusw = xCenter - minusw, plusw += xCenter, plusw, minusw }, //
						new int[] { minush = yCenter - minush, minush, plush += yCenter, plush }//
						, 4);
			else if (angRotation == 90.0 | angRotation == 270.0)
				return polygonCache = new Polygon(//
						new int[] { minush = xCenter - minush, plush += xCenter, plush, minush }, //
						new int[] { minusw = yCenter - minusw, minusw, plusw += yCenter, plusw }//
						, 4);
		}
		radius = Math.hypot(halfWidth = width / 2.0, halfHeight = height / 2.0);

		// for all corners: they are 4 -> 2 booleans iterating
		counter = 0;
		xx = new int[4];
		yy = new int[4];
		addingy = false;
		do {
			addingx = false;
			tempy = addingy ? (yCenter + halfHeight) : (yCenter - halfHeight);
			do {
				tempx = addingx ? (xCenter + halfWidth) : (xCenter - halfWidth);

				rad = Math.toRadians(//
						MathUtilities.angleDeg(xCenter, yCenter, tempx, tempy) //
								+ angRotation);
				xx[counter] = (int) Math.round(//
						xCenter + radius * Math.cos(rad));
				yy[counter++] = (int) Math.round(//
						yCenter + radius * Math.sin(rad));
			} while (addingx = !addingx);
		} while (addingy = !addingy);
		return polygonCache = new Polygon(xx, yy, 4);
	}

	/**
	 * NOTE: again, no cache is performed, as like as for
	 * {@link #getLeftTopCorner()}.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getBoundingBox() {
		Point2D ltc;
		ltc = getLeftTopCorner();
		if (ltc == null)
			return null;
		return new Rectangle((int) ltc.getX(), (int) ltc.getY(), width, height);
	}

	@Override
	public AbstractShape2D clone() {
		return new ShapeRectangle(this);
	}
}