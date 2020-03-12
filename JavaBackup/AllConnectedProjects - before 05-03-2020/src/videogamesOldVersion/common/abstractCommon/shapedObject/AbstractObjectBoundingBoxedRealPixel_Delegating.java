package common.abstractCommon.shapedObject;

public interface AbstractObjectBoundingBoxedRealPixel_Delegating extends AbstractObjectBoundingBoxedRealPixel {

	public AbstractObjectRectangleBoxed getObjectBoundingBoxed();

	public AbstractObjectBoundingBoxedRealPixel_Delegating setObjectBoundingBoxed(
			AbstractObjectRectangleBoxed objectBoundingBoxed);

	@Override
	public default int getScaleMicropixelToRealpixel() {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		return (obb == null || (!(obb instanceof AbstractObjectBoundingBoxedRealPixel_Delegating))) ? Integer.MIN_VALUE
				: ((AbstractObjectBoundingBoxedRealPixel_Delegating) obb).getScaleMicropixelToRealpixel();
	}

	@Override
	public default AbstractObjectBoundingBoxedRealPixel setScaleMicropixelToRealpixel(int scaleMicropixelToRealpixel) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		return (obb == null || (!(obb instanceof AbstractObjectBoundingBoxedRealPixel_Delegating))) ? null
				: ((AbstractObjectBoundingBoxedRealPixel_Delegating) obb)
						.setScaleMicropixelToRealpixel(scaleMicropixelToRealpixel);
	}

	@Override
	public default int getXLeftBottom() {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		return (obb == null) ? Integer.MIN_VALUE : obb.getXLeftBottom();
	}

	@Override
	public default int getYLeftBottom() {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		return (obb == null) ? Integer.MIN_VALUE : obb.getYLeftBottom();
	}

	@Override
	public default double getAngleDeg() {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		return (obb == null) ? Double.NaN : obb.getAngleDeg();
	}

	@Override
	public default int getXCenter() {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		return (obb == null) ? Integer.MIN_VALUE : obb.getXCenter();
	}

	@Override
	public default int getYCenter() {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		return (obb == null) ? Integer.MIN_VALUE : obb.getYCenter();
	}

	@Override
	public default int getWidth() {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		return (obb == null) ? Integer.MIN_VALUE : obb.getWidth();
	}

	@Override
	public default int getHeight() {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		return (obb == null) ? Integer.MIN_VALUE : obb.getHeight();
	}

	//

	// TODO SETTER

	@Override
	public default AbstractObjectRectangleBoxed setAngleDeg(double angleDeg) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		if (obb == null) return null;
		obb.setAngleDeg(angleDeg);
		return this;
	}

	@Override
	public default AbstractObjectOnCartesianPlan setXLeftBottom(int x) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		if (obb == null) return null;
		obb.setXLeftBottom(x);
		return this;
	}

	@Override
	public default AbstractObjectOnCartesianPlan setYLeftBottom(int y) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		if (obb == null) return null;
		obb.setYLeftBottom(y);
		return this;
	}

	@Override
	public default AbstractObjectOnCartesianPlan setLeftBottomCorner(int x, int y) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		if (obb == null) return null;
		obb.setLeftBottomCorner(x, y);
		return this;
	}

	@Override
	public default AbstractObjectOnCartesianPlan setXCenter(int x) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		if (obb == null) return null;
		obb.setXCenter(x);
		return this;
	}

	@Override
	public default AbstractObjectOnCartesianPlan setYCenter(int y) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		if (obb == null) return null;
		obb.setYCenter(y);
		return this;
	}

	@Override
	public default AbstractObjectOnCartesianPlan setCenter(int x, int y) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		if (obb == null) return null;
		obb.setCenter(x, y);
		return this;
	}

	@Override
	public default AbstractObjectBoundingBoxedRealPixel setWidth(int width) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		if (obb == null) return null;
		obb.setWidth(width);
		return this;
	}

	@Override
	public default AbstractObjectBoundingBoxedRealPixel setHeight(int height) {
		AbstractObjectRectangleBoxed obb;
		obb = getObjectBoundingBoxed();
		if (obb == null) return null;
		obb.setHeight(height);
		return this;
	}
}