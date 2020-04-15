package games.generic.controlModel.subimpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;

public interface AbilityTimedGeneric extends AbilityGeneric, TimedObjectSimpleImpl {

	@Override
	public default void executeAction(GModality gmodality) {
		performAbility(gmodality);
	}
}