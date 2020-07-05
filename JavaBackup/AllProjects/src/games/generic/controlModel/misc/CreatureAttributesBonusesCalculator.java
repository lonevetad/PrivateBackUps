package games.generic.controlModel.misc;

/**
 * Some implementations of {@link CreatureAttributes} could compute some
 * attributes depending on other attributes. This interface generalize it.
 */
public interface CreatureAttributesBonusesCalculator {
	public CreatureAttributes getCreatureAttributesSet();

	public void setCreatureAttributesSet(CreatureAttributes creatureAttributesSet);

	/** Utility method useful when caching values */
	public default void markCacheAsDirty() {
	}

	/** Calculate the bonus for a given index's value */
	public default int getBonusFor(int index) {
		return 0;
	}
}