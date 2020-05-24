package geometry.implementations.shapes;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.AbstractShapeRotated;
import geometry.implementations.shapes.subHierarchy.ShapeFillableImpl;
import tools.MathUtilities;

public class ShapeRectangle extends ShapeFillableImpl {
	private static final long serialVersionUID = 716984256106843689L;

	public ShapeRectangle() { this(true); }

	public ShapeRectangle(boolean isFilled) {
		super((isFilled ? ShapeRunnersImplemented.Rectangle : ShapeRunnersImplemented.RectangleBorder));
		this.width = 0;
		this.height = 0;
		this.diameterCache = 0;
		this.isFilled = isFilled;
	}

	public ShapeRectangle(ShapeRectangle s) {
		super(s);
		this.width = s.width;
		this.height = s.height;
		this.diameterCache = s.diameterCache;
	}

	public ShapeRectangle(double angleRotation, int xCenter, int yCenter, boolean isFilled, int width, int height) {
		super((isFilled ? ShapeRunnersImplemented.Rectangle : ShapeRunnersImplemented.RectangleBorder), angleRotation,
				xCenter, yCenter, isFilled);
		this.width = width;
		this.height = height;
	}

	protected int width, height, diameterCache;
	protected Polygon polygonCache;

	//
	@Override
	public ShapeRunnersImplemented getShapeImplementing() {
		return isFilled ? ShapeRunnersImplemented.Rectangle : ShapeRunnersImplemented.RectangleBorder;
	}

	@Override
	public int getWidth() { return width; }

	@Override
	public int getHeight() { return height; }

	@Override
	public boolean isRegular() { return width == height; }

	@Override
	public int getDiameter() {
		if (isRegular())
			return width == 0 ? 0 : ((int) Math.round(width * MathUtilities.sqrtTwo));
		if (this.polygonCache == null)
			toPolygon(); // update cache
		return diameterCache;// (int) Math.round(Math.hypot(width, height));
	}

	@Override
	public int getRadius() {
		return getDiameter() >> 1;// (int) Math.round(Math.hypot(width, height) / 2.0);
	}

	@Override
	public final int getCornersAmounts() { return 4; }

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
	public AbstractShape2D setCornersAmounts(int cornersAmounts) { return this; }

	@Override
	public AbstractShape2D setRadius(int radius) { return setDiameter(radius << 1); }

	@Override
	public AbstractShape2D setDiameter(int diameter) {
		double ratio, radius, angleRad;
		if (diameter != this.diameterCache) {
			if (diameter == 0) {
				this.diameterCache = width = height = 0;
			} else {
				if (height == 0)
					this.diameterCache = height = 0;
				else if (width == 0)
					this.diameterCache = width = 0;
				else {
					this.diameterCache = diameter;
					if (width == height)
						width = height = (int) Math.round((diameter / 2.0) * MathUtilities.sqrtTwo);
					else {
						// ratio = h/w = tan(alpha) -> alpha ? arctan(h/w)
						// -> h = prevRadius * sin( arctan(ratio)), w = similar
						ratio = height / width;
						angleRad = Math.atan(ratio);
						radius = diameter / 2.0;
						height = (int) Math.round(radius * Math.sin(angleRad));
						width = (int) Math.round(radius * Math.cos(angleRad));
					}
				}
			}
			this.polygonCache = null;
		}
		return this;
	}

	//

