package games.generic.controlModel;

/**
 * Define an event fired during the game.<br>
 * Could help implement the event-oriented programming.
 */
public interface GameEvent extends ObjectWIthID {

	/**
	 * The event could refers to something.<br>
	 * TODO : don't know if it's useful.
	 */
	public default Object getReferredObject() {
		return null;
	}

	/** Could be used to distinguish one type of event from others. */
	public default String getName() {
		return this.getClass().getSimpleName();
	}

	/** This event's description, useful for logging. */
	public default String getDescription() {
		return null;
	}

	/**
	 * The event will have effect to, presumably, the actual game, represented by
	 * the {@link GameModality}}.
	 */
	/* DEPRECATED since events DO NOT PERFORMS ACTIONS */
	// public void performEvent(GameModality gm);
}