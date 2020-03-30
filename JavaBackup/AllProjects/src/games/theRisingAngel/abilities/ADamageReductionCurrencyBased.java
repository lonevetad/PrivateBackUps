package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.ObjectWithID;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.CurrencyHolder;
import games.generic.controlModel.inventoryAbil.AbilityModifyingAttributeRealTime;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.subImpl.PlayerRPG_E1;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.DamageTypesTRAr;
import games.theRisingAngel.events.EventDamageTRAr;
import games.theRisingAngel.events.EventsTRAr;

public class ADamageReductionCurrencyBased extends AbilityModifyingAttributeRealTime implements GEventObserver {
	private static final long serialVersionUID = -69287821202158L;
	public static final String NAME = "Buying Reducion ";

	public ADamageReductionCurrencyBased(DamageTypesTRAr dt) {
		super(AttributesTRAr.damageReductionByType(dt), NAME + dt.getName());
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAr.DamageReceived.getName());
		perThousandFraction = 0;
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

	protected int getDefaultCurrencyAmount(CreatureSimple c) {
		int a;
		CurrencySet ch;
		if (!(c instanceof CurrencyHolder))
			return 0;
		ch = ((CurrencyHolder) c).getCurrencies();
		a = ch.getMoneyAmount(CurrencySet.BASE_CURRENCY_INDEX);
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
	public void updateAttributeModifiersAmount(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca) {
		super.getAttributeToModify().setValue( //
				(getDefaultCurrencyAmount(ah) * getPerThousandFraction()) / 1000);
		System.out.println("... attri to mod " + getAttributeToModify().getAttributeModified().getName() + " has value "
				+ getAttributeToModify().getValue() + ", creature's value : "
				+ this.getEquipItem().getCreatureWearingEquipments().getAttributes()
						.getValue(getAttributeToModify().getAttributeModified().getIndex()));
		System.out.println("MoNeY: " + //
				((PlayerRPG_E1) this.getEquipItem().getCreatureWearingEquipments()).getCurrencies()
						.getMoneyAmount(0));
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int a;
		EventDamageTRAr<?> ed;
		BaseCreatureRPG c;
		CurrencySet ch;
		if (ge.getName() == EventsTRAr.DamageReceived.getName()) {
			ed = (EventDamageTRAr<?>) ge;
//			damage = ed.getDamage();
			c = ed.getTarget();
			if (!(c instanceof CurrencyHolder))
				return;
			ch = ((CurrencyHolder) c).getCurrencies();
			a = ch.getMoneyAmount(CurrencySet.BASE_CURRENCY_INDEX);
			a -= ((a * getPerThousandFraction()) / 1000);
			ch.setMoneyAmount(CurrencySet.BASE_CURRENCY_INDEX, //
					a > 0 ? a : 0);
//	super.getAttributeToModify().
		}
	}
}