package games.theRisingAngel.events;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.DamageGeneric;

public class EventDamageTRAn extends EventDamage {
	private static final long serialVersionUID = 2222178786L;

	public EventDamageTRAn(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage) {
		super(eventIdentifier, source, target, damage);
	}

	public EventDamageTRAn(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage, int damageAmountToBeApplied) {
		super(eventIdentifier, source, target, damage, damageAmountToBeApplied);
	}
}