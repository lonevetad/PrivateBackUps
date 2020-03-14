package games.generic.controlModel.subImpl;

import games.generic.controlModel.GameEventManager;

/** Needs to be an interface to allow multiple inheritance */
public interface IGameModalityEventBased {

	/** Override designed */
	public GameEventManager newEventManager();
}