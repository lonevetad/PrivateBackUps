package games.theRisingAngel.inventory;

import games.generic.controlModel.subimpl.GModalityRPG;

public class EIRing extends EIJewelry {
	private static final long serialVersionUID = 1L;

	public EIRing(GModalityRPG gmrpg, String name) {
		super(gmrpg, EquipmentTypesTRAn.Ring, name);
//		this.slotFingerSize = 1;
	}

	public int getSlotFingerSize() {
		return dimensionInInventory.width;
	}

	public void setSlotFingerSize(int slotFingerSize) {
		if (slotFingerSize > 0)
			dimensionInInventory.width = slotFingerSize;
	}
}