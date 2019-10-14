package geometry.implementations.shapes;

import tools.MathUtilities;
import videogamesOldVersion.common.abstractCommon.shapedObject.AbstractObjectOnCartesianPlan;
import videogamesOldVersion.common.abstractCommon.shapedObject.AbstractObjectRectangleBoxed;
import videogamesOldVersion.common.mainTools.mOLM.MatrixObjectLocationManager;

/**
 * All variables, expecially the <code>int</code> ones, MUST be considered as
 * expressed in {@link MatrixObjectLocationManager}'s pixels/cell.
 * <p>
 * This class seems to implements the Abstract Factory Pattern
 */
/*
 * This class uses {@link ValuesModifiedHistoryKeeper} as message to notify its
 * observers.
 */
public abstract class ShapeSpecification//
		// extends Observable //
		implements AbstractObjectOnCartesianPlan {

	private static final long serialVersionUID = -65100784903L;

	public ShapeSpecification() {
		this(ShapesImplemented.Point, -2, -2);
	}

	/** BEWARE: the point (x,y) is the bottom-left corner !! */
	public ShapeSpecification(ShapesImplemented si, int xLeftBottom, int yLeftBottom) {
		this.xLeftBottom = xLeftBottom;
		this.yLeftBottom = yLeftBottom;
		this.shape = si;
		this.shapeRunner = null;
	}

	public ShapeSpecification(ShapeSpecification ss) {
		xLeftBottom = ss.xLeftBottom;
		yLeftBottom = ss.yLeftBottom;
		this.shape = ss.shape;
		this.shapeRunner = ss.shapeRunner;
	}

	public int xLeftBottom, yLeftBottom;
	ShapesImplemented shape;
	ShapeRunner shapeRunner; // used as cache

	//

	// TODO GETTER

	@Override
	public int getXLeftBottom() {
		return xLeftBottom;
	}

	@Override
	public int getYLeftBottom() {
		return yLeftBottom;
	}

	public ShapesImplemented getShape() {
		return shape;
	}

	public ShapeRunner getShapeRunner() {
		return shapeRunner;
	}

	@Override
	public abstract int getXCenter();

	@Override
	public abstract int getYCenter();

	//

	// TODO SETTER

	@Override
	public ShapeSpecification setXLeftBottom(int x) {
		this.xLeftBottom = x;
		return this;
	}

	@Override
	public ShapeSpecification setYLeftBottom(int y) {
		this.yLeftBottom = y;
		return this;
	}

	@Override
	public ShapeSpecification setLeftBottomCorner(int x, int y) {
		this.yLeftBottom = y;
		this.xLeftBottom = x;
		return this;
	}

	public ShapeSpecification setShapeRunner(ShapeRunner shapeRunner) {
		this.shapeRunner = shapeRunner;
		return this;
	}

	@Override
	public abstract ShapeSpecification setXCenter(int x);

	@Override
	public abstract ShapeSpecification setYCenter(int y);

	@Override
	public abstract ShapeSpecification setCenter(int x, int y);

	//

	@Override
	public abstract ShapeSpecification clone();

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder(128);
		// sb.append("ShapeSpecification=[");
		sb.append(", ShapesImplemented: ");
		sb.append(getShape().name());
		sb.append(", xLeftBottom: ");
		sb.append(xLeftBottom);
		sb.append(", yLeftBottom: ");
		sb.append(yLeftBottom);
		sb.append(", xCenter: ");
		sb.append(getXCenter());
		sb.append(", yCenter: ");
		sb.append(getYCenter());
		// sb.append(']');
		return sb.toString();
	}

	//

	// TODO STATIC

	public static SS_Point newPoint(int xx, int yy) {
		return new SS_Point(xx, yy);
	}

	/** BEWARE: the point (x,y) is the shape's center !! */
	public static SS_Line newLine(int xx, int yy, int length, double angleDeg) {
		return new SS_Line(xx - (length >> 1), yy, length, angleDeg);
	}

	/** BEWARE: the point (x,y) is the shape's center !! */
	public static SS_Rectangular newRectangle(boolean isFillingRectangle, int width, int height) {
		return newRectangle(isFillingRectangle, width >> 1, height >> 1, width, height, 0);
	}

	/** BEWARE: the point (x,y) is the shape's center !! */
	public static SS_Rectangular newRectangle(boolean isFillingRectangle, int xx, int yy, int width, int height,
			double angleDeg) {
		if (width < 1)
			throw new IllegalArgumentException("Incorrect width: " + width);
		if (height < 1)
			throw new IllegalArgumentException("Incorrect height: " + height);
		return new SS_Rectangular(isFillingRectangle, xx - (width >> 1), yy - (height >> 1), width, height, angleDeg);
	}

	/** BEWARE: the point (x,y) is the shape's center !! */
	public static SS_Circular newCircle(boolean isFillingCircle, int ray) {
		return newCircle(isFillingCircle, ray, ray, ray);
	}

	/** BEWARE: the point (x,y) is the shape's center !! */
	public static SS_Circular newCircle(boolean isFillingCircle, int xx, int yy, int ray) {
		if (ray < 1)
			throw new IllegalArgumentException("Incorrect ray: " + ray);
		return new SS_Circular(isFillingCircle, xx - ray, yy - ray, ray);
	}

	/** BEWARE: the point (x,y) is the shape's center !! */
	public static SS_Triangle newTriangle(int xx, int yy, int borderLength, double angleDeg) {
		if (borderLength < 1)
			throw new IllegalArgumentException("Incorrect borderLength: " + borderLength);
		return new SS_Triangle(xx - (borderLength >> 1), yy, borderLength - (borderLength >> 1), angleDeg);
	}

	/** BEWARE: the point (x,y) is the shape's center !! */
	public static SS_Cone newCone(int xx, int yy, int numberOfSteps, int baseHeight, int widthIncrement,
			int heightIncrement, double angleDeg) {
		SS_Cone ssc;
		if (numberOfSteps < 1)
			throw new IllegalArgumentException("Incorrect numberOfSteps: " + numberOfSteps);
		if (baseHeight < 0)
			throw new IllegalArgumentException("Incorrect baseHeight: " + baseHeight);
		if (widthIncrement < 1)
			throw new IllegalArgumentException("Incorrect widthIncrement: " + widthIncrement);
		if (heightIncrement < 1)
			throw new IllegalArgumentException("Incorrect heightIncrement: " + heightIncrement);
		ssc = new SS_Cone(xx, yy, numberOfSteps, baseHeight, widthIncrement, heightIncrement, angleDeg);
		// ssc.setXLeftBottom(xx - (ssc.totalWidth >> 1));
		// ssc.setYLeftBottom(yy - (ssc.totalHeight >> 1));
		ssc.setLeftBottomCorner(xx - (ssc.totalWidth >> 1), yy - (ssc.totalHeight >> 1));
		return ssc;
	}

	/** BEWARE: the point (x,y) is the shape's center !! */
	public static SS_Arrow newArrowBorderBodySameLength(int xx, int yy, int borderLength, double angleDeg) {
		if (borderLength < 1)
			throw new IllegalArgumentException("Incorrect borderLength: " + borderLength);
		return new SS_Arrow(xx, yy, borderLength, angleDeg);
	}

	/** BEWARE: the point (x,y) is the shape's center !! */
	public static SS_Arrow newArrow(int xx, int yy, int borderLength, double angleDegBorder, int bodyLength,
			double angleDeg) {
		if (borderLength < 1)
			throw new IllegalArgumentException("Incorrect borderLength: " + borderLength);
		if (bodyLength < 1)
			throw new IllegalArgumentException("Incorrect bodyLength: " + bodyLength);
		return new SS_Arrow(xx, yy, borderLength, angleDegBorder, bodyLength, angleDeg);
	}

	public static SS_Ellipse newEllipseNoRotation(boolean isFillingEllipse, int xx, int yy, int width, int height) {
		if (width < 1)
			throw new IllegalArgumentException("Incorrect width: " + width);
		if (height < 1)
			throw new IllegalArgumentException("Incorrect height: " + height);
		return new SS_Ellipse(isFillingEllipse, xx - (width >> 1), yy - (height >> 1), width, height);
	}

	// TODO EXTENDIONS

	public static class SS_Point extends ShapeSpecification {

		private static final long serialVersionUID = -65100784904L;

		public SS_Point(SS_Point gss) {
			super(gss);
		}

		public SS_Point(int xx, int yy) {
			super(ShapesImplemented.Point, xx, yy);
		}

		@Override
		public SS_Point clone() {
			return new SS_Point(this);
		}

		@Override
		public int getXCenter() {
			return getXLeftBottom();
		}

		@Override
		public int getYCenter() {
			return getYLeftBottom();
		}

		@Override
		public SS_Point setXCenter(int x) {
			super.setXLeftBottom(x);
			return this;
		}

		@Override
		public SS_Point setYCenter(int y) {
			super.setYLeftBottom(y);
			return this;
		}

		@Override
		public SS_Point setLeftBottomCorner(int x, int y) {
			if (x != getXLeftBottom())
				setXLeftBottom(x);
			if (y != getYLeftBottom())
				setYLeftBottom(y);
			return this;
		}

		@Override
		public SS_Point setCenter(int x, int y) {
			if (x != getXCenter())
				setXCenter(x);
			if (y != getYCenter())
				setYCenter(y);
			return this;
		}
	}

	/*
	 * Ereditarieta' che ha poco senso (dovrebbe essere l'inverso !) ma per
	 * questioni di ClassCast e' necessario
	 */
	public static class SS_Line extends ShapeSpecification {

		private static final long serialVersionUID = -65100784904L;

		public SS_Line(SS_Line gss) {
			super(gss);
			this.length = gss.length;
			this.angleDeg = gss.angleDeg;
		}

		public SS_Line(int xx, int yy, int length, double angleDeg) {
			super(ShapesImplemented.Line, xx, yy);
			this.length = length;
			this.angleDeg = angleDeg;
		}

		int length;
		double angleDeg;

		public int getLength() {
			return length;
		}

		public double getAngleDeg() {
			return angleDeg;
		}

		@Override
		public int getXCenter() {
			return getXLeftBottom() + (length >> 1);
		}

		@Override
		public int getYCenter() {
			return getYLeftBottom();
		}

		public SS_Line setLength(int length) {
			if (length >= 0)
				this.length = length;
			return this;
		}

		public SS_Line setAngleDeg(double angleDeg) {
			this.angleDeg = MathUtilities.adjustDegAngle(angleDeg);
			return this;
		}

		@Override
		public SS_Line setXCenter(int x) {
			setXLeftBottom(x - (getLength() >> 1));
			return this;
		}

		@Override
		public SS_Line setYCenter(int y) {
			super.setYLeftBottom(y);
			return this;
		}

		@Override
		public SS_Line setLeftBottomCorner(int x, int y) {
			setXLeftBottom(x);
			setYLeftBottom(y);
			return this;
		}

		@Override
		public SS_Line setCenter(int x, int y) {
			setXCenter(x);
			setYCenter(y);
			return this;
		}

		@Override
		public SS_Line clone() {
			return new SS_Line(this);
		}

	}

	protected static abstract class ShapeWithCenter extends ShapeSpecification {
		private static final long serialVersionUID = 64130640245L;

		public ShapeWithCenter(ShapesImplemented si, int xLeftBottom, int yLeftBottom) {
			super(si, xLeftBottom, yLeftBottom);
		}

		public ShapeWithCenter(ShapeWithCenter ss) {
			super(ss);
			this.xCenter = ss.xCenter;
			this.yCenter = ss.yCenter;
		}

		int // used as cache
		xCenter, yCenter;

		@Override
		public int getXCenter() {
			return xCenter;
		}

		@Override
		public int getYCenter() {
			return yCenter;
		}

		@Override
		public ShapeWithCenter setXCenter(int xCenter) {
			this.xCenter = xCenter;
			return this;
		}

		@Override
		public ShapeWithCenter setYCenter(int yCenter) {
			this.yCenter = yCenter;
			return this;
		}

	}

	protected static abstract class ShapeCenteredRotated extends ShapeWithCenter {
		private static final long serialVersionUID = 64130640247L;

		public ShapeCenteredRotated(ShapesImplemented si, int xLeftBottom, int yLeftBottom) {
			this(si, xLeftBottom, yLeftBottom, 0.0);
		}

		public ShapeCenteredRotated(ShapesImplemented si, int xLeftBottom, int yLeftBottom, double angleDeg) {
			super(si, xLeftBottom, yLeftBottom);
			this.angleDeg = angleDeg;
		}

		public ShapeCenteredRotated(ShapeCenteredRotated ss) {
			super(ss);
			this.angleDeg = ss.angleDeg;
		}

		double angleDeg;

		public double getAngleDeg() {
			return angleDeg;
		}

		public ShapeCenteredRotated setAngleDeg(double angleDeg) {
			this.angleDeg = MathUtilities.adjustDegAngle(angleDeg);
			return this;
		}
	}

	protected static abstract class ShapeCenteredRectangleBoxed extends ShapeCenteredRotated
			implements AbstractObjectRectangleBoxed {
		private static final long serialVersionUID = 64130640247L;

		public ShapeCenteredRectangleBoxed(ShapesImplemented si, int xLeftBottom, int yLeftBottom) {
			this(si, xLeftBottom, yLeftBottom, 0.0);
		}

		public ShapeCenteredRectangleBoxed(ShapesImplemented si, int xLeftBottom, int yLeftBottom, double angleDeg) {
			super(si, xLeftBottom, yLeftBottom);
		}

		public ShapeCenteredRectangleBoxed(ShapeCenteredRectangleBoxed ss) {
			super(ss);
		}
	}

	// TODO RECTANGLE
	public static class SS_Rectangular extends ShapeCenteredRectangleBoxed {
		private static final long serialVersionUID = -65100784905L;

		public SS_Rectangular(boolean isFillingRectangle, int xLeftBottom, int yLeftBottom, int width, int height,
				double angleDeg) {
			super(isFillingRectangle ? ShapesImplemented.Rectangle : ShapesImplemented.Rectangle_Border, xLeftBottom,
					yLeftBottom, angleDeg);
			this.setWidth(width);
			this.setHeight(height);
		}

		public SS_Rectangular(SS_Rectangular gss) {
			super(gss);
			width = gss.width;
			height = gss.height;
		}

		int width, height;

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		//

		@Override
		public SS_Rectangular setXLeftBottom(int x) {
			this.xLeftBottom = x;
			xCenter = x + (width >> 1);
			return this;
		}

		@Override
		public SS_Rectangular setYLeftBottom(int y) {
			this.yLeftBottom = y;
			yCenter = y + (height >> 1);
			return this;
		}

		@Override
		public SS_Rectangular setWidth(int width) {
			if (width > 0) {
				this.width = width;
				xCenter = getXLeftBottom() + (width >> 1);
			}
			return this;
		}

		@Override
		public SS_Rectangular setLeftBottomCorner(int x, int y) {
			this.xLeftBottom = x;
			this.yLeftBottom = y;
			xCenter = x + (width >> 1);
			yCenter = y + (height >> 1);
			return this;
		}

		@Override
		public SS_Rectangular setCenter(int x, int y) {
			xCenter = x;
			yCenter = y;
			this.xLeftBottom = x - (width >> 1);
			this.yLeftBottom = y - (height >> 1);
			return this;
		}

		@Override
		public SS_Rectangular setHeight(int height) {
			if (height > 0) {
				this.height = height;
				yCenter = getYLeftBottom() + (height >> 1);
			}
			return this;
		}

		@Override
		public SS_Rectangular setXCenter(int x) {
			if (xCenter != x) {
				xCenter = x;
				xLeftBottom = x - (width >> 1);
			}
			return this;
		}

		@Override
		public SS_Rectangular setYCenter(int y) {
			if (yCenter != y) {
				yCenter = y;
				yLeftBottom = y - (height >> 1);
			}
			return this;
		}

		@Override
		public SS_Rectangular clone() {
			return new SS_Rectangular(this);
		}

		@Override
		public String toString() {
			return "SS_Rectangular [width=" + width + ", height=" + height + ", angleDeg=" + angleDeg + super.toString()
					+ "]";
		}
	}

	// TODO SS_Circular
	public static class SS_Circular extends ShapeWithCenter implements AbstractObjectRectangleBoxed {
		private static final long serialVersionUID = -65100784906L;

		public SS_Circular(boolean isFillingCircle, int xx, int yy, int ray) {
			super(isFillingCircle ? ShapesImplemented.Circle : ShapesImplemented.Circumference, xx, yy);
			this.setRay(ray);
		}

		public SS_Circular(SS_Circular gss) {
			super(gss);
			ray = gss.ray;
		}

		int ray, diameter;

		public int getRay() {
			return ray;
		}

		public int getDiameter() {
			return diameter;
		}

		//

		@Override
		public SS_Circular setXLeftBottom(int x) {
			this.xLeftBottom = x;
			xCenter = x + (ray);
			return this;
		}

		@Override
		public SS_Circular setYLeftBottom(int y) {
			this.yLeftBottom = y;
			yCenter = y + (ray);
			return this;
		}

		@Override
		public SS_Circular setLeftBottomCorner(int x, int y) {
			this.xLeftBottom = x;
			this.yLeftBottom = y;
			xCenter = x + ray;
			yCenter = y + ray;
			return this;
		}

		@Override
		public SS_Circular setCenter(int x, int y) {
			xCenter = x;
			yCenter = y;
			this.xLeftBottom = x - ray;
			this.yLeftBottom = y - ray;
			return this;
		}

		public SS_Circular setRay(int ray) {
			if (ray > 0) {
				this.ray = ray;
				diameter = ray << 1;
				xCenter = xLeftBottom + ray;
				yCenter = yLeftBottom + ray;
			}
			return this;
		}

		@Override
		public SS_Circular setXCenter(int x) {
			this.xCenter = x;
			xLeftBottom = x - (ray);
			return this;
		}

		@Override
		public SS_Circular setYCenter(int y) {
			this.yCenter = y;
			yLeftBottom = y - (ray);
			return this;
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
		public SS_Circular setWidth(int width) {
			setRay(width >> 1);
			return this;
		}

		@Override
		public SS_Circular setHeight(int height) {
			setRay(height >> 1);
			return this;
		}

		@Override
		public double getAngleDeg() {
			return 0;
		}

		@Override
		public AbstractObjectRectangleBoxed setAngleDeg(double angleDeg) {
			// i'm a circle, rotations don't change me LOL
			return this;
		}

		@Override
		public String toString() {
			return "SS_Circular [ray=" + ray + super.toString() + "]";
		}

		@Override
		public SS_Circular clone() {
			return new SS_Circular(this);
		}
	}

	// TODO SS_Triangle
	/**
	 * The coordinates P(x,y) passed as parameter must NOT be considered as the
	 * center of the triangle, BUT the left-bottom corner as usual.<br>
	 * All triangle's borders has the same length.
	 */
	public static class SS_Triangle extends ShapeCenteredRotated {
		private static final long serialVersionUID = -65100784907L;

		public SS_Triangle(int xx, int yy, int borderLength, double angleDeg) {
			super(ShapesImplemented.Triangle, xx, yy);
			this.borderLength = borderLength;
			this.angleDeg = angleDeg;
		}

		public SS_Triangle(SS_Triangle gss) {
			super(gss);
			borderLength = gss.borderLength;
			angleDeg = gss.angleDeg;
		}

		int borderLength;
		double angleDeg;

		public int getBorderLength() {
			return borderLength;
		}

		@Override
		public double getAngleDeg() {
			return angleDeg;
		}

		//

		@Override
		public SS_Triangle setAngleDeg(double angleDeg) {
			this.angleDeg = angleDeg;
			return this;
		}

		public SS_Triangle setBorderLength(int borderLength) {
			if (borderLength > 0) {
				int hb = borderLength >> 1;
				this.borderLength = borderLength;
				xCenter = xLeftBottom + hb;
				yCenter = yLeftBottom + hb;
			}
			return this;
		}

		@Override
		public SS_Triangle setXCenter(int x) {
			this.xCenter = x;
			xLeftBottom = x - (borderLength >> 1);
			return this;
		}

		@Override
		public SS_Triangle setYCenter(int y) {
			this.yCenter = y;
			yLeftBottom = y - (borderLength >> 1);
			return this;
		}

		@Override
		public SS_Triangle setLeftBottomCorner(int x, int y) {
			int hbl;
			hbl = borderLength >> 1;
			this.xLeftBottom = x;
			this.yLeftBottom = y;
			xCenter = x + hbl;
			yCenter = y + hbl;
			return this;
		}

		@Override
		public SS_Triangle setCenter(int x, int y) {
			int hbl;
			hbl = borderLength >> 1;
			xCenter = x;
			yCenter = y;
			this.xLeftBottom = x - hbl;
			this.yLeftBottom = y - hbl;
			return this;
		}

		@Override
		public SS_Triangle clone() {
			return new SS_Triangle(this);
		}
	}

	// TODO SS_Cone
	public static class SS_Cone extends ShapeCenteredRotated {
		private static final long serialVersionUID = -65100784908L;

		public SS_Cone(SS_Cone g) {
			super(g);
			this.numberOfSteps = g.numberOfSteps;
			this.baseHeight = g.baseHeight;
			this.widthIncrement = g.widthIncrement;
			this.heightIncrement = g.heightIncrement;
			// this.angleDeg = g.angleDeg;
			this.totalWidth = g.totalWidth;
			this.totalHeight = g.totalHeight;
		}

		public SS_Cone(int xx, int yy, int numberOfSteps, int baseHeight, int widthIncrement, int heightIncrement,
				double angleDeg) {
			super(ShapesImplemented.Cone, xx, yy, angleDeg);
			if (numberOfSteps < 1 || baseHeight < 0 || widthIncrement < 1 || heightIncrement < 1)
				throw new IllegalArgumentException("Invalid parameters");

			this.numberOfSteps = numberOfSteps;
			this.baseHeight = baseHeight;
			this.widthIncrement = widthIncrement;
			this.heightIncrement = heightIncrement;
			recalcTotals();
		}

		int numberOfSteps = 1, baseHeight, widthIncrement, heightIncrement//
				, totalWidth, totalHeight;

		public int getNumberOfSteps() {
			return numberOfSteps;
		}

		public int getBaseHeight() {
			return baseHeight;
		}

		public int getWidthIncrement() {
			return widthIncrement;
		}

		public int getHeightIncrement() {
			return heightIncrement;
		}

		public int getTotalWidth() {
			return totalWidth;
		}

		public int getTotalHeight() {
			return totalHeight;
		}

		//

		public SS_Cone setNumberOfSteps(int numberOfSteps) {
			if (numberOfSteps > 0 && numberOfSteps != this.numberOfSteps) {
				this.numberOfSteps = numberOfSteps;
				recalcTotals();
			}
			return this;
		}

		public SS_Cone setBaseHeight(int baseHeight) {
			if (baseHeight >= 0 && baseHeight != this.baseHeight) {
				this.baseHeight = baseHeight;
				recalcTotals();
			}
			return this;
		}

		public SS_Cone setWidthIncrement(int widthIncrement) {
			if (widthIncrement > 0 && widthIncrement != this.widthIncrement) {
				this.widthIncrement = widthIncrement;
				recalcTotals();
			}
			return this;
		}

		public SS_Cone setHeightIncrement(int heightIncrement) {
			if (heightIncrement > 0 && heightIncrement != this.heightIncrement) {
				this.heightIncrement = heightIncrement;
				recalcTotals();
			}
			return this;
		}

		@Override
		public SS_Cone setXLeftBottom(int x) {
			this.xLeftBottom = x;
			xCenter = x + (totalWidth >> 1);
			return this;
		}

		@Override
		public SS_Cone setYLeftBottom(int y) {
			this.yLeftBottom = y;
			yLeftBottom = y + (totalHeight >> 1);
			return this;
		}

		@Override
		public SS_Cone clone() {
			return new SS_Cone(this);
		}

		@Override
		public SS_Cone setXCenter(int x) {
			xCenter = x;
			xLeftBottom = x - (totalWidth >> 1);
			return this;
		}

		@Override
		public SS_Cone setYCenter(int y) {
			yCenter = y;
			yLeftBottom = y - (totalHeight >> 1);
			return this;
		}

		@Override
		public SS_Cone setLeftBottomCorner(int x, int y) {
			this.xLeftBottom = x;
			this.yLeftBottom = y;
			xCenter = x + (totalWidth >> 1);
			yCenter = y + (totalHeight >> 1);
			return this;
		}

		@Override
		public SS_Cone setCenter(int x, int y) {
			xCenter = x;
			yCenter = y;
			this.xLeftBottom = x - (totalWidth >> 1);
			this.yLeftBottom = y - (totalHeight >> 1);
			return this;
		}

		protected void recalcTotals() {
			totalWidth = numberOfSteps * widthIncrement;
			totalHeight = baseHeight + ((numberOfSteps * heightIncrement) << 1);
			xCenter = xLeftBottom + (totalWidth >> 1);
			yCenter = yLeftBottom + (totalHeight >> 1);
		}

	}

	// TODO SS_Arrow
	public static class SS_Arrow extends ShapeWithCenter {
		private static final long serialVersionUID = -65100784907L;

		public SS_Arrow(int xx, int yy, int borderLength, double angleDeg) {
			this(ShapesImplemented.ArrowBorderBodySameLength, xx, yy, borderLength, 0, 0, angleDeg);
		}

		public SS_Arrow(int xx, int yy, int borderLength, double angleDegBorder, int bodyLength, double angleDeg) {
			this(ShapesImplemented.Arrow, xx, yy, borderLength, angleDegBorder, bodyLength, angleDeg);
		}

		protected SS_Arrow(ShapesImplemented si, int xx, int yy, int borderLength, double angleDegBorder,
				int bodyLength, double angleDeg) {
			super(si, xx, yy);
			this.borderLength = borderLength;
			this.angleDegBorder = angleDegBorder;
			this.bodyLength = bodyLength;
			this.angleDeg = angleDeg;
			recalcTotals();
		}

		public SS_Arrow(SS_Arrow gss) {
			super(gss);
			this.borderLength = gss.borderLength;
			this.angleDegBorder = gss.angleDegBorder;
			this.bodyLength = gss.bodyLength;
			this.angleDeg = gss.angleDeg;
			this.totalWidth = gss.totalWidth;
			this.totalHeight = gss.totalHeight;
		}

		int borderLength, bodyLength, totalWidth, totalHeight;
		double angleDegBorder, angleDeg;

		public int getBorderLength() {
			return borderLength;
		}

		public double getAngleDegBorder() {
			return angleDegBorder;
		}

		public int getBodyLength() {
			return bodyLength;
		}

		public double getAngleDeg() {
			return angleDeg;
		}

		public SS_Arrow setBorderLength(int borderLength) {
			if (borderLength != this.borderLength) {
				this.borderLength = borderLength;
				recalcTotals();
			}
			return this;
		}

		public SS_Arrow setAngleDegBorder(double angleDegBorder) {
			angleDegBorder = MathUtilities.adjustDegAngle(angleDegBorder);
			if (angleDegBorder != this.angleDegBorder) {
				this.angleDegBorder = angleDegBorder;
				recalcTotals();
			}
			return this;
		}

		public SS_Arrow setAngleDeg(double angleDeg) {
			this.angleDeg = MathUtilities.adjustDegAngle(angleDeg);
			return this;
		}

		public SS_Arrow setBodyLength(int bodyLength) {
			if (bodyLength != this.bodyLength) {
				totalWidth += (bodyLength - this.bodyLength) / 2;
				this.bodyLength = bodyLength;
				// recalcTotals();
			}
			return this;
		}

		@Override
		public SS_Arrow setXCenter(int x) {
			xCenter = x;
			xLeftBottom = x - (totalWidth >> 1);
			return this;
		}

		@Override
		public SS_Arrow setYCenter(int y) {
			yCenter = y;
			yLeftBottom = y - (totalHeight >> 1);
			return this;
		}

		@Override
		public SS_Arrow setLeftBottomCorner(int x, int y) {
			this.xLeftBottom = x;
			this.yLeftBottom = y;
			xCenter = x + (totalWidth >> 1);
			yCenter = y + (totalHeight >> 1);
			return this;
		}

		@Override
		public SS_Arrow setCenter(int x, int y) {
			xCenter = x;
			yCenter = y;
			this.xLeftBottom = x - (totalWidth >> 1);
			this.yLeftBottom = y - (totalHeight >> 1);
			return this;
		}

		protected void recalcTotals() {
			double rad, cosBorder, sinBorder;
			rad = Math.toRadians(MathUtilities.adjustDegAngle(180 - getAngleDegBorder()));
			cosBorder = Math.cos(rad) * borderLength;
			sinBorder = Math.sin(rad) * borderLength;

			totalWidth = (int) (cosBorder > 0 ? (bodyLength + cosBorder) : Math.max(bodyLength, -cosBorder));
			if (sinBorder < 0)
				sinBorder = -sinBorder;
			totalHeight = (int) (sinBorder * 2.0);
			xCenter = xLeftBottom + (totalWidth >> 1);
			yCenter = yLeftBottom + (totalHeight >> 1);
		}

		@Override
		public SS_Arrow clone() {
			return new SS_Arrow(this);
		}
	}

	// TODO RECTANGLE
	public static class SS_Ellipse extends ShapeWithCenter implements AbstractObjectRectangleBoxed {
		private static final long serialVersionUID = -65100784905L;

		public SS_Ellipse(int xLeftBottom, int yLeftBottom, int width, int height) {
			this(true, xLeftBottom, yLeftBottom, width, height);
		}

		public SS_Ellipse(boolean isFillingEllipse, int xLeftBottom, int yLeftBottom, int width, int height) {
			this(isFillingEllipse, xLeftBottom, yLeftBottom, width, height, 0);
		}

		public SS_Ellipse(boolean isFillingEllispe, int xLeftBottom, int yLeftBottom, int width, int height,
				double angleDeg) {
			super(isFillingEllispe ? (angleDeg == 0.0 ? ShapesImplemented.EllipseNoRotation : null)
					: (angleDeg == 0.0 ? ShapesImplemented.EllipseNoRotation_Border : null), xLeftBottom, yLeftBottom);
			this.setWidth(width);
			this.setHeight(height);
			this.angleDeg = angleDeg;
		}

		public SS_Ellipse(SS_Ellipse gss) {
			super(gss);
			width = gss.width;
			height = gss.height;
			angleDeg = gss.angleDeg;
		}

		int width, height;
		double angleDeg;

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public double getAngleDeg() {
			return angleDeg;
		}

		//

		@Override
		public SS_Ellipse setXLeftBottom(int x) {
			this.xLeftBottom = x;
			xCenter = x + (width >> 1);
			return this;
		}

		@Override
		public SS_Ellipse setYLeftBottom(int y) {
			this.yLeftBottom = y;
			yCenter = y + (height >> 1);
			return this;
		}

		@Override
		public SS_Ellipse setWidth(int width) {
			if (width > 0) {
				this.width = width;
				xCenter = getXLeftBottom() + (width >> 1);
			}
			return this;
		}

		@Override
		public SS_Ellipse setLeftBottomCorner(int x, int y) {
			this.xLeftBottom = x;
			this.yLeftBottom = y;
			xCenter = x + (width >> 1);
			yCenter = y + (height >> 1);
			return this;
		}

		@Override
		public SS_Ellipse setCenter(int x, int y) {
			xCenter = x;
			yCenter = y;
			this.xLeftBottom = x - (width >> 1);
			this.yLeftBottom = y - (height >> 1);
			return this;
		}

		@Override
		public SS_Ellipse setHeight(int height) {
			if (height > 0) {
				this.height = height;
				yCenter = getYLeftBottom() + (height >> 1);
			}
			return this;
		}

		@Override
		public SS_Ellipse setAngleDeg(double angleDeg) {
			this.angleDeg = angleDeg;
			return this;
		}

		@Override
		public SS_Ellipse setXCenter(int x) {
			if (xCenter != x) {
				xCenter = x;
				xLeftBottom = x - (width >> 1);
			}
			return this;
		}

		@Override
		public SS_Ellipse setYCenter(int y) {
			if (yCenter != y) {
				yCenter = y;
				yLeftBottom = y - (height >> 1);
			}
			return this;
		}

		@Override
		public SS_Ellipse clone() {
			return new SS_Ellipse(this);
		}

		@Override
		public String toString() {
			return "SS_Ellipse [width=" + width + ", height=" + height + ", angleDeg=" + angleDeg + super.toString()
					+ "]";
		}
	}

	//

	// TODO FOR OBSERVERS

}