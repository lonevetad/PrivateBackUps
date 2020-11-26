package games.generic.controlModel.heal;

import games.generic.controlModel.misc.AmountNamed;
import tools.ObjectNamedID;

/**
 * Created during the "healing activity" of a {@link HealingObject}, this class
 * links that object to a specific type of "healable" ("curable", to be
 * English-coreect) resource and it's used to define the event
 * {@link EventHealing}.
 */
public class HealAmountInstance extends AmountNamed {
	private static final long serialVersionUID = 894363018L;

	public HealAmountInstance(IHealableResourceType healType, int healAmount) { super(healType, healAmount); }

	public int getHealAmount() {
		return super.value; // super.getValue();
	}

	public IHealableResourceType getHealType() {
		return (IHealableResourceType) super.type;// super.getType();
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