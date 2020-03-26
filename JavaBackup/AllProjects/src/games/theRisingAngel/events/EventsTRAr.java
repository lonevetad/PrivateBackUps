package games.theRisingAngel.events;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.eventsGame.ExampleGameEvents;

/** Taken from {@link ExampleGameEvents}. */
public enum EventsTRAr implements IGEvent {
	Destroyed("OIS"), ObjectAdded("OIS"), ObjectRemoved("OIS"), ObjectMoved("OIS"), // OIS = Object In Space
	DamageInflicted("Dmg"), DamageReceived("Dmg"), HealReceived("Heal"), HealGiven("Heal"), //
	PickedUpMoney("Money"), MoneyChanged("Money"), PickedUpDrop("Drop"), DropReleased("Drop");

	protected final String type;

	EventsTRAr(String t) {
		this.type = t;
	}

	@Override
	public Integer getID() {
		return ordinal();
	}

	public String getSuperType() {
		return type;
	}

	@Override
	public String getName() {
		return name();
	}
}