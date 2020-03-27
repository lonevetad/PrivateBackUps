package games.generic.controlModel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.AbilityGeneric;
import games.generic.controlModel.subImpl.TimedObjectSimpleImpl;

public interface AbilityTimedGeneric extends AbilityGeneric, TimedObjectSimpleImpl {

	@Override
	public default void executeAction(GModality gmodality) {
		performAbility(gmodality);
	}
}