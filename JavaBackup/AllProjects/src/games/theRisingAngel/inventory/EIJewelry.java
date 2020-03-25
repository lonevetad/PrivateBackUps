package games.theRisingAngel.inventory;

import games.generic.controlModel.inventory.EquipmentItem;

/**
 * Simple maker abstract class to represents all kinds of jewelry: rings,
 * necklaces, bracelets, earrings.
 */
public abstract class EIJewelry extends EquipmentItem {

	public EIJewelry(EquipmentTypesTRAr et) {
		super(et);
		if (et == null || (et != EquipmentTypesTRAr.Earrings && et != EquipmentTypesTRAr.Necklace
				&& et != EquipmentTypesTRAr.Rings)) {
			throw new IllegalArgumentException("Not a really jewelry");
		}
	}
}