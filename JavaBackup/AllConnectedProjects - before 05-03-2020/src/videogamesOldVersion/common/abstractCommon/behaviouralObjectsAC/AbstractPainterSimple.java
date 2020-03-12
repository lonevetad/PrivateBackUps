package common.abstractCommon.behaviouralObjectsAC;

import java.awt.Graphics;
import java.io.Serializable;

public interface AbstractPainterSimple extends Serializable {
	public static final AbstractPainterSimple DEFAULT_PAINTER_SIMPLE = (g) -> {
	};

	public void paintOn(Graphics g);

	public static AbstractPainterSimple getOrDefault(AbstractPainterSimple p) {
		return p == null ? DEFAULT_PAINTER_SIMPLE : p;
	}
}
