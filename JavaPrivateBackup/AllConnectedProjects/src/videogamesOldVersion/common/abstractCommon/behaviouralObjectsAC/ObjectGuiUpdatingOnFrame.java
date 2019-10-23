package common.abstractCommon.behaviouralObjectsAC;

import common.abstractCommon.referenceHolderAC.FrameHolder;
import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

/**
 * This interface has just one meaning: update the image/animation/GUI just if the frame has
 * changed.<br>
 * Why? because one of {@link ObjectWithID} subclasses could implements this interface, being added
 * inside a {@link MatrixObjectLocationManager} with a {@link ShapeSpecification} greater than a
 * single point and some kind of painting process is trying to update this instance several times
 * (maybe because that process iterate over the {@link MatrixObjectLocationManager}) during the same
 * refresh (so the frame is remaining the same).<br>
 * Remembering the last frame and updating once per frame could resolve some GUI's bugs.
 * <P>
 * Deprecated because there are too many abstract methods and the "act" should be used instead of
 * {@link #performAnimation(int)}, even if the logic held by this interface is interesting.
 */
public interface ObjectGuiUpdatingOnFrame extends ObjectGuiUpdatingOnTime, FrameHolder {

	/**
	 * Realize the animation.<br>
	 * Must NOT be called. Call {@link #updateGui(int)} instead.
	 */
	public void performAnimation(int milliseconds);

	/**
	 * {@inheritDoc}, BUT did not perform any animation or changes before a test over the current
	 * frame.<br>
	 * Override the method {@link #performAnimation(int)} to describe how the animation will be
	 * performed or changed.
	 */
	@Override
	public default void updateGui(int milliseconds) {
		int frame;
		frame = getCurrentFrame();
		if (milliseconds > 0 && frame >= 0 && frame != getLastFrame()) {
			setLastFrame(frame);
			performAnimation(milliseconds);
		}
	}
}