package games.theRisingAngel.abilities;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.damage.EventDamage;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.ASimpleFixedBufferVanishing;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.misc.AttributesTRAn;

public class AVampireBerserker extends ASimpleFixedBufferVanishing {
	private static final long serialVersionUID = 624478230215L;
	public static final String NAME = "Vampire Hunt";
	public static final int RARITY = 4, BASE_LIFE_LEECH = 5;

	public AVampireBerserker() {
		super(NAME,
				new AttributeModification[] {
						new AttributeModification(AttributesTRAn.LifeLeechPercentage, BASE_LIFE_LEECH),
						new AttributeModification(AttributesTRAn.RegenLife, -1),
						new AttributeModification(AttributesTRAn.RegenMana, 0) });
		setCumulative(true);
		addEventWatched(EventsTRAn.DamageReceived);
		setAbilityEffectDuration(5000);
		setVanishingEffectDuration(3000);
		setRarityIndex(RARITY);
		setMaxAmountStackedTriggerCharges(4);
	}

	@Override
	protected boolean isAcceptableEvent(IGEvent e) {
		return EventsTRAn.DamageReceived.getName() == e.getName() && //
		// I am the receiver?
				((EventDamage) e).isTarget((LivingObject) this.getOwner());
	}

	@Override
	public void doUponAbilityActivated() {
		super.doUponAbilityActivated();
//		removeAndNullifyEffects(); // jet done
		this.attributesToModify[0].setValue(BASE_LIFE_LEECH);
		this.attributesToModify[1].setValue(-1);
		this.attributesToModify[2].setValue(0);
	}

	@Override
	public void doUponAbilityRefreshed() {}

	@Override
	public void vanishEffect(int timeUnit) {// nothing
	}

	@Override
	public void doUponAbilityStartsVanishing() {
		removeAndNullifyEffects();
		this.attributesToModify[0].setValue(0);
		this.attributesToModify[1].setValue(1);
		this.attributesToModify[2].setValue(1);
	}

	@Override
	public void doUponAbilityEffectEnds() {
		super.doUponAbilityEffectEnds();
		removeAndNullifyEffects();
	}
}