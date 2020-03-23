package games.theRisingAngel;

public class EINotJewelry extends EquipmentItem {

	public EINotJewelry(EquipmentTypes et) {
		super(et);
		int ord;
		if (et == null || //
				((ord = et.ordinal()) < EquipmentTypes.Head.ordinal() // i.e. == Earrings
						|| ord > EquipmentTypes.SecodaryWeapon.ordinal())) {
			throw new IllegalArgumentException("It's a jewelry");
		}

	}

}
