package games.generic.controlModel;

import java.util.Set;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.ObjectWithID;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.GModalityHolder;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.gObj.MovingObject;
import games.generic.controlModel.gObj.ObjectInSpace;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subImpl.GModalityET;
import geometry.AbstractShape2D;

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

	public InSpaceObjectsManager getInSpaceObjectsManager();

	public GEventInterface getGEventManager();

	public void setInSpaceObjectsManager(InSpaceObjectsManager isom);

	public void setGEventInterface(GEventInterface gem);

	//

	public void addToMap(MovingObject mo);

	public void removeFromMap(MovingObject mo);

	public Set<ObjectInSpace> findInArea(AbstractShape2D shape);

	//

	// TODO todo other creature's related methods

	public <SourceDamage extends ObjectWithID> void dealsDamageTo(SourceDamage source, CreatureSimple target,
			DamageGeneric damage);

}