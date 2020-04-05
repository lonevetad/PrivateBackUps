package common.abstractCommon;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

public interface MouseClickListener extends MouseListener, Serializable {

	@Override
	public default void mousePressed(MouseEvent e) {
	}

	@Override
	public default void mouseReleased(MouseEvent e) {
	}

	@Override
	public default void mouseEntered(MouseEvent e) {
	}

	@Override
	public default void mouseExited(MouseEvent e) {
	}
}