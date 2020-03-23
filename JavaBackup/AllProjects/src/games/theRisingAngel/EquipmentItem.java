package games.theRisingAngel;

/** Top class for equippable object. */
public abstract class EquipmentItem extends InventoryItem {
	protected final EquipmentTypes equipmentType;

	public EquipmentItem(EquipmentTypes equipmentType) {
		super();
		this.equipmentType = equipmentType;
	}

	public EquipmentTypes getEquipmentType() {
		return equipmentType;
	}
}