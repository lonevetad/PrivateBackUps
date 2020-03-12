package games.generic.controlModel;

public enum ExampleGameEvents implements GameEvent {
	ObjectAdded, ObjectRemoved, ObjectMoved, //
	DamageInflicted, DamageReceived, HealReceived, HealGiven, //
	Destroyed, Spawned, HealGained, PickedUpMoney, PickedUpObject;

	@Override
	public Integer getID() {
		return ordinal();
	}

}