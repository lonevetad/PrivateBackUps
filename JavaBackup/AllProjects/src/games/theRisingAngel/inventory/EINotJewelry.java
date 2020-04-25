package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.subimpl.GModalityRPG;

public class EINotJewelry extends EquipmentItem {
	private static final long serialVersionUID = 1L;

	public EINotJewelry(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name) {
		super(gmrpg, et, name);
		int ord;
		if (et == null || //
				((ord = et.ordinal()) < EquipmentTypesTRAn.Head.ordinal() // i.e. == Earrings
						|| ord > EquipmentTypesTRAn.SecodaryWeapon.ordinal())) {
			throw new IllegalArgumentException("It's a jewelry");
		}
	}

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
		// do nothing
	}
}