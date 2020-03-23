package games.theRisingAngel;

/**
 * Simple maker abstract class to represents all kinds of jewelry: rings,
 * necklaces, bracelets, earrings.
 */
public abstract class EIJewelry extends EquipmentItem {

	public EIJewelry(EquipmentTypes et) {
		super(et);
		if (et == null
				|| (et != EquipmentTypes.Earrings && et != EquipmentTypes.Necklace && et != EquipmentTypes.Rings)) {
			throw new IllegalArgumentException("Not a really jewelry");
		}
	}
}