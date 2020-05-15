package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.subimpl.GModalityRPG;

/**
 * Simple marker class to represents all kinds of jewelry: rings, necklaces,
 * bracelets, earrings.
 */
public class EIJewelry extends EquipmentItem {
	private static final long serialVersionUID = 1L;

	public EIJewelry(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name) {
		super(gmrpg, et, name);
		if (et == null || (et != EquipmentTypesTRAn.Earrings && et != EquipmentTypesTRAn.Necklace
				&& et != EquipmentTypesTRAn.Ring)) {
			throw new IllegalArgumentException("Not a really jewelry");
		}
	}

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
		// do nothing, yet done in loading time
	}
}