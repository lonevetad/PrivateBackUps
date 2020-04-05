package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import tools.ObjectNamedID;
import tools.ObjectWithID;

public interface AbilityGeneric extends ObjectNamedID {
	public ObjectWithID getOwner();

	public void setOwner(ObjectWithID owner);

	/** Perform the ability */
	public void performAbility(GModality gm);

	/**
	 * Clone the ability.
	 */
//	public AbilityGeneric cloneAbility();
}