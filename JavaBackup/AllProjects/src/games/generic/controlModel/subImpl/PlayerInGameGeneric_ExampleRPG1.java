package games.generic.controlModel.subImpl;

import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.gameObj.CurrencyHolder;
import games.generic.controlModel.inventory.EquipmentSet;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.PlayerIG_WithExperience;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.CreatureUIDProvider;

/** Designed for Role Play Game. */
public abstract class PlayerInGameGeneric_ExampleRPG1 extends PlayerIG_WithExperience
		implements CreatureOfRPGs, CurrencyHolder {
	private static final long serialVersionUID = -777564684007L;

//	GModality gameModality;
	protected boolean isDestroyed;
	protected int life, ticks;
	protected long accumulatedTimeLifeRegen;
	protected List<String> eventsWatching;
	protected EquipmentSet equipmentSet;
	protected CreatureAttributes attributes;
	protected CurrencySet currencies;

	public PlayerInGameGeneric_ExampleRPG1(GModality gameModality) {
		super(gameModality);
		this.isDestroyed = false;
//		this.ID = CreatureUIDProvider.newID();
		this.attributes = new CreatureAttributesModsCaching(AttributesTRAr.VALUES.length);
		this.life = 1; // just something to start with
	}

	@Override
	protected void initializeID() {
		this.ID = CreatureUIDProvider.newID();
	}

	//

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public int getLife() {
		return life;
	}

	@Override
	public CurrencySet getCurrencies() {
		return currencies;
	}

	@Override
	public EquipmentSet getEquipmentSet() {
		return equipmentSet;
	}

	@Override
	public CreatureAttributes getAttributes() {
		return attributes;
	}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public int getTicks() {
		return ticks;
	}

	@Override
	public long getAccumulatedTimeLifeRegen() {
		return accumulatedTimeLifeRegen;
	}

	//

	//

	@Override
	public void setLife(int life) {
		if (life <= 0)
			this.life = 0;
		else {
			if (life > getLifeMax())
				life = getLifeMax();
			this.life = life;
		}
	}

	@Override
	public void setEquipmentSet(EquipmentSet equips) {
		this.equipmentSet = equips;
		if (equips != null) {
			equips.setCreatureWearingEquipments(this);
		}
	}

	@Override
	public void setAttributes(CreatureAttributes attributes) {
		this.attributes = attributes;
	}

	@Override
	public void setCurrencies(CurrencySet currencies) {
		this.currencies = currencies;
	}

	@Override
	public void setAccumulatedTimeLifeRegen(long newAccumulated) {
		this.accumulatedTimeLifeRegen = newAccumulated;
	}

	@Override
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	//

	//

	/**
	 * Override designed. <br>
	 * When the game actually starts and the player "drops into the game", some
	 * actions could be performed.
	 */
	public abstract void onStartingGame(GModality mg);

	//

	// TODO EVENTS FIRING

}