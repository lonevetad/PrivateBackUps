package games.generic.controlModel.player;

import games.generic.controlModel.gObj.CurrencyHolder;
import games.generic.controlModel.gObj.creature.BaseCreatureRPG;
import games.generic.controlModel.misc.CreatureAttributes;

/** Base definition of a "player" of a "Rule Play Game" (RPG) */
public interface BasePlayerRPG extends PlayerWithExperience, BaseCreatureRPG, CurrencyHolder {

	/**
	 * After leveling up, a certain amount of points are aquired and should be spent
	 * o increase values of {@link CreatureAttributes}. This is that counter.
	 */
	public int getAttributePointsLeftToApply();

	/** See {@link #getAttributePointsLeftToApply()} */
	public void setAttributePointsLeftToApply(int attributePointsLeftToApply);

	public default void increasettributePointsLeftBy(int delta) {
		setAttributePointsLeftToApply(getAttributePointsLeftToApply() + delta);
	}
}