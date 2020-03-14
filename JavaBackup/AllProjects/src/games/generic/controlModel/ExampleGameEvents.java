package games.generic.controlModel;

public enum ExampleGameEvents implements GameEventFactory {
	ObjectAdded, ObjectRemoved, ObjectMoved, //
	DamageInflicted, DamageReceived, HealReceived, HealGiven, //
	Destroyed, Spawned, HealGained, PickedUpMoney, PickedUpObject;

	protected final GameEventFactory delegate;

	ExampleGameEvents() {
		this(null); // TODO complete other
	}

	ExampleGameEvents(GameEventFactory del) {
		this.delegate = del;
	}

	@Override
	public GameEvent newGameEvent(Integer id, String name) {
		return delegate.newGameEvent(ordinal(), name());
	}

}