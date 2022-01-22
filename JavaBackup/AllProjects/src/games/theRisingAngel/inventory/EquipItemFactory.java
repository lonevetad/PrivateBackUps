package games.theRisingAngel.inventory;

import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentTypesTRAn;

public interface EquipItemFactory {
	public static enum DefaultEIF implements EquipItemFactory {
		JewelryFactory((g, et, name, bmod) -> { return new EIJewelry(g, et, name, bmod); }), //
		NonJewelryFacory((g, et, name, bmod) -> { return new EINotJewelry(g, et, name, bmod); }), //
		RingFactory((g, et, name, bmod) -> { return new EIRing(g, name, bmod); }), //
		WeaponFactory((g, et, name, bmod) -> {
			return new EIWeapon(g, et, name, bmod) {
				private static final long serialVersionUID = 1L;
			};
		});

		final EquipItemFactory fact;

		DefaultEIF(EquipItemFactory f) { this.fact = f; }

		@Override
		public EquipmentItem newEquipItem(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name,
				AttributeModification[] baseAttributeMods) {
			return fact.newEquipItem(gmrpg, et, name, baseAttributeMods);
		}
	}

	//

	public EquipmentItem newEquipItem(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name,
			AttributeModification[] baseAttributeMods);
}