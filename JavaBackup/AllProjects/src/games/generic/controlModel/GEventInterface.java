package games.generic.controlModel;

import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.subImpl.GModalityET;

/**
 * Holder of ALL event-firing methods and the {@link GEventManager}.<br>
 * All of those methods are removed and separated from {@link GModality} just to
 * make it thinner and less confusing.
 * <p>
 * COMMON PATTERN of event-firing methods:
 * <code>void fireXXX({@link GModality} gm, {@link PlayerGeneric} player)</code>,
 * like {@link #firePlayerEnteringInMap(GModality, PlayerGeneric)}.
 */
public interface GEventInterface {

	public void setNewGameEventManager(GModalityET gameModality);

	public GEventManager getGameEventManager();

//	public void setGameEventManager(GameEventManager gem);

	// TODO put here ALL methods

	/** After loading the map, creating stuffs and enemies, etc, fire this event */
	public void firePlayerEnteringInMap(GModalityET gameModality, PlayerGeneric p);

	/**
	 * Put as an example, most usefull on RPG and RTS games, could be ignored and
	 * left empty if not needed. <br>
	 * TODO docs
	 */
	public <SourceDamage> void fireDamageDealtEvent(GModalityET gm, SourceDamage source, CreatureSimple target,
			DamageGeneric damage);
}