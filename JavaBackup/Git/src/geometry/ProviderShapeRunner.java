package geometry;

import geometry.pointTools.PointConsumer;

public interface ProviderShapeRunner extends AbstractShapeRunner {

	public AbstractShapeRunner getShapeRunner(ShapeRunnersImplemented sri);

	@Override
	public default ShapeRunnersImplemented getShapeRunnersImplemented() {
		return null;
	}

	@Override
	public default boolean runShape(AbstractShape2D shape, PointConsumer action) {
		AbstractShapeRunner r;
		if (shape == null || action == null)
			return false;
		r = this.getShapeRunner(shape.getShapeImplementing());
		return r == null ? false : r.runShape(shape, action);
	}
}