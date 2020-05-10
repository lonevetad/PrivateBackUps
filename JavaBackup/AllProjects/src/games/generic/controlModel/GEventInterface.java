package games.generic.controlModel;

import java.awt.Point;

import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.subimpl.GModalityET;
import geometry.ObjectLocated;

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

	public void fireGameObjectAdded(GModalityET gameModality, ObjectLocated o);

	public void fireGameObjectRemoved(GModalityET gameModality, ObjectLocated o);

	/**
	 * Fire an event representing the movement for an object from a point to its
	 * (new) location.
	 */
	public void fireGameObjectMoved(GModalityET gameModality, Point previousLocation, ObjectLocated o);

	/** After loading the map, creating stuffs and enemies, etc, fire this event */
	public void firePlayerEnteringInMap(GModalityET gameModality, PlayerGeneric p);

	/**
	 * Put as an example, most useful on RPG and RTS games, could be ignored and
	 * left empty if not needed. <br>
	 * TODO docs
	 */
	public EventDamage fireDamageDealtEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage);

	public EventDamage fireCriticalDamageDealtEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage);
}