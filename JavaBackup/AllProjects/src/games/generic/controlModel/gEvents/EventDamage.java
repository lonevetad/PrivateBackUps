package games.generic.controlModel.gEvents;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.DamageGeneric;

public class EventDamage<Source> extends EventInfo_SourceToTarget<Source, LivingObject> {
	private static final long serialVersionUID = 1L;

	public EventDamage(IGEvent eventIdentifier, Source source, LivingObject target, DamageGeneric damage) {
		super(eventIdentifier, source, target);
		this.damage = damage;
	}

	protected DamageGeneric damage;

	public DamageGeneric getDamage() {
		return damage;
	}

	public void setDamage(DamageGeneric damage) {
		this.damage = damage;
	}

}