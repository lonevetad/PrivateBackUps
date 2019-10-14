package common;

import java.awt.Color;
import java.awt.Graphics;

import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.PainterMOLMNullItem;

public class PainterMolm implements DoSomethingWithNode, PainterMOLMNullItem {
	private static final long serialVersionUID = 5565000506548062123L;
	public static final Color COLOR_ON_OWID_ON_NOT_LEFTBOTTOM_CORNER = new Color(0x05F5D4);

	public PainterMolm() {
		this(null);
	}

	public PainterMolm(Graphics g) {
		this(g, PainterMOLMNullItem.newDefaultInstance());
	}

	public PainterMolm(Graphics g, PainterMOLMNullItem pmoni) {
		this.graphics = g;
		/*
		 * this.sizeTile = sizeTile; yActual = -1; yTileSized = -sizeTile;
		 */
		this.painterMOLMNullItem = pmoni;
	}

	Graphics graphics;
	// int sizeTile, yActual, yTileSized;
	PainterMOLMNullItem painterMOLMNullItem;

	public static PainterMolm newInstance() {
		return newInstance(PainterMOLMNullItem.newInstance_MicropixelPurpose());
	}

	public static PainterMolm newInstance(PainterMOLMNullItem pmoni) {
		return new PainterMolm(null, pmoni);
	}

	//

	// TODO GETTER

	public Graphics getGraphics() {
		return graphics;
	}

	public PainterMOLMNullItem getPainterMOLMNullItem() {
		return painterMOLMNullItem;
	}

	@Override
	public int getWidth() {
		return painterMOLMNullItem == null ? Integer.MIN_VALUE : painterMOLMNullItem.getWidth();
	}

	@Override
	public int getHeight() {
		return painterMOLMNullItem == null ? Integer.MIN_VALUE : painterMOLMNullItem.getHeight();
	}
	//

	// TODO SETTER

	@Override
	public PainterMOLMNullItem setWidth(int width) {
		return painterMOLMNullItem.setWidth(width);
	}

	@Override
	public PainterMOLMNullItem setHeight(int height) {
		return painterMOLMNullItem.setHeight(height);
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

	public void setPainterMOLMNullItem(PainterMOLMNullItem painterMOLMNullItem) {
		this.painterMOLMNullItem = PainterMOLMNullItem.getOrDefault(painterMOLMNullItem);
	}

	//

	// TODO OTHERs

	@Override
	public Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int x, int y) {
		/**
		 * AbstractImageObjectBoundedPixeled obbrp;// <br>
		 * if (graphics == null) return null;// <br>
		 * if (item != null) {// <br>
		 * if (canBePainted(item)) {// <br>
		 * if (item instanceof AbstractImageObjectBoundedPixeled) {// <br>
		 * obbrp = (AbstractImageObjectBoundedPixeled) item;// <br>
		 * // if (y == obbrp.getYCenter() && x == obbrp.getXCenter()) {// <br>
		 * if (obbrp.getAngleDeg() != 0.0 && obbrp.getImageOriginal() != null)// <br>
		 * GraphicTools.paintRoeated(graphics, obbrp);// <br>
		 * else// <br>
		 * graphics.drawImage(obbrp.getImageOriginal(), obbrp.getXLeftBottomRealPixel(),// <br>
		 * obbrp.getYLeftBottomRealPixel(), obbrp.getWidthRealPixel(), obbrp.getHeightRealPixel(),//
		 * <br>
		 * null);// <br>
		 * // }// <br>
		 * }// <br>
		 * if (item instanceof AbstractPainter) {// <br>
		 * ((AbstractPainter) item).paintOn(graphics, x, y);// <br>
		 * }// <br>
		 * processAfterPaint(item);// <br>
		 * }// <br>
		 * } else {// <br>
		 * if (painterMOLMNullItem != null) painterMOLMNullItem.paintOn(graphics, x, y); } // <br>
		 */
		return item;
	}

	@Override
	public void paintOn(Graphics g, int x, int y, int width, int height) {
		painterMOLMNullItem.paintOn(g, x, y, width, height);
	}

	/**
	 * Override-designed
	 */
	public void prepareForPaintingRun() {
		// nothing to do here for now
	}

	/**
	 * Override-designed
	 */
	public void processAfterPaint(ObjectWithID obbrp) {
		// do nothing here
	}

	/**
	 * Override-designed
	 */
	public boolean canBePainted(ObjectWithID obbrp) {
		return obbrp != null;
	}
}