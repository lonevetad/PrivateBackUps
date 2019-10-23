package geometry.implementations.shapes;

import java.awt.Polygon;
import java.awt.geom.Line2D;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.AbstractShapeImpl;
import tools.MathUtilities;

public class ShapeLine extends AbstractShapeImpl {
	private static final long serialVersionUID = -1545584051L;

	public ShapeLine() {
		super(ShapeRunnersImplemented.Line);
	}

	public ShapeLine(ShapeLine s) {
		super(s);
		this.length = s.length;
	}

	public ShapeLine(double angleRotation, int x, int y, int length) {
		super(ShapeRunnersImplemented.Line, angleRotation, x, y);
		this.length = 0;
		this.setLength(length);
	}

	int length;

	//

	public int getLength() {
		return length;
	}

	@Override
	public boolean isRegular() {
		return true;
	}

	@Override
	public int getDiameter() {
		return length;
	}

	@Override
	public int getRadius() {
		return length >> 1;
	}

	@Override
	public int getCornersAmounts() {
		return 2;
	}

	//

	@Override
	public AbstractShape2D setDiameter(int diameter) {
		if (diameter >= 0) {
			if (this.length != diameter)
				this.polygonCache = null;
			this.length = diameter;
		}
		return this;
	}

	public void setLength(int length) {
		setDiameter(length);
	}

	@Override
	public AbstractShape2D setCornersAmounts(int cornersAmounts) {
		return this;
	}

	@Override
	public boolean contains(int x, int y) {
		Polygon p;
		p = toPolygon();
		if (p == null)
			return false;
		return MathUtilities.areCollinear(p.xpoints[0], p.ypoints[0], x, y, p.xpoints[1], p.ypoints[1]);
	}

	//

	@Override
	public Polygon toPolygon() {
		double dx, dy, rad, halfLength;
		int[] xx, yy;
		if (polygonCache != null)
			return polygonCache;
		if (length == 0)
			return polygonCache = new Polygon(new int[] { xCenter, xCenter }, new int[] { yCenter, yCenter }, 1);
		halfLength = length / 2.0;
		rad = this.getAngleRotation();
		if (rad == 0.0 || rad == 180.0) {
			xx = new int[] { (int) (xCenter - halfLength), (int) (xCenter + halfLength) };
			yy = new int[] { (yCenter), (yCenter) };
		} else if (rad == 90.0 || rad == 270.0) {
			xx = new int[] { (xCenter), (xCenter) };
			yy = new int[] { (int) (yCenter - halfLength), (int) (yCenter + halfLength) };
		} else {
//			rad = Math.toRadians(rad / 2.0);
			rad = Math.toRadians(rad);
			dx = Math.cos(rad) * halfLength;
			dy = Math.sin(rad) * halfLength;
			halfLength = length / 4.0;
//			xx = new int[] { (int) (xCenter - dx), (int) (xCenter + dx) };
//			yy = new int[] { (int) (yCenter - dy), (int) (yCenter + dy) };
			xx = new int[] { (int) (xCenter - dx), (int) (xCenter + dx) };
			yy = new int[] { (int) (yCenter - dy), (int) (yCenter + dy) };
//			return polygonCache = super.toPolygon();
		}
		return polygonCache = new Polygon(xx, yy, 2);
	}

	@Override
	public AbstractShape2D clone() {
		return new ShapeLine(this);
	}

	public Line2D toLine() {
		Polygon p;
		p = this.toPolygon();
		if (p == null)
			return null;
		return new Line2D.Double(p.xpoints[0], p.ypoints[0], p.xpoints[1], p.ypoints[1]);
	}
}