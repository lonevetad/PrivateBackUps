package games.generic.controlModel;

public interface GameEventFactory {

	public GameEvent newGameEvent(Integer id, String name);
}