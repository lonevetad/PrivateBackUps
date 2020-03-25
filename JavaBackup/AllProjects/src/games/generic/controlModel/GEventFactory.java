package games.generic.controlModel;

import games.generic.controlModel.subImpl.GEvent;

/** Interfaccia di dubbio uso, pensata per le enumerazioni. */
public interface GEventFactory {

	public GEvent newGameEvent(Integer id, String name);
}