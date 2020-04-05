package games.generic.controlModel;

import games.generic.controlModel.gEvents.ExampleGameEvents;
import games.generic.controlModel.subimpl.GEventManagerFineGrained;
import tools.ObjectNamedID;

public interface IGEvent extends ObjectNamedID {

	/**
	 * Identify this event as different to other events, see
	 * {@link ExampleGameEvents} enumeration for examples.<br>
	 * It's used in {@link GEventManagerFineGrained} to notify only the
	 * {@link GEventObserver} interested in this event AND this method's value
	 * should the one included in {@link GEventObserver#getEventsWatching()}'s
	 * returned value.
	 */
	@Override
	public String getName();

	/** This event's description, useful for logging. */
	public default String getDescription() {
		return null;
	}
}