package games.generic.controlModel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.subimpl.TimedObjectSimpleImpl;

public interface AbilityTimedGeneric extends AbilityGeneric, TimedObjectSimpleImpl {

	@Override
	public default void executeAction(GModality gmodality) {
		performAbility(gmodality);
	}
}