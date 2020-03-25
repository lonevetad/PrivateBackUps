package games.generic.controlModel;

import games.generic.controlModel.eventsGame.ExampleGameEvents;
import games.generic.controlModel.subImpl.GEventManagerFineGrained;

public interface IGEvent extends ObjectWithID {

	/**
	 * Identify this event as different to other types, see
	 * {@link ExampleGameEvents} enumeration for examples.<br>
	 * It's used in {@link GEventManagerFineGrained} to notify only the
	 * {@link GEventObserver} interested in this event AND this method's value
	 * should the one included in {@link GEventObserver#getEventsWatching()}'s
	 * returned value.
	 */
	public abstract String getType();

	/**
	 * Could be used to distinguish one type of event from others, especially in
	 * fine-level differences. But {@link #getType()} is preferred.
	 */
	public String getName();

	/** This event's description, useful for logging. */
	public default String getDescription() {
		return null;
	}
}