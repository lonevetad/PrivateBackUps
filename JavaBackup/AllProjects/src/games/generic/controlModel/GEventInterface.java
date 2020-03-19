package games.generic.controlModel;

import games.generic.controlModel.player.PlayerInGame_Generic;

/**
 * Holder of ALL event-firing methods and the {@link GEventManager}.<br>
 * All of those methods are removed and separated from {@link GModality} just
 * to make it thinner and less confusing.
 * <p>
 * COMMON PATTERN of event-firing methods:
 * <code>void fireXXX({@link GModality} gm, {@link PlayerInGame_Generic} player)</code>,
 * like {@link #firePlayerEnteringInMap(GModality, PlayerInGame_Generic)}.
 */
public interface GEventInterface {

	public void setNewGameEventManager(GModality gameModality);

	public GEventManager getGameEventManager();

//	public void setGameEventManager(GameEventManager gem);

	// TODO put here ALL methods

	/** After loading the map, creating stuffs and enemies, etc, fire this event */
	public void firePlayerEnteringInMap(GModality gameModality, PlayerInGame_Generic p);
}