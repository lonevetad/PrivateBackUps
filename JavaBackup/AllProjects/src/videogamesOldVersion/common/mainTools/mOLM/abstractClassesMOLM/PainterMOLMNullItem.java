package common.mainTools.mOLM.abstractClassesMOLM;

import java.awt.Graphics;

import common.abstractCommon.behaviouralObjectsAC.AbstractPainter;

/**
 * Utilizzata solo per il metodo di paint della matrice in una istanza di
 * {@link java.awt.Graphics}.<br>
 * Questa interfaccia specifica cosa fare nel caso in cui, scorrendo la matrice, in una cella si
 * punti a <code>null</code>.<br>
 * Solitamente, si passa una lambda expression vuota, o ancora meglio <code>null</code>.
 */
public interface PainterMOLMNullItem extends AbstractPainter {

	public int getWidth();

	public int getHeight();

	//

	public PainterMOLMNullItem setWidth(int width);

	public PainterMOLMNullItem setHeight(int height);

	//

	@Override
	public default void paintOn(Graphics g, int x, int y) {
		paintOn(g, x, y, getWidth(), getHeight());
	}

	public static PainterMOLMNullItem newDefaultInstance() {
		return new PainterMOLMNullItem_DEFAULT();
	}

	public static PainterMOLMNullItem newInstance_MicropixelPurpose() {
		return new PainterMOLMNullItem_MicropixelPurpose();
	}

	public static PainterMOLMNullItem newInstance_JustFieldsKeeper() {
		return new PainterMOLMNullItem_JustFieldsKeeper();
	}

	public static PainterMOLMNullItem getOrDefault(PainterMOLMNullItem pmni) {
		return pmni != null ? pmni : newInstance_JustFieldsKeeper();
	}

	//

	public static class PainterMOLMNullItem_DEFAULT implements PainterMOLMNullItem {
		private static final long serialVersionUID = -89498148153687007L;

		public PainterMOLMNullItem_DEFAULT() {
			super();
			width = height = 1;
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

		@Override
		public PainterMOLMNullItem setWidth(int width) {
			if (width > 0) this.width = width;
			return this;
		}

		@Override
		public PainterMOLMNullItem setHeight(int height) {
			if (height > 0) this.height = height;
			return this;
		}

		@Override
		public void paintOn(Graphics g, int x, int y) {
			paintOn(g, x, y, width, height);
		}

		@Override
		public void paintOn(Graphics g, int x, int y, int width, int height) {
			AbstractPainter.DEFAULT_PAINTER_BLACK_RECTANGULAR.paintOn(g, x, y, width, height);
		}
	}

	public static class PainterMOLMNullItem_MicropixelPurpose implements PainterMOLMNullItem {
		private static final long serialVersionUID = -89498148153687006L;

		public PainterMOLMNullItem_MicropixelPurpose() {
			super();
			sizeSquare = 1;
		}

		int sizeSquare;

		@Override
		public int getWidth() {
			return sizeSquare;
		}

		@Override
		public int getHeight() {
			return sizeSquare;
		}

		@Override
		public PainterMOLMNullItem setWidth(int width) {
			if (width > 0) this.sizeSquare = width;
			return this;
		}

		@Override
		public PainterMOLMNullItem setHeight(int height) {
			if (height > 0) this.sizeSquare = height;
			return this;
		}

		@Override
		public void paintOn(Graphics g, int x, int y) {
			paintOn(g, x, y, 1, 1);
		}

		@Override
		public void paintOn(Graphics g, int x, int y, int width, int height) {
			AbstractPainter.DEFAULT_PAINTER_BLACK_RECTANGULAR.paintOn(g, x * sizeSquare, y * sizeSquare,
					width * sizeSquare, height * sizeSquare);
		}
	}

	public static class PainterMOLMNullItem_JustFieldsKeeper extends PainterMOLMNullItem_MicropixelPurpose {
		private static final long serialVersionUID = -6208788L;

		@Override
		public void paintOn(Graphics g, int x, int y, int width, int height) {
		}
	}
}
