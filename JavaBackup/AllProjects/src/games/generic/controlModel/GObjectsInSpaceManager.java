package games.generic.controlModel;

import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.SetMapped;
import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.gObj.GModalityHolder;
import games.generic.controlModel.gObj.ObjectInSpace;
import games.generic.controlModel.subimpl.GEvent;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.IGameModalityEventBased;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.ObjectWithID;

/**
 * One of the core classes.
 * <p>
 * "Space"-focused handler for objects in game.<br>
 * Wraps a {@link InSpaceObjectsManager} instance, delegating to it the real
 * object management, to let {@link GEvent}s to be fired through subclasses of
 * {@link GModality} returned by {@link #getGameModality()}.
 */
public interface GObjectsInSpaceManager extends GModalityHolder, GObjectsHolder {

	public static final String OISM_NAME = "oism";

	/** Delegates there the real objects management */
	public InSpaceObjectsManager<Double> getOIMManager();

	//

	/**
	 * Used by {@link GObjectsHolder} (like {@link GModel}) to add this instance of
	 * {@link GObjectsHolder}.
	 */
	public default String getNameGObjHolder() { return OISM_NAME; }

	/**
	 * The "space" concept could not be atomic and so could be divided in smaller
	 * parts, like the <i>meter</i> could be divided in <i>centimeters</i> or even
	 * <i>millimeters</i>. See the "return" section for further informations.
	 * 
	 * @return The amount of <i>sub-units</i> that each <i>"space macro-unit"</i> is
	 *         subdivided into. A number greater than one means that the space has a
	 *         concept of <i>macro sections</i>, like <i>meter</i> has
	 *         <i>centimeters</i>. Just <code>1</code> means that the space is at
	 *         its finest granularity. A zero or negative number means <i>the space
	 *         can be infinitively subdivided</i>.
	 */
	public int getSpaceSubunitsEachMacrounits();

	//

	public boolean containsObject(ObjectInSpace o);

	/**
	 * To be intended as "teleport" from the object's location to a destination
	 * point. <br>
	 * <code>
		if(! contains(o)){ addObject(true, o); }
		else if(to==null)removeObject(false, o);
		// else then TELEPORT
	 * </code>
	 */
	public default boolean moveObject(ObjectInSpace o, Point to) {
		if (o == null)
			return false;
		if (!this.containsObject(o)) {
			o.setLocation(to);
			this.addObject(true, o);
		} else if (to == null) {
			this.removeObject(true, o);
		} else {
			GModality gm;
			Point prevLocation;
			prevLocation = new Point(o.getLocation());
			this.removeObject(false, o);
			o.setLocation(to);
			this.addObject(false, o);
			gm = this.getGameModality();
			if (gm instanceof IGameModalityEventBased) {
				IGameModalityEventBased gme;
				gme = (IGameModalityEventBased) gm;
				gme.getEventInterface().fireGameObjectMoved((GModalityET) gm, prevLocation, o);
			}
		}
		return true;
	}

	public default boolean addObject(ObjectInSpace o) { return addObject(true, o); }

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

	public default boolean removeObject(ObjectInSpace o) { return removeObject(true, o); }

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

	//

	//

	// trom GObjHolder

	@Override
	public default Set<ObjectWithID> getObjects() {
		SetMapped<ObjectLocated, ObjectWithID> sm;
		sm = new SetMapped<>(this.getOIMManager().getAllObjectLocated(), ol -> { return (ObjectWithID) ol; });
		sm.setReverseMapper(owid -> {
			if (owid instanceof ObjectLocated)
				return (ObjectLocated) owid;
			throw new IllegalArgumentException("Cannot add/remove a non-ObjectLocated to/from original set");
		});
		return sm;
	}

	@Override
	public default boolean add(ObjectWithID o) {
		if (o == null || (!(o instanceof ObjectInSpace)))
			return false;
		return addObject((ObjectInSpace) o);
	}

	@Override
	public default boolean remove(ObjectWithID o) {
		if (o == null || (!(o instanceof ObjectInSpace)))
			return false;
		return removeObject((ObjectInSpace) o);
	}

	@Override
	public default boolean contains(ObjectWithID o) {
		if (o == null || (!(o instanceof ObjectInSpace)))
			return false;
		return containsObject((ObjectInSpace) o);
	}

	@Override
	public default boolean removeAll() { return this.getOIMManager().removeAllObjects(); }

	@Override
	public default ObjectWithID get(Integer id) { return this.getOIMManager().getObjectLocated(id); }

	@Override
	public default void forEach(Consumer<ObjectWithID> action) { this.getObjects().forEach(action); }

	//

	//

	//

	public default Set<ObjectLocated> findAll(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter) {
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