	@Override
	public Polygon toPolygon() {
		int counter;
		double tempx, tempy, rad, halfWidth, halfHeight, radius, angRotation;
		double[] deltasx, deltasy;
		int[] xx, yy;
		Polygon p;
		if (polygonCache != null)
			return polygonCache;

		angRotation = this.getAngleRotation();
		{
			int minusw, plusw, minush, plush;
			minusw = (width - 1) >> 1;
			minush = (height - 1) >> 1;
			plusw = minusw + ((width & 0x1) == 1 ? 0 : 1);
			plush = minush + ((height & 0x1) == 1 ? 0 : 1);
			p = null;
			if (angRotation == 0.0 | angRotation == 180.0)
				p = polygonCache = new Polygon(//
						new int[] { minusw = xCenter - minusw, plusw += xCenter, plusw, minusw }, //
						new int[] { minush = yCenter - minush, minush, plush += yCenter, plush }//
						, 4);
			else if (angRotation == 90.0 | angRotation == 270.0)
				p = polygonCache = new Polygon(//
						new int[] { minush = xCenter - minush, plush += xCenter, plush, minush }, //
						new int[] { minusw = yCenter - minusw, minusw, plusw += yCenter, plusw }//
						, 4);
			if (p != null) {
				diameterCache = (int) Math.round(Math.hypot(width, height));
				return p;
			}
		}
		radius = Math.hypot(halfWidth = width, halfHeight = height);// used as temp
		diameterCache = (int) Math.round(radius);
		radius /= 2.0;
		halfHeight /= 2.0;
		halfWidth /= 2.0;
		// for all corners: they are 4 -> 2 booleans iterating
		counter = -1;
		// use xx and yy as deltas, then as they are designed for
		xx = new int[4];
		yy = new int[4];
		deltasx = new double[] { -halfWidth, halfWidth, halfWidth, -halfWidth };
		deltasy = new double[] { -halfHeight, -halfHeight, halfHeight, halfHeight };
		while (++counter < 4) {
			tempx = (xCenter + deltasx[counter]);
			tempy = (yCenter + deltasy[counter]);
			rad = MathUtilities.angleDegrees(xCenter, yCenter, tempx, tempy) + angRotation;//
			if (rad < 0.0)
				rad += 360.0;
			else if (rad > 360.0)
				rad -= 360.0;
			rad = Math.toRadians(rad);
//			System.out.println("xCenter: " + xCenter + ", yCenter: " + yCenter + "\n\t tempx: " + tempx + ", tempy: "
//					+ tempy + ",\n\t ang between" + MathUtilities.angleDegrees(xCenter, yCenter, tempx, tempy));
			xx[counter] = (int) Math.round(//
					xCenter + radius * Math.cos(rad));
			yy[counter] = (int) Math.round(//
					yCenter + radius * Math.sin(rad));
		}
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
	public AbstractShapeRotated setAngleRotation(double angleRotation) {
		super.setAngleRotation(angleRotation);
		this.polygonCache = null;
		return this;
	}

	@Override
	public AbstractShape2D clone() { return new ShapeRectangle(this); }

	//

	//

	@Deprecated
	public Polygon toPolygon_BROKEN() {
		boolean addingx, addingy;
		int counter;
		double tempx, tempy, rad, halfWidth, halfHeight, radius, angRotation;
		int[] xx, yy;
		Polygon p;

		if (polygonCache != null)
			return polygonCache;

		angRotation = this.getAngleRotation();
		{
			int minusw, plusw, minush, plush;
			minusw = width >> 1;
			minush = height >> 1;
			plusw = minusw + (width & 1);
			plush = minush + (height & 1);
			p = null;
			if (angRotation == 0.0 | angRotation == 180.0)
				p = polygonCache = new Polygon(//
						new int[] { minusw = xCenter - minusw, plusw += xCenter, plusw, minusw }, //
						new int[] { minush = yCenter - minush, minush, plush += yCenter, plush }//
						, 4);
			else if (angRotation == 90.0 | angRotation == 270.0)
				p = polygonCache = new Polygon(//
						new int[] { minush = xCenter - minush, plush += xCenter, plush, minush }, //
						new int[] { minusw = yCenter - minusw, minusw, plusw += yCenter, plusw }//
						, 4);
			if (p != null) {
				diameterCache = (int) Math.round(Math.hypot(width, height));
				return p;
			}
		}
		radius = Math.hypot(halfWidth = width, halfHeight = height);// used as temp
		diameterCache = (int) Math.round(radius);
		radius /= 2.0;
		halfHeight /= 2.0;
		halfWidth /= 2.0;
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

				rad = MathUtilities.angleDegrees(xCenter, yCenter, tempx, tempy) + angRotation;//
				if (rad < 0.0)
					rad += 360.0;
				else if (rad > 360.0)
					rad -= 360.0;
				rad = Math.toRadians(rad);
				System.out.println(
						"xCenter: " + xCenter + ", yCenter: " + yCenter + "\n\t tempx: " + tempx + ", tempy: " + tempy
								+ ",\n\t ang between" + MathUtilities.angleDegrees(xCenter, yCenter, tempx, tempy));
				xx[counter] = (int) Math.round(//
						xCenter + radius * Math.cos(rad));
				yy[counter++] = (int) Math.round(//
						yCenter + radius * Math.sin(rad));
			} while (addingx = !addingx);
		} while (addingy = !addingy);
		return polygonCache = new Polygon(xx, yy, 4);
	}
}