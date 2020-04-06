package games.generic.controlModel.inventoryAbil;

import java.util.function.Predicate;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.GObjectsInSpaceManager;
import geometry.ObjectLocated;

public interface AbilityTargetingGObjInMap extends AbilityGeneric {
	/**
	 * Returns a filter that defines what instances will be affected by this ability
	 * and what won't.<br>
	 * Used in
	 * {@link InSpaceObjectsManager#fetch(geometry.AbstractShape2D, Predicate)} by
	 * {@link GObjectsInSpaceManager}
	 */
	public abstract Predicate<ObjectLocated> getTargetsFilter();
}