package common.abstractCommon.behaviouralObjectsAC;

import java.awt.Color;
import java.awt.Graphics;

public interface AbstractPainter extends AbstractPainterSimple {

	public static final AbstractPainter DEFAULT_PAINTER_BLACK_RECTANGULAR = (g, x, y, w, h) -> {
		Color pc;
		if (g == null) return;
		if (x < 0) {
			w += x;
			x = 0;
		}
		if (y < 0) {
			h += y;
			y = 0;
		}
		if (w < 1 || h < 1) return;
		pc = g.getColor();
		g.setColor(Color.BLACK);
		// g.drawLine(x++, y, x, y);
		g.fillRect(x, y, w, h);
		g.setColor(pc);
	};

	//

	@Override
	public default void paintOn(Graphics g) {
		paintOn(g, 0, 0);
	}

	public default void paintOn(Graphics g, int x, int y) {
		paintOn(g, x, y, 1, 1);
	}

	public default void paintOn(int width, int height, Graphics g) {
		paintOn(g, 0, 0, width, height);
	}

	public void paintOn(Graphics g, int x, int y, int width, int height);
}
