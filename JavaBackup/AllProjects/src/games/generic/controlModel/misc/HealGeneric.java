package games.generic.controlModel.misc;

import tools.ObjectNamedID;

/**
 * The "type" of healing and usually is an {@link Enum}-like as, for instance,
 * <code>{Life, Mana}</code>.
 */
public class HealGeneric extends AmountNamed {
	private static final long serialVersionUID = 894363018L;

	public HealGeneric(HealingType healType, int healAmount) {
		super(healType, healAmount);
	}

	public int getHealAmount() {
		return super.value; // super.getValue();
	}

	public HealingType getHealType() {
		return (HealingType) super.type;// super.getType();
	}

	public void setHealAmount(int healAmount) {
//		super.setValue(healAmount);
		super.value = healAmount;
	}

	public void setHealType(ObjectNamedID healType) {
//		super.setType(healType);
		super.type = healType;
	}

	@Override
	public String toString() {
		return "HealGeneric [healAmount=" + getHealAmount() + ", healType=" + getHealType() + "]";
	}
}