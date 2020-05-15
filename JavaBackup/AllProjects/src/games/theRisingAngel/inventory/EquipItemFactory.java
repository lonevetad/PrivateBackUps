package games.theRisingAngel.inventory;

import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.subimpl.GModalityRPG;

public interface EquipItemFactory {
	public static enum DefaultEIF implements EquipItemFactory {
		JewelryFactory((g, et, name) -> { return new EIJewelry(g, et, name); }), //
		NonJewelryFacory((g, et, name) -> { return new EINotJewelry(g, et, name); }), //
		RingFactory((g, et, name) -> { return new EIRing(g, name); }), //
		WeaponFactory((g, et, name) -> {
			return new EIWeapon(g, et, name) {
				private static final long serialVersionUID = 1L;
			};
		});

		final EquipItemFactory fact;

		DefaultEIF(EquipItemFactory f) { this.fact = f; }

		@Override
		public EquipmentItem newEquipItem(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name) {
			return fact.newEquipItem(gmrpg, et, name);
		}
	}

	//

	public EquipmentItem newEquipItem(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name);

}