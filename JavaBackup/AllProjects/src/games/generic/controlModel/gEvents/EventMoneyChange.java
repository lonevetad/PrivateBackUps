package games.generic.controlModel.gEvents;

import games.generic.controlModel.player.PlayerGeneric;

public class EventMoneyChange extends GEvent {
	private static final long serialVersionUID = 6986301488631859L;
	protected int originalValue, newValue, currencyType;
	protected PlayerGeneric player;

	public EventMoneyChange() {
		super();
	}

	public EventMoneyChange(PlayerGeneric player, int currencyType, int originalValue, int newValue) {
		this();
		this.player = player;
		this.currencyType = currencyType;
		this.originalValue = originalValue;
		this.newValue = newValue;
	}

	//

	@Override
	public String getName() {
		return ExampleGameEvents.MoneyChanged.getName();
	}

	public int getOriginalValue() {
		return originalValue;
	}

	public int getNewValue() {
		return newValue;
	}

	public int getCurrencyType() {
		return currencyType;
	}

	public PlayerGeneric getPlayer() {
		return player;
	}

	public void setOriginalValue(int originalValue) {
		this.originalValue = originalValue;
	}

	public void setNewValue(int newValue) {
		this.newValue = newValue;
	}

	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}

	public void setPlayer(PlayerGeneric player) {
		this.player = player;
	}

	//

	public int getDifference() {
		return this.newValue - this.originalValue;
	}

}