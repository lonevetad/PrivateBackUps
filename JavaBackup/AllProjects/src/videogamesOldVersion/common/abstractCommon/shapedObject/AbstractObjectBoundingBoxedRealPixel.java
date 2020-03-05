package common.abstractCommon.shapedObject;

import common.abstractCommon.referenceHolderAC.ScaleMicropixelToPixelHolder;

public interface AbstractObjectBoundingBoxedRealPixel
		extends AbstractObjectRectangleBoxed, ScaleMicropixelToPixelHolder {

	// getter

	//

	public default int getXLeftBottomRealPixel() {
		return getXLeftBottom() * getScaleMicropixelToRealpixel();
	}

	public default int getYLeftBottomRealPixel() {
		return getYLeftBottom() * getScaleMicropixelToRealpixel();
	}

	public default int getXCenterRealPixel() {
		return getXCenter() * getScaleMicropixelToRealpixel();
	}

	public default int getYCenterRealPixel() {
		return getYCenter() * getScaleMicropixelToRealpixel();
	}

	public default int getWidthRealPixel() {
		return getWidth() * getScaleMicropixelToRealpixel();
	}

	public default int getHeightRealPixel() {
		return getHeight() * getScaleMicropixelToRealpixel();
	}

	// setter

	//

	public default AbstractObjectBoundingBoxedRealPixel setXLeftBottomRealPixel(int x) {
		setXLeftBottom(x / getScaleMicropixelToRealpixel());
		return this;
	}

	public default AbstractObjectBoundingBoxedRealPixel setYLeftBottomRealPixel(int y) {
		setYLeftBottom(y / getScaleMicropixelToRealpixel());
		return this;
	}

	public default AbstractObjectBoundingBoxedRealPixel setLeftBottomCornerRealPixel(int x, int y) {
		setXLeftBottomRealPixel(x);
		setYLeftBottomRealPixel(y);
		return this;
	}

	public default AbstractObjectBoundingBoxedRealPixel setXCenterRealPixel(int x) {
		setXCenter(x / getScaleMicropixelToRealpixel());
		return this;
	}

	public default AbstractObjectBoundingBoxedRealPixel setYCenterRealPixel(int y) {
		setYCenter(y / getScaleMicropixelToRealpixel());
		return this;
	}

	public default AbstractObjectBoundingBoxedRealPixel setCenterRealPixel(int x, int y) {
		setXCenterRealPixel(x);
		setYCenterRealPixel(y);
		return this;
	}

	public default AbstractObjectBoundingBoxedRealPixel setWidthRealPixel(int width) {
		if (width < 0) return null;
		setWidth(width / getScaleMicropixelToRealpixel());
		return this;
	}

	public default AbstractObjectBoundingBoxedRealPixel setHeightRealPixel(int height) {
		if (height < 0) return null;
		setHeight(height / getScaleMicropixelToRealpixel());
		return this;
	}
}
