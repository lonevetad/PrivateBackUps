package games.generic.controlModel.subImpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.TimedObject;

/**
 * Object performing a defined action as times goes on, at a defined time
 * threshold, defined by {@link #getTimeThreshold()}.
 */
public interface TimedObjectSimpleImpl extends TimedObject {

	public long getAccumulatedTimeElapsed();

	public void setAccumulatedTimeElapsed(long newAccumulated);

	/**
	 * Get the time threshold at which, every time this amount of time is elapsed,
	 * perform the action defined as {@link #executeAction(GModality)}. <br>
	 * Could modify the status determining {@link #getTimeThreshold()}.
	 */
	public long getTimeThreshold();

	/**
	 * Execute the given action. Could modify the status determining
	 * {@link #getTimeThreshold()} during the execution.<br>
	 * Define this method instead of {@link #executeAction(GModality)}.
	 */
	public void executeAction(GModality modality);

	/**
	 * A default implementation is provided, redefine
	 * {@link #executeAction(GModality)} instead.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public default void act(GModality modality, int milliseconds) {
		if (milliseconds > 0) {
			setAccumulatedTimeElapsed(milliseconds + getAccumulatedTimeElapsed());
			while(getAccumulatedTimeElapsed() > getTimeThreshold()) {
				setAccumulatedTimeElapsed(getAccumulatedTimeElapsed() - getTimeThreshold());
				executeAction(modality);
			}
		}
	}
}