package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.ObjGModalityBasedProvider;

public class AbilitiesProvider extends ObjGModalityBasedProvider<AbilityGeneric> {
//	public void addAbility(AbilityGeneric a) {
//		addObjIdentified(a);
//		this.addObj(name, gm);
//	}

	/** Should be preferred over {@link #getObjIdentifiedByID(Integer)}. */
	public AbilityGeneric getAbilityByName(GModality gm, String name) {
		if (name == null)
			return null;
		return getObjIdentifiedByName(name).newInstance(gm);
	}

	public int getAbilitiesCount() {
		return getObjectsIdentifiedCount();
	}
}