package games.generic.controlModel.gEvents;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.HealGeneric;

public class EventHealing<Source> extends EventInfo_SourceToTarget<Source, LivingObject> {
	private static final long serialVersionUID = -2222178787L;

	public EventHealing(IGEvent eventIdentifier, Source source, LivingObject target, HealGeneric heal) {
		super(eventIdentifier, source, target);
		this.heal = heal;
	}

	protected HealGeneric heal;

	public HealGeneric getHeal() {
		return heal;
	}

	public void setHeal(HealGeneric heal) {
		this.heal = heal;
	}
}