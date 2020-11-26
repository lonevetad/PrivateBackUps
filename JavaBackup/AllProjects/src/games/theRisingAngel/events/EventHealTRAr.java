package games.theRisingAngel.events;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.heal.EventHealing;
import games.generic.controlModel.heal.HealAmountInstance;

public class EventHealTRAr<Source> extends EventHealing<Source> {
	private static final long serialVersionUID = 2222178787L;

	public EventHealTRAr(IGEvent eventIdentifier, Source source, LivingObject target, HealAmountInstance heal) {
		super(eventIdentifier, source, target, heal);
	}

}