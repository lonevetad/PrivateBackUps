package games.generic.controlModel.eventsGame;

import games.generic.controlModel.player.PlayerInGame_Generic;
import games.generic.controlModel.subImpl.GEvent;

public class EventMoneyChange extends GEvent {

	protected int originalValue, newValue, currencyType;
	protected PlayerInGame_Generic player;

	public EventMoneyChange() {
		super();
	}

	public EventMoneyChange(PlayerInGame_Generic player, int currencyType, int originalValue, int newValue) {
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

	public PlayerInGame_Generic getPlayer() {
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

	public void setPlayer(PlayerInGame_Generic player) {
		this.player = player;
	}

	//

	public int getDifference() {
		return this.newValue - this.originalValue;
	}

}