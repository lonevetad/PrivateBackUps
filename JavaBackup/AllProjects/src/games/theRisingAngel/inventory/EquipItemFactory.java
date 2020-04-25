package games.theRisingAngel.inventory;

import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.subimpl.GModalityRPG;

public interface EquipItemFactory {
	public static final EquipItemFactory JewelryFactory = (g, et, name) -> {
		return new EIJewelry(g, et, name);
	}, NonJewelryFacory = (g, et, name) -> {
		return new EINotJewelry(g, et, name);
	};

	//

	public EquipmentItem newEquipItem(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name);

}