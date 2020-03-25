package games.theRisingAngel;

import games.generic.controlModel.inventory.EquipmentType;

/**
 * Rings can occupy one single slot (a simple ring) or two (very big, bulky and
 * cumbersome ring, the biggest ones).
 */
public enum EquipmentTypesTRAr implements EquipmentType {
	Earrings, Head, Cloak, // cloak = mantello
	Hands, Chest, Arms, //
	Feet, Belt, Legs, //
	MainWeapon, Special, SecodaryWeapon, //
	Necklace, Bracelet, Rings//
	;
	public static final int HANDS_AMOUNT = 2, FINGERS_EACH_HAND = 5, RING_SLOTS_EACH_FINGERS = 2, //
			TOTAL_RINGS_SLOTS_AMOUNT, TOTAL_FINGERS_AMOUNT = (HANDS_AMOUNT * FINGERS_EACH_HAND), //
			BRACELET_AMOUNT = 2, NECKLACE_AMOUNT = 3, TOTAL_AMOUNT_EQUIPMENTS_WEARABLES;

	static {
		TOTAL_RINGS_SLOTS_AMOUNT = (TOTAL_FINGERS_AMOUNT * RING_SLOTS_EACH_FINGERS);
		TOTAL_AMOUNT_EQUIPMENTS_WEARABLES = (EquipmentTypesTRAr.values().length - 3) // 3 = Rings + Necklace + Bracelet
				+ TOTAL_RINGS_SLOTS_AMOUNT + BRACELET_AMOUNT + NECKLACE_AMOUNT; // = 27
	}

	@Override
	public int getIndex() {
		return ordinal();
	}

	@Override
	public String getName() {
		return name();
	}
}