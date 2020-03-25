package games.generic.controlModel.eventsGame;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gameObj.CreatureOfRPGs;

public class EventDamage<Source> extends EventInfo_SourceToTarget<Source, CreatureOfRPGs> {

	protected int damage;

	public EventDamage(IGEvent eventIdentifier, Source source, CreatureOfRPGs target, int damage) {
		super(eventIdentifier, source, target, damage);
		this.damage = damage;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

}