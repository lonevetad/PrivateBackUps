package games.generic.controlModel.gEvents;

import games.generic.controlModel.IGEvent;

public enum ExampleGameEvents implements IGEvent { // implements GameEventFactory
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

	/** SHOULD NOT BE USED */
	@Deprecated
	public String getSuperType() {
		return type;
	}

	@Override
	public Integer getID() {
		return ordinal();
	}

	@Override
	public String getName() {
		return name();
	}

//	public GameEvent newGameEvent(Integer id, String name) {
//		return delegate.newGameEvent(ordinal(), name());
//	}

}