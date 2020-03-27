package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.ObjectWithID;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.inventory.AbilityModifyingAttributeRealTime;
import games.generic.controlModel.inventory.EquipmentItem;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencyHolder;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.events.EventDamageTRAr;
import games.theRisingAngel.events.EventsTRAr;

public class ADamageReductionPhysicalCurrencyBased extends AbilityModifyingAttributeRealTime implements GEventObserver {

	public ADamageReductionPhysicalCurrencyBased() {
		super(AttributesTRAr.DamageReductionPhysical);
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(this.getAttributeToModify().getAttributeModified().getName());
	}

	protected int perThousandFraction;
	protected List<String> eventsWatching;

	//

	@Override
	public Integer getObserverID() {
		return getID();
	}

	/**
	 * Get the "percentage" (but it's over a thousand, 1000, not the classical
	 * hundred of "%") of the currency held to be converted to
	 * {@link AttributesTRAr.DamageReductionPhysical}.
	 */
	public int getPerThousandFraction() {
		return perThousandFraction;
	}

	protected int getDefaultCurrencyAmount(CreatureOfRPGs c) {
		int a;
		CurrencyHolder ch;
		if (!(c instanceof CurrencyHolder))
			return 0;
		ch = (CurrencyHolder) c;
		a = ch.getMoneyAmount(CurrencyHolder.BASE_CURRENCY_INDEX);
		return a > 0 ? a : 0;
	}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public ObjectWithID getOwner() {
		return this.getEquipItem().getCreatureWearingEquipments();
	}

	//

	@Override
	public void setOwner(ObjectWithID owner) {
		throw new UnsupportedOperationException("Too lazy");
	}

	public void setPerThousandFraction(int perThousandFraction) {
		this.perThousandFraction = perThousandFraction;
	}

	//

	@Override
	public void updateAttributeModifiersAmount(GModality gm, EquipmentItem ei, CreatureOfRPGs ah,
			CreatureAttributes ca) {
		super.getAttributeToModify().setValue( //
				(getDefaultCurrencyAmount(ah) * getPerThousandFraction()) / 1000);
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int a;
		EventDamageTRAr<?> ed;
		CreatureOfRPGs c;
		CurrencyHolder ch;
		if (ge == EventsTRAr.DamageReceived) {
			ed = (EventDamageTRAr<?>) ge;
//			damage = ed.getDamage();
			c = ed.getTarget();
			if (!(c instanceof CurrencyHolder))
				return;
			ch = (CurrencyHolder) c;
			a = ch.getMoneyAmount(CurrencyHolder.BASE_CURRENCY_INDEX);
			ch.setMoneyAmount(CurrencyHolder.BASE_CURRENCY_INDEX, //
					a - ((a * getPerThousandFraction()) / 1000));
//	super.getAttributeToModify().
		}
	}

}