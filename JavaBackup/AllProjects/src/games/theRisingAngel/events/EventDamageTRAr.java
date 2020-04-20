package games.theRisingAngel.events;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.DamageGeneric;

public class EventDamageTRAr<Source> extends EventDamage<Source> {
	private static final long serialVersionUID = 2222178786L;

	public EventDamageTRAr(IGEvent eventIdentifier, Source source, LivingObject target, DamageGeneric damage) {
		super(eventIdentifier, source, target, damage);
	}

	public EventDamageTRAr(IGEvent eventIdentifier, Source source, LivingObject target, DamageGeneric damage,
			int damageAmountToBeApplied) {
		super(eventIdentifier, source, target, damage, damageAmountToBeApplied);
	}
}