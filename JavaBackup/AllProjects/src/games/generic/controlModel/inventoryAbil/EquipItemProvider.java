package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.GameObjectsProvider;

public class EquipItemProvider extends GameObjectsProvider<EquipmentItem> {
	public static final String NAME = "EquipP";

	public EquipItemProvider() {
	}

	public EquipmentItem getEquipByName(GModality gm, String name) {
		if (gm == null || name == null)
			return null;
//		return getObjByName(name).newInstance(gm);
		return getNewObjByName(gm, name);
	}
}