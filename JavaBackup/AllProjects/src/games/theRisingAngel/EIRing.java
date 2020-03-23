package games.theRisingAngel;

public abstract class EIRing extends EIJewelry {

	int slotFingerSize;

	public EIRing(EquipmentTypes et) {
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