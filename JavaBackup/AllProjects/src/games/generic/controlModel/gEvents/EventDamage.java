package games.generic.controlModel.gEvents;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.misc.DamageGeneric;

public class EventDamage<Source> extends EventInfo_SourceToTarget<Source, CreatureSimple> {
	private static final long serialVersionUID = 1L;

	public EventDamage(IGEvent eventIdentifier, Source source, CreatureSimple target, DamageGeneric damage) {
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