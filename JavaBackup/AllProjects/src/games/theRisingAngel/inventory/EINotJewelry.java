package games.theRisingAngel.inventory;

import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.subimpl.GModalityRPG;

public abstract class EINotJewelry extends EquipmentItem {
	private static final long serialVersionUID = 1L;

	public EINotJewelry(GModalityRPG gmrpg, EquipmentTypesTRAr et, String name) {
		super(gmrpg, et, name);
		int ord;
		if (et == null || //
				((ord = et.ordinal()) < EquipmentTypesTRAr.Head.ordinal() // i.e. == Earrings
						|| ord > EquipmentTypesTRAr.SecodaryWeapon.ordinal())) {
			throw new IllegalArgumentException("It's a jewelry");
		}

	}
}