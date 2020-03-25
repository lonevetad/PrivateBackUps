package games.theRisingAngel.inventory;

import games.generic.controlModel.inventory.EquipmentItem;

public abstract class EINotJewelry extends EquipmentItem {

	public EINotJewelry(EquipmentTypesTRAr et) {
		super(et);
		int ord;
		if (et == null || //
				((ord = et.ordinal()) < EquipmentTypesTRAr.Head.ordinal() // i.e. == Earrings
						|| ord > EquipmentTypesTRAr.SecodaryWeapon.ordinal())) {
			throw new IllegalArgumentException("It's a jewelry");
		}

	}
}