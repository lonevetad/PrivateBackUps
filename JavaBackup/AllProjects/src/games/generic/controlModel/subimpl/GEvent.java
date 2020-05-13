package games.generic.controlModel.subimpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import tools.UniqueIDProvider;

/**
 * Define an event fired during the game.<br>
 * The event MUST hold all informations needed to work with (except for
 * {@link GModality} instance, it's usually not needed)..
 * <p>
 * Could help implement the event-oriented programming.
 */
public abstract class GEvent implements IGEvent {
	private static final long serialVersionUID = -53224778410585340L;
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

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * The event will have effect to, presumably, the actual game, represented by
	 * the {@link GameModality}}.
	 */
	/* DEPRECATED since events DO NOT PERFORMS ACTIONS */
	// public void performEvent(GameModality gm);
}