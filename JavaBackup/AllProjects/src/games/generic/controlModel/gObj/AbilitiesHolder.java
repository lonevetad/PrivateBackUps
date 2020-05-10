package games.generic.controlModel.gObj;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import games.generic.controlModel.inventoryAbil.AbilityGeneric;

/**
 * Marks the class as having a set of abilities. It's implemented as a
 * {@link Map} to make easier to check if this instance has a particular ability
 * and/or to remove it.
 */
public interface AbilitiesHolder {
	/**
	 * See this class documentation to understand why this is a {@link Map} and not
	 * a {@link Set}.
	 */
	public Map<String, AbilityGeneric> getAbilities();

	public default void forEachAbilities(BiConsumer<String, AbilityGeneric> action) {
		getAbilities().forEach(action);
	}
}