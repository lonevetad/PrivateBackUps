package common;

import common.abstractCommon.MainController;
import common.abstractCommon.referenceHolderAC.FrameHolder;
import common.abstractCommon.referenceHolderAC.MainHolder;

/** Semplice wrapper che conserva un intero: il frame attuale. Punto. */
public class FrameHolderDEFAULT implements FrameHolder, MainHolder {
	private static final long serialVersionUID = 26018180000550L;

	public FrameHolderDEFAULT() {
		lastFrame = 0;
	}

	int lastFrame;
	transient MainController main;

	@Override
	public int getLastFrame() {
		return lastFrame;
	}

	@Override
	public MainController getMain() {
		return main;
	}

	@Override
	public int getCurrentFrame() {
		// throw new UnsupportedOperationException(
		// "FrameHolderDEFAULT is not able to fetch the current frame, just
		// holds the last");
		return getMain().getMainGenericGUI().getCurrentFrame();
	}

	@Override
	public FrameHolder setLastFrame(int frame) {
		lastFrame = frame;
		return this;
	}

	@Override
	public MainHolder setMain(MainController main) {
		if (main != null)
			this.main = main;
		return this;
	}
}