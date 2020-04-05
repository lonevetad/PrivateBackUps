package games.theRisingAngel.inventory;

import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.subimpl.GModalityRPG;

/**
 * Simple maker abstract class to represents all kinds of jewelry: rings,
 * necklaces, bracelets, earrings.
 */
public abstract class EIJewelry extends EquipmentItem {
	private static final long serialVersionUID = 1L;

	public EIJewelry(GModalityRPG gmrpg, EquipmentTypesTRAr et, String name) {
		super(gmrpg, et, name);
		if (et == null || (et != EquipmentTypesTRAr.Earrings && et != EquipmentTypesTRAr.Necklace
				&& et != EquipmentTypesTRAr.Rings)) {
			throw new IllegalArgumentException("Not a really jewelry");
		}
	}
}