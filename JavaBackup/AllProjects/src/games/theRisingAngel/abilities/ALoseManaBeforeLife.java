package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.gObj.ManaHavingObject;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityBaseImpl;
import games.generic.controlModel.misc.AttributesHolder;
import games.theRisingAngel.events.EventsTRAn;

public class ALoseManaBeforeLife extends AbilityBaseImpl implements GEventObserver {
	private static final long serialVersionUID = -451558465L;
	public static final String NAME = "Shielding the Temple";
	public static final int RARITY = 3, PRIORITY_OBSERVER_SHIELDING_THE_TEMPLE = 1;

	public ALoseManaBeforeLife() {
		eventsWatching = new ArrayList<>(2);
		eventsWatching.add(EventsTRAn.DamageReceived.getName());
		setRarityIndex(RARITY);
	}

	protected List<String> eventsWatching;

	@Override
	public List<String> getEventsWatching() { return eventsWatching; }

	@Override
	public int getObserverPriority() { return PRIORITY_OBSERVER_SHIELDING_THE_TEMPLE; }

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int damageAmount, manaAmount, min;
		LivingObject lo;
		EventDamage ed;
		ManaHavingObject mho;
		if (ge.getName() != EventsTRAn.DamageReceived.getName())
			return;
		ed = (EventDamage) ge;
		if (!(owner instanceof LivingObject && owner instanceof ManaHavingObject && owner instanceof AttributesHolder))
			return;
		lo = (LivingObject) owner;
		if (!ed.isTarget(lo))
			return;
		mho = (ManaHavingObject) owner;
		manaAmount = mho.getMana();
		if (manaAmount <= 0)
			return; // no mana to sacrifice
		damageAmount = ed.getDamageReducedByTargetArmors(); // get the damage (TODO not the AmountToBeApplied?)
		min = damageAmount > manaAmount ? manaAmount : damageAmount;
		System.out.println(NAME + " assorbed: " + min);
		mho.setMana(mho.getMana() - min);
		ed.setDamageAmountToBeApplied(ed.getDamageAmountToBeApplied() - min);
		// TODO sicuro che non sia getDamageReducedByTargetArmors ?
	}

	@Override
	public void performAbility(GModality gm) {}

	@Override
	public void resetAbility() {}
}