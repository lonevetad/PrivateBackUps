package games.generic.controlModel;

/** Interfaccia di dubbio uso, pensata per le enumerazioni. */
public interface GEventFactory {

	public GEvent newGameEvent(Integer id, String name);
}