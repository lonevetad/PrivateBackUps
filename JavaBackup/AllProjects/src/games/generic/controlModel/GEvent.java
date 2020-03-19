package games.generic.controlModel;

import games.generic.UniqueIDProvider;
import games.generic.controlModel.eventsGame.ExampleGameEvents;

/**
 * Define an event fired during the game.<br>
 * The event MUST hold all informations needed to work with (except for
 * {@link GModality} instance, it's usually not needed)..
 * <p>
 * Could help implement the event-oriented programming.
 */
public abstract class GEvent implements ObjectWIthID {
	protected final Integer ID;

	public GEvent() {
		this(UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID());
	}

	public GEvent(Integer iD) {
		super();
		ID = iD;
	}

	/**
	 * This is an INSTANCE identifier, i.e. two events of the same type are
	 * different thanks to this
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Integer getID() {
		return ID;
	}

	/**
	 * Identify this event as different to other types, see
	 * {@link ExampleGameEvents} enumeration for examples.
	 */
	public abstract String getType();

	/**
	 * Could be used to distinguish one type of event from others, especially in
	 * fine-level differences. But {@link #getType()} is preferred.
	 */
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/** This event's description, useful for logging. */
	public String getDescription() {
		return null;
	}

	/**
	 * The event will have effect to, presumably, the actual game, represented by
	 * the {@link GameModality}}.
	 */
	/* DEPRECATED since events DO NOT PERFORMS ACTIONS */
	// public void performEvent(GameModality gm);
}