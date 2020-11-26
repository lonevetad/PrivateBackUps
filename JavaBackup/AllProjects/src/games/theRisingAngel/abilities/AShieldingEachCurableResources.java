package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.heal.IHealableResourceType;
import games.generic.controlModel.heal.resExample.ExampleHealingType;
import games.generic.controlModel.heal.HealAmountInstance;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityBaseImpl;
import games.theRisingAngel.events.EventDamageTRAn;
import games.theRisingAngel.events.EventHealTRAr;
import games.theRisingAngel.misc.DamageTypesTRAn;
import tools.ObjectWithID;

public class AShieldingEachCurableResources extends AbilityBaseImpl implements GEventObserver {
	private static final long serialVersionUID = 1L;
	public static final int MAX_SHIELD = 100, RARITY = 4,
			PRIORITY_DAMAGE_OBSERVER = ALoseManaBeforeLife.PRIORITY_OBSERVER_SHIELDING_THE_TEMPLE << 1;
	public static final String NAME = "Essence insofference";
	protected static List<String> EVENTS_WATCHING = null;

	protected static List<String> getEventsWatching_SECR() {
		DamageTypesTRAn[] dd;
		ExampleHealingType[] vv;
		if (EVENTS_WATCHING != null)
			return EVENTS_WATCHING;
		vv = ExampleHealingType.values();
		dd = DamageTypesTRAn.values();
		EVENTS_WATCHING = new ArrayList<>(dd.length + vv.length);
		for (ExampleHealingType ht : vv) {
			EVENTS_WATCHING.add(ht.getName());
		}
		for (DamageTypesTRAn dt : dd) {
			EVENTS_WATCHING.add(dt.getName());
		}
		return EVENTS_WATCHING;
	}

	public AShieldingEachCurableResources() {
		shields = new int[ExampleHealingType.values().length];
		resetAbility();

	}

	protected int[] shields;
//	protected List<String> eventsWatching;

	@Override
	public int getObserverPriority() { return PRIORITY_DAMAGE_OBSERVER; }

	@Override
	public List<String> getEventsWatching() { return getEventsWatching_SECR(); }

	@Override
	public void performAbility(GModality gm) {}

	@Override
	public void resetAbility() { Arrays.fill(shields, MAX_SHIELD); }

	protected ExampleHealingType healForDamage(DamageTypesTRAn dt) {
		if (dt == DamageTypesTRAn.Physical)
			return ExampleHealingType.Life;
		else if (dt == DamageTypesTRAn.Magical)
			return ExampleHealingType.Mana;
		else
			return null;
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int index;
		if (ge instanceof EventDamageTRAn) {
			// consume the shield to reduce the damage
			int min;
			EventDamageTRAn ed;
			ObjectWithID o;
			ExampleHealingType ht;
			DamageGeneric dg;
			ed = (EventDamageTRAn) ge;
			o = getOwner();
			if (!(o instanceof LivingObject && ed.isTarget((LivingObject) o)))
				return;
			dg = ed.getDamage();
			ht = healForDamage((DamageTypesTRAn) dg.getDamageType());
			if (ht == null)
				return;
			index = ht.ordinal();
			min = Math.min(ed.getDamageReducedByTargetArmors(), this.shields[index]);
			// shields the damage
			this.shields[index] -= min;
			ed.setDamageAmountToBeApplied(ed.getDamageAmountToBeApplied() - min);
		} else if (ge instanceof EventHealTRAr<?>) {
			// heal the shield
			int amount;
			IHealableResourceType ht;
			EventHealTRAr<?> eh;
			HealAmountInstance hg;
			eh = (EventHealTRAr<?>) ge;
			hg = eh.getHeal();
			ht = hg.getHealType();
			index = ht.getID();
			amount = this.shields[index] + hg.getHealAmount();
			if (amount > MAX_SHIELD)
				amount = MAX_SHIELD;
			this.shields[index] = amount;
		}
	}
}