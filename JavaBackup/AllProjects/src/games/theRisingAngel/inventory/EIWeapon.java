package games.theRisingAngel.inventory;

import games.generic.controlModel.subimpl.GModalityRPG;

public abstract class EIWeapon extends EINotJewelry {
	private static final long serialVersionUID = -63485105886988L;

	public EIWeapon(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name) {
		super(gmrpg, et, name);
	}

	protected boolean isRightHand, isSingleHanded;

	//

	public boolean isRightHand() {
		return isRightHand;
	}

	public boolean isSingleHanded() {
		return isSingleHanded;
	}

	//

	public void setRightHand(boolean isRightHand) {
		this.isRightHand = isRightHand;
	}

	public void setSingleHanded(boolean isSingleHanded) {
		this.isSingleHanded = isSingleHanded;
	}
}