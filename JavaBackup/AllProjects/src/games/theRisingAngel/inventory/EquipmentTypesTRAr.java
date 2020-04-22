package games.theRisingAngel.inventory;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.inventoryAbil.EquipmentType;
import tools.Comparators;

/**
 * Rings can occupy one single slot (a simple ring) or two (very big, bulky and
 * cumbersome ring, the biggest ones).
 */
public enum EquipmentTypesTRAr implements EquipmentType {
	Earrings, Head, Cloak, // cloak = mantello
	Hands, Chest, Arms, //
	Feet, Belt, Legs, //
	MainWeapon, Special, SecodaryWeapon, //
	Necklace, Bracelet, Ring//
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

	@Override
	public Integer getID() {
		return ordinal();
	}

	//

	// copied from AttributesTRAr

	public static final EquipmentTypesTRAr[] VALUES = EquipmentTypesTRAr.values();
	private static Map<String, EquipmentTypesTRAr> attTRArByName = null;

	public static EquipmentTypesTRAr getEquipTypeTRArByName(String name) {
		EquipmentTypesTRAr e;
		Map<String, EquipmentTypesTRAr> m;
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null");
		if (attTRArByName == null) {
			m = attTRArByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
			for (EquipmentTypesTRAr eq : VALUES) {
				m.put(eq.name(), eq); // using name() instead of getName() just to be faster
			}
		}
		e = attTRArByName.get(name);
		if (e == null)
			throw new IllegalArgumentException("Invalid name for AttributesTRAr: " + name);
		return e;
	}

	public static EquipmentTypesTRAr getEquipTypeTRArByIndex(int index) {
		return VALUES[index];
	}
}