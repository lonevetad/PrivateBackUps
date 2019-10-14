package geometry;

import java.io.Serializable;

import geometry.pointTools.PointConsumer;

public interface AbstractShapeRunner extends Serializable {

	public ShapeRunnersImplemented getShapeRunnersImplemented();

//	public AbstractShapeRunner setShapeRunnersImplemented(ShapeRunnersImplemented shapeRunnersImplemented);

	/**
	 * If the given shape is compatible with this runner (for instance, a circle
	 * runner is not adequate to a rectangular shape), then performs an action on a
	 * area described by the implementation on a specific shape. The permission to
	 * cycle is performed.<br>
	 * To do so, check the equality of the instance of
	 * {@link ShapeRunnersImplemented} returned by
	 * {@link #getShapeRunnersImplemented()} and the instance returned by
	 * {@link AbstractShape2D#getShapeImplementing()}.
	 * <p>
	 * NOTE: this should be a final
	 */
	public boolean runShape(AbstractShape2D shape, PointConsumer action);
}