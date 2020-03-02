package common.abstractCommon.shapedObject;

import java.awt.Image;

import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

/**
 * As described in {@link AbstractMatrixObjectLocationManager} and {@link ShapeSpecification}, all
 * values (excepted from explicit specification or an obvious meaning), MUST be considered and
 * expressed in "micropixel". <br>
 * Added an {@link Image} reference but it could be useless
 * <p>
 * N.B.: The shape of the bounding box is, obviously, a rectangle.
 */
public interface AbstractObjectRectangleBoxed extends AbstractObjectOnCartesianPlan {
	/*
	 * public default BufferedImage getImage() { return null; } public default
	 * AbstractObjectRectangleBoxed setImage(BufferedImage image) { return null; }
	 */

	// getter

	public double getAngleDeg();

	@Override
	public default int getXCenter() {
		return getXLeftBottom() + (getWidth() >> 1);
	}

	@Override
	public default int getYCenter() {
		return getYLeftBottom() + (getHeight() >> 1);
	}

	/** Should be expressed in {@link AbstractMatrixObjectLocationManager}'s "<i>micropixel</i>". */
	public int getWidth();

	/** See {@link #getWidth()}. */
	public int getHeight();

	// setter

	@Override
	public default AbstractObjectOnCartesianPlan setXCenter(int x) {
		setXLeftBottom(x - (getWidth() >> 1));
		return this;
	}

	@Override
	public default AbstractObjectOnCartesianPlan setYCenter(int y) {
		setYLeftBottom(y - (getHeight() >> 1));
		return this;
	}

	public AbstractObjectRectangleBoxed setWidth(int width);

	public AbstractObjectRectangleBoxed setHeight(int height);

	public Object setAngleDeg(double angleDeg);

	public default boolean intersects(AbstractObjectRectangleBoxed anotherBox) {
		boolean b;
		int x, y, w, h, mx, my, mw, mh;
		b = false;
		if (anotherBox != null) {
			if (anotherBox == this)
				b = true;
			else {
				mx = getXLeftBottom();
				my = getYLeftBottom();
				mw = getWidth();
				mh = getHeight();
				x = anotherBox.getXLeftBottom();
				y = anotherBox.getYLeftBottom();
				w = anotherBox.getWidth();
				h = anotherBox.getHeight();
				if (mw <= 0 || mh <= 0 || w <= 0 || h <= 0) {
					b = false;
				} else
					// From Java's Rectangle2D
					b = (x + w > mx && y + h > my && //
							x < mx + mw && y < my + mh);
			}
		}
		return b;
	}
}
