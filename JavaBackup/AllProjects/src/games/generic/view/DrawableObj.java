package games.generic.view;

import java.awt.Point;
import java.awt.image.BufferedImage;

import games.generic.controlModel.gObj.TimedObject;

public interface DrawableObj {
// TODO un oggetto con forma e Drawable, tipo immagine o GIF
	/**
	 * Both a static image or an animation (and possibly a 3D object) could have an
	 * image associated. This is that image.
	 */
	public BufferedImage getCurrentImage();

	/** This object is located somewhere in space. */
	public Point getLocation();

	/** See {@link #getLocation()}. */
	public int getX();

	/** See {@link #getLocation()}. */
	public int getY();

	/**
	 * During the rendering process (that is probably performed cyclically and
	 * identified by a {@link long}: the first parameter) some images could require
	 * to change or update as time goes on. Similarly to what is specified in
	 * {@link TimedObject}'s documentation, the <i>amount of time elapsed</i> (the
	 * second parameter) could be considered as a <i>sub-unit</i>
	 */
	public BufferedImage progressImageByTime(long cyclePainting, int timeSubunits);

	/** Should not be used */
	public default void drawOn(Drawer drawer) {
		if (drawer != null) { drawer.drawImage(getCurrentImage(), getX(), getY()); }
	}
}