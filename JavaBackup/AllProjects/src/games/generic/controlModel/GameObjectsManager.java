package games.generic.controlModel;

import java.util.Set;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.GModalityHolder;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.gObj.MovingObject;
import games.generic.controlModel.gObj.ObjectInSpace;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subImpl.GModalityET;
import geometry.AbstractShape2D;

/**
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
 * <li>{@link MovingObject}</li>
 * <li>{@link LivingObject}</li>
 * <li>{@link ObjectInSpace}</li>
 * <li>{@link GEventObserver}</li>
 * </ul>
 */
public interface GameObjectsManager extends GModalityHolder {

	public InSpaceObjectsManager getInSpaceObjectsManager();

	public GEventManager getGEventManager();

	public void setGEventManager(InSpaceObjectsManager isom);

	public void setInSpaceObjectsManager(GEventManager gem);

	//

	public void addToMap(MovingObject mo);

	public void removeFromMap(MovingObject mo);

	public Set<ObjectInSpace> findInArea(AbstractShape2D shape);

	//

	public <SourceDamage> void dealsDamageTo(SourceDamage source, CreatureSimple target, DamageGeneric damage);

	//

	public <SourceDamage> void fireDamageDealtEvent(GModalityET gm, SourceDamage source, CreatureSimple target,
			DamageGeneric damage);
}