package games.generic.controlModel;

import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.gObj.GModalityHolder;
import games.generic.controlModel.gObj.ObjectInSpace;
import games.generic.controlModel.subimpl.GEvent;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.IGameModalityEventBased;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;

/**
 * Handler for objects in game, that can be placed in a kind of "space" concept.
 * <br>
 * Wraps a {@link InSpaceObjectsManager} instance, delegating to it the real
 * object management, to let {@link GEvent}s to be fired through subclasses of
 * {@link GModality} returned by {@link #getGameModality()}.
 */
public interface GObjectsInSpaceManager extends GModalityHolder {

	/** Delegates there the real objects management */
	public InSpaceObjectsManager<Double> getOIMManager();

	//

	public boolean contains(ObjectInSpace o);

	/**
	 * To be intended as "teleport". <br>
	 * <code>
		if(! contains(o)){ addObject(true, o); }
		else if(to==null)removeObject(false, o);
		// else then TELEPORT
	 * </code>
	 */
	public boolean moveObject(ObjectInSpace o, Object from, Object to);

	public default boolean addObject(ObjectInSpace o) {
		return addObject(true, o);
	}

	/**
	 * Set the object's location before adding it.
	 * 
	 * @param fireEvent specify if a "add" event should be fired
	 */
	public default boolean addObject(boolean fireEvent, ObjectInSpace o) {
		boolean added;
		added = getOIMManager().add(o);
		if (added && fireEvent) {
			GModality gm;
			gm = this.getGameModality();
			if (gm instanceof IGameModalityEventBased) {
				IGameModalityEventBased gme;
				gme = (IGameModalityEventBased) gm;
				gme.getEventInterface().fireGameObjectAdded((GModalityET) gme, o);
			}
		}
		return added;
	}

	public default boolean removeObject(ObjectInSpace o) {
		return removeObject(true, o);
	}

	/**
	 * @param fireEvent specify if a "remove" event should be fired
	 */
	public default boolean removeObject(boolean fireEvent, ObjectInSpace o) {
		boolean removed;
		removed = getOIMManager().remove(o);
		if (removed && fireEvent) {
			GModality gm;
			gm = this.getGameModality();
			if (gm instanceof IGameModalityEventBased) {
				IGameModalityEventBased gme;
				gme = (IGameModalityEventBased) gm;
				gme.getEventInterface().fireGameObjectRemoved((GModalityET) gme, o);
			}
		}
		return removed;
	}

	public default <E extends ObjectLocated> Set<ObjectLocated> findAll(AbstractShape2D areaToLookInto,
			Predicate<E> objectFilter) {
		return getOIMManager().fetch(areaToLookInto, objectFilter);
	}

	/** Queris all objects located in the given area, if any. */
	public default Set<ObjectLocated> findAll(AbstractShape2D areaToLookInto) {
		return this.findAll(areaToLookInto, null);
	}

	/**
	 * Refers to
	 * {@link InSpaceObjectsManager#findInPath(AbstractShape2D, Predicate, List) }.
	 */
	public default Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		return getOIMManager().findInPath(areaToLookInto, objectFilter, path);
	}

	/**
	 * Refers to {@link findInPath(AbstractShape2D, Predicate, List) }, giving to it
	 * <code>null</code> as {@link Predicate}.
	 */
	public default Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, List<Point> path) {
		return findInPath(areaToLookInto, null, path);
	}
}