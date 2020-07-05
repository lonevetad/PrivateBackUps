package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.subimpl.TimedObjectSimpleImpl;

public interface AbilityTimedGeneric extends AbilityGeneric, TimedObjectSimpleImpl {

	@Override
	public default void executeAction(GModality gmodality) { performAbility(gmodality); }

	@Override
	public default void resetAbility() { setAccumulatedTimeElapsed(0); }
}