package games.theRisingAngel.inventory;

public abstract class EIRing extends EIJewelry {

	int slotFingerSize;

	public EIRing(EquipmentTypesTRAr et) {
		super(et);
		this.slotFingerSize = 1;
	}

	public int getSlotFingerSize() {
		return slotFingerSize;
	}

	public void setSlotFingerSize(int slotFingerSize) {
		this.slotFingerSize = slotFingerSize;
	}
}