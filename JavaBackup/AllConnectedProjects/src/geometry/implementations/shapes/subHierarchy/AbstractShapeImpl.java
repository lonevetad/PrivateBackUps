package geometry.implementations.shapes.subHierarchy;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;

public abstract class AbstractShapeImpl extends AbstractShapeRotated {
	private static final long serialVersionUID = -5641048035L;

	public AbstractShapeImpl(ShapeRunnersImplemented shapeImplementing) {
		super(shapeImplementing);
		this.xCenter = 0;
		this.yCenter = 0;
	}

	public AbstractShapeImpl(AbstractShapeImpl s) {
		super(s);
		this.xCenter = s.xCenter;
		this.yCenter = s.yCenter;
	}

	public AbstractShapeImpl(ShapeRunnersImplemented shapeImplementing, double angleRotation, int xCenter,
			int yCenter) {
		super(shapeImplementing, angleRotation);
		this.xCenter = xCenter;
		this.yCenter = yCenter;
	}

	protected int xCenter, yCenter;

	//

	@Override
	public int getXCenter() {
		return xCenter;
	}

	@Override
	public int getYCenter() {
		return yCenter;
	}

	//
	//

	@Override
	public AbstractShape2D setXCenter(int x) {
		if (this.xCenter != x)
			this.polygonCache = null;
		super.setXCenter(x);
		this.xCenter = x;
		return this;
	}

	@Override
	public AbstractShape2D setYCenter(int y) {
		if (this.yCenter != y)
			this.polygonCache = null;
		super.setYCenter(y);
		this.yCenter = y;
		return this;
	}
}