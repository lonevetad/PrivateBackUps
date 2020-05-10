package games.theRisingAngel.events;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.ExampleGameEvents;

/** Taken from {@link ExampleGameEvents}. */
public enum EventsTRAn implements IGEvent {
	Destroyed("OIS"), ObjectAdded("OIS"), ObjectRemoved("OIS"), ObjectMoved("OIS"), // OIS = Object In Space
<<<<<<< HEAD:JavaBackup/AllProjects/src/games/theRisingAngel/events/EventsTRAn.java
	DamageInflicted("Dmg"), DamageReceived("Dmg"), DamageCriticalInflicted("Dmg"), DamageCriticalReceived("Dmg"),
	DamageMissed("Dmg"), DamageAvoided("Dmg"), //
	HealReceived("Heal"), HealGiven("Heal"), //
	PickedUpMoney("Money"), MoneyChanged("Money"), PickedUpDrop("Drop"), DropReleased("Drop"),
	//
	UserInteraction("User");
=======
	DamageInflicted("Dmg"), DamageReceived("Dmg"), DamageCriticalInflicted("Dmg"), DamageCriticalReceived("Dmg"), //
	HealReceived("Heal"), HealGiven("Heal"), //
	PickedUpMoney("Money"), MoneyChanged("Money"), PickedUpDrop("Drop"), DropReleased("Drop");
>>>>>>> master:JavaBackup/AllProjects/src/games/theRisingAngel/events/EventsTRAr.java

	protected final String type;

	EventsTRAn(String t) {
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