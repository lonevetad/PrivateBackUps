package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.GameObjectsProvider;

public class AbilitiesProvider extends GameObjectsProvider<AbilityGeneric> {

	/** Should be preferred over {@link #getObjIdentifiedByID(Integer)}. */
	public AbilityGeneric getAbilityByName(GModality gm, String name) {
		if (name == null)
			return null;
//		return getObjByName(name).newInstance(gm);
		return getNewObjByName(gm, name);
	}

	public int getAbilitiesCount() {
		return getObjectsIdentifiedCount();
	}
}