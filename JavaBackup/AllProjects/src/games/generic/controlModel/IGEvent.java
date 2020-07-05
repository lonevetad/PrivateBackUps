package games.generic.controlModel;

import games.generic.controlModel.gEvents.ExampleGameEvents;
import games.generic.controlModel.subimpl.GEventManagerFineGrained;
import games.generic.controlModel.subimpl.GModalityET;
import tools.ObjectNamedID;

public interface IGEvent extends ObjectNamedID {

	/**
	 * Identify this event as different to other events, see
	 * {@link ExampleGameEvents} enumeration for examples.<br>
	 * It's used in {@link GEventManagerFineGrained} to notify only the
	 * {@link GEventObserver} interested in this event AND this method's value
	 * should the one included in {@link GEventObserver#getEventsWatching()}'s
	 * returned value.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public String getName();

	/** This event's description, useful for logging. */
	public default String getDescription() { return null; }

	/**
	 * Long explanation:<br>
	 * Usually events are put in a queue (inside a {@link GEventManager}) and are
	 * processed all together, at the end of the implementation of
	 * {@link GModalityET#doOnEachCycle(int)} (just remember that it's called inside
	 * {@link GModalityET#runSingleGameCycle()}).<br>
	 * Some events requires to be processed immediately (i.e.: notify all
	 * {@link GEventObserver}) because they are urgent in some way. Some examples
	 * are damage event, destruction event, heal event.<br>
	 * So, in short, returns :
	 * <ul>
	 * <li><code>true</code> if the event is urgent and requires to be processed
	 * immediately</li>
	 * <li><code>false</code> if it's not urgent, so can be put in he queue as
	 * usually</li>
	 * </ul>
	 * <p>
	 * Short explanation:<br>
	 * Mark this event as urgent if <code>true</code> to force to process it as i's
	 * fired.
	 * 
	 * @return <code>true</code> if the event is urgent, <code>false</code>
	 *         otherwise
	 */
	public default boolean isRequirigImmediateProcessing() { return false; }

	/** Alias for {@link #isRequirigImmediateProcessing()}. */
	public default boolean isUrgent() {
		return isRequirigImmediateProcessing();
	}

	/**
	 * Used by someone like {@link GEventManager} to perform operations upon
	 * processing finished.
	 */
	public default void onProcessingEnded() {}
}