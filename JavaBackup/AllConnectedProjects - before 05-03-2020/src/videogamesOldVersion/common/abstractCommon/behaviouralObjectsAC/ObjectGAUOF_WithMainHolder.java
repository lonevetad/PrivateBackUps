package common.abstractCommon.behaviouralObjectsAC;

import common.abstractCommon.MainController;
import common.abstractCommon.referenceHolderAC.MainHolder;

/**
 * Shorthand to {@link ObjectGuiUpdatingOnFrame} just to implements
 * {@link ObjectGuiUpdatingOnFrame#getCurrentFrame()} as described by
 * its documentation.
 */
public interface ObjectGAUOF_WithMainHolder extends ObjectGuiUpdatingOnFrame, MainHolder {

	@Override
	public default int getCurrentFrame() {
		MainController m = getMain();
		return m != null ? m.getMainGenericGUI().getCurrentFrame() : Integer.MIN_VALUE;
	}

	/**
	 * That's NOT my task !<br>
	 * {@link MainController} should do this !<br>
	 * So i delegate this task to it.
	 */
	public default ObjectGuiUpdatingOnFrame setCurrentFrame(int frame) {
		MainController m;
		if ((m = getMain()) != null) m.getMainGenericGUI().setCurrentFrame(frame);
		return this;
	}
}