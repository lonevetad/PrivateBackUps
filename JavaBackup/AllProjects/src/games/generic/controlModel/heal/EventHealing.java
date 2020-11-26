package games.generic.controlModel.heal;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventInfo_SourceToTarget;
import games.generic.controlModel.gObj.LivingObject;

public class EventHealing<Source> extends EventInfo_SourceToTarget<Source, LivingObject> {
	private static final long serialVersionUID = -2222178787L;

	public EventHealing(IGEvent eventIdentifier, Source source, LivingObject target, HealAmountInstance heal) {
		super(eventIdentifier, source, target);
		this.heal = heal;
	}

	protected HealAmountInstance heal;

	public HealAmountInstance getHeal() {
		return heal;
	}

	public void setHeal(HealAmountInstance heal) {
		this.heal = heal;
	}

	@Override
	public boolean isRequirigImmediateProcessing() {
		return true;
	}
}