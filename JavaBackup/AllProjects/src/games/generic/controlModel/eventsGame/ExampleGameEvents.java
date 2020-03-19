package games.generic.controlModel.eventsGame;

public enum ExampleGameEvents { // implements GameEventFactory
	Destroyed("OIS"), ObjectAdded("OIS"), ObjectRemoved("OIS"), ObjectMoved("OIS"), // OIS = Object In Space
	DamageInflicted("Dmg"), DamageReceived("Dmg"), HealReceived("Heal"), HealGiven("Heal"), //
	PickedUpMoney("Money"), MoneyChanged("Money"), PickedUpDrop("Drop"), DropReleased("Drop")
	//
	;

	protected final String type;

//	ExampleGameEvents(GameEventFactory del) {
//		this.delegate = del;
//	}
	ExampleGameEvents(String t) {
		this.type = t;
	}

	public String getType() {
		return type;
	}

//	public GameEvent newGameEvent(Integer id, String name) {
//		return delegate.newGameEvent(ordinal(), name());
//	}

}