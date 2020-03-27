package games.generic.controlModel.eventsGame;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.misc.DamageGeneric;

public class EventDamage<Source> extends EventInfo_SourceToTarget<Source, CreatureOfRPGs> {

	protected DamageGeneric damage;

	public EventDamage(IGEvent eventIdentifier, Source source, CreatureOfRPGs target, DamageGeneric damage) {
		super(eventIdentifier, source, target);
		this.damage = damage;
	}

	public DamageGeneric getDamage() {
		return damage;
	}

	public void setDamage(DamageGeneric damage) {
		this.damage = damage;
	}

}