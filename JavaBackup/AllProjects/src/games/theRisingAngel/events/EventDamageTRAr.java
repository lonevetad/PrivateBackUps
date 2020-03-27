package games.theRisingAngel.events;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.eventsGame.EventDamage;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.misc.DamageGeneric;

public class EventDamageTRAr<Source> extends EventDamage<Source> {

	public EventDamageTRAr(IGEvent eventIdentifier, Source source, CreatureOfRPGs target, DamageGeneric damage) {
		super(eventIdentifier, source, target, damage);
	}
}