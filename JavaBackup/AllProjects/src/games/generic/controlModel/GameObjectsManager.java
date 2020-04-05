package games.generic.controlModel;

import java.awt.Point;
import java.util.Set;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.GModalityHolder;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.gObj.MovingObject;
import games.generic.controlModel.gObj.ObjectInSpace;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subimpl.GModalityET;
import geometry.AbstractShape2D;
import tools.ObjectWithID;

/**
 * One of the core classes
 * <p>
 * Manages all games objects, performing actions based on their types, like:
 * <ul>
 * <li>For space-based object, like {@link ObjectInSpace}, they could be added,
 * moved, removed, queried.</li>
 * <li>For life-based object, like {@link LivingObject}, then they could inflict
 * and receive damage, heal, destroy/kill, spawn, revive, transform</li>
 * <li>For event-based object, like {@link GEventObserver}, then refers to the
 * instance of {@link GEventManager} provided by {@link #getGEventManager()}
 * (which is taken from {@link GModalityET#getEventManager()}).</li>
 * </ul>
 * Usually this class is used inside RPG or RTS games, but it's not mandatory.
 * <p>
 * Kind of object handled:
 * <ul>
 * <li>{@link MovingObject} (through {@link InSpaceObjectsManager}
 * instance)</li>
 * <li>{@link LivingObject} (through methods like
 * {@link #dealsDamageTo(Object, CreatureSimple, DamageGeneric)}).</li>
 * <li>{@link ObjectInSpace} (as n°1)</li>
 * <li>{@link GEventObserver} (through {@link GEventInterface} instance)</li>
 * </ul>
 */
public interface GameObjectsManager extends GModalityHolder {

	/** Returns the delegator who manage who manages the "space" concept. */
	public GObjectsInSpaceManager getGObjectInSpaceManager();

	/** Returns the delegator who manage the "event handling" concept. */
	public GEventInterface getGEventManager();

	public void setGObjectsInSpaceManager(GObjectsInSpaceManager isom);

	public void setGEventInterface(GEventInterface gem);

	//

	public default void addToSpace(MovingObject mo) {
		getGObjectInSpaceManager().addObject(mo);
	}

	public void removeFromSpace(MovingObject mo);

	public Set<ObjectInSpace> findInArea(AbstractShape2D shape);

	/**
	 * Teleport the given object to a new given location.<br>
	 * Checks about the new location and all kinds of object's tracking and
	 * memorizing are delegated to {@link GObjectsInSpaceManager} returned by
	 * {@link #getGObjectInSpaceManager()}.<br>
	 * On default implementation, it just calls
	 * {@link #removeFromSpace(MovingObject)} and then
	 * {@link #addToSpace(MovingObject)}.
	 */
	public default void moveTo(MovingObject mo, Point newLocation) {
		if (mo == null)
			throw new IllegalArgumentException("The object to move is null");
		removeFromSpace(mo);
		mo.setLocation(newLocation);
		addToSpace(mo);
	}

	//

	// TODO todo other creature's related methods

	public <SourceDamage extends ObjectWithID> void dealsDamageTo(SourceDamage source, CreatureSimple target,
			DamageGeneric damage);

}