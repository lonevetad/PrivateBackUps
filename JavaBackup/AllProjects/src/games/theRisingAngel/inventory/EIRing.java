package games.theRisingAngel.inventory;

import games.generic.controlModel.subImpl.GModalityRPG;

public abstract class EIRing extends EIJewelry {
	private static final long serialVersionUID = 1L;
	int slotFingerSize;

	public EIRing(GModalityRPG gmrpg, EquipmentTypesTRAr et, String name) {
		super(gmrpg, et, name);
		this.slotFingerSize = 1;
	}

	public int getSlotFingerSize() {
		return slotFingerSize;
	}

	public void setSlotFingerSize(int slotFingerSize) {
		this.slotFingerSize = slotFingerSize;
	}
}