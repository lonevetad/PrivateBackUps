package games.generic.controlModel.misc;

/**
 * Some implementations of {@link CreatureAttributes} could compute some
 * attributes depending on other attributes. This interface generalize it.
 */
public interface CreatureAttributesBonusesCalculator {
	public CreatureAttributes getCreatureAttributesSet();

	public void setCreatureAttributesSet(CreatureAttributes creatureAttributesSet);

	public default int getBonusForValue(int index) {
		return 0;
	}
}