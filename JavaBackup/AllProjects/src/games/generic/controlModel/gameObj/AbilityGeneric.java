package games.generic.controlModel.gameObj;

import games.generic.ObjectNamedID;
import games.generic.ObjectWithID;
import games.generic.controlModel.GModality;

public interface AbilityGeneric extends ObjectNamedID {
	public ObjectWithID getOwner();

	public void setOwner(ObjectWithID owner);

	/** Perform the ability */
	public void performAbility(GModality gm);
}