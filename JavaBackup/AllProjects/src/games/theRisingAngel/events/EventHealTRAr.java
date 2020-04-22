package games.theRisingAngel.events;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventHealing;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.HealGeneric;

public class EventHealTRAr<Source> extends EventHealing<Source> {
	private static final long serialVersionUID = 2222178787L;

	public EventHealTRAr(IGEvent eventIdentifier, Source source, LivingObject target, HealGeneric heal) {
		super(eventIdentifier, source, target, heal);
	}

}