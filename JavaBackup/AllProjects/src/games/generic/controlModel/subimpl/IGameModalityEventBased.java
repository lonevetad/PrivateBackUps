package games.generic.controlModel.subimpl;

import games.generic.controlModel.GEventInterface;

/**
 * Specify that this Game <br>
 * Needs to be an interface to allow multiple inheritance
 */
public interface IGameModalityEventBased {

	/** Override designed */
	public GEventInterface newEventInterface();

	public GEventInterface getEventInterface();

	public void setEventInterface(GEventInterface ei);

}