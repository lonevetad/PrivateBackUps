package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentTypesTRAn;

public class EINotJewelry extends EquipmentItem {
	private static final long serialVersionUID = 1L;

	public EINotJewelry(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name,
			AttributeModification[] baseAttributeMods) {
		super(gmrpg, et, name, baseAttributeMods);
		int ord;
		if (et == null || //
				((ord = et.ordinal()) < EquipmentTypesTRAn.Head.ordinal() // i.e. == Earrings
						|| ord > EquipmentTypesTRAn.SecodaryWeapon.ordinal())) { throw new IllegalArgumentException(
								"It's a jewelry"); }
	}

	@Override
	public void onDrop(GModalityRPG gmRPG) {}

	@Override
	public void onPickUp(GModalityRPG gmRPG) {}

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {}
}