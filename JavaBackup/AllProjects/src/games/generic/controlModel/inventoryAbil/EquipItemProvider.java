package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.ObjGModalityBasedProvider;

public class EquipItemProvider extends ObjGModalityBasedProvider<EquipmentItem> {

	public EquipItemProvider() {
	}

	public EquipmentItem getEquipByName(GModality gm, String name) {
		if (gm == null || name == null)
			return null;
		return getObjIdentifiedByName(name).newInstance(gm);
	}
}