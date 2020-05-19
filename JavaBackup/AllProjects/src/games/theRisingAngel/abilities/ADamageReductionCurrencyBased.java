package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.CurrencyHolder;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencySet;
import games.theRisingAngel.events.EventDamageTRAn;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.misc.AttributesTRAn;

public class ADamageReductionCurrencyBased extends AbilityModifyingAttributesRealTime implements GEventObserver {
	private static final long serialVersionUID = -69287821202158L;
	public static final String NAME = "Buying Reducion ";
	public static final int RARITY = 3;

	public ADamageReductionCurrencyBased() {// (DamageTypesTRAn dt
		this(RARITY);
	}

	public ADamageReductionCurrencyBased(int rarity) {
//			super(NAME + dt.getName(), AttributesTRAn.damageReductionByType(dt));
		super(NAME + rarity,
				new AttributesTRAn[] { AttributesTRAn.DamageReductionPhysical, AttributesTRAn.DamageReductionMagical });
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAn.DamageReceived.getName());
		perThousandFraction = 0;
		maximumReduction = 0;
		setRarityIndex(rarity);
		this.setPerThousandFraction(25 + (10 * rarity));
		this.setMaximumReduction(20 + (10 * rarity));
	}

	protected int perThousandFraction, maximumReduction;
	protected List<String> eventsWatching;

	//

	@Override
	public Integer getObserverID() { return getID(); }

	/**
	 * Get the "percentage" (but it's over a thousand, 1000, not the classical
	 * hundred of "%") of the currency held to be converted to
	 * {@link AttributesTRAn.DamageReductionPhysical}.
	 */
	public int getPerThousandFraction() { return perThousandFraction; }

	public int getMaximumReduction() { return maximumReduction; }

	public void setMaximumReduction(int maximumReduction) { this.maximumReduction = maximumReduction; }

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
	public List<String> getEventsWatching() { return eventsWatching; }

	//

	public void setPerThousandFraction(int perThousandFraction) { this.perThousandFraction = perThousandFraction; }

	//

	@Override
	public void updateAttributesModifiersValues(GModality gm, /* EquipmentItem ei, */ CreatureSimple ah,
			CreatureAttributes ca) {
		int reduct;
		AttributeModification am;
		reduct = (getDefaultCurrencyAmount(ah) * getPerThousandFraction()) / 1000;
		if (maximumReduction > 0 && reduct > maximumReduction) { reduct = maximumReduction; }
		am = super.getAttributesToModify()[0];
		am.setValue(reduct);
//		System.out.println(";;ADamagRedCurrBas... attri to mod " + am.getAttributeModified().getName() + " has value "
//				+ am.getValue() + ", creature's value : " + //
//				// this.getEquipItem().getCreatureWearingEquipments().getAttributes()
//				this.getAttributesOfOwner()//
//						.getValue(am.getAttributeModified()));
//		System.out.println("MoNeY: " + //
//				((BasePlayerRPG) this.getOwner() // this.getEquipItem().getCreatureWearingEquipments()
//				).getCurrencies().getMoneyAmount(0));
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int a;
		EventDamageTRAn ed;
		BaseCreatureRPG c;
		CurrencySet ch;
		if (ge.getName() == EventsTRAn.DamageReceived.getName()) {
			ed = (EventDamageTRAn) ge;
//			damage = ed.getDamage();
			c = (BaseCreatureRPG) ed.getTarget();
			if (!(c instanceof CurrencyHolder))
				return;
			ch = ((CurrencyHolder) c).getCurrencies();
			a = ch.getMoneyAmount(CurrencySet.BASE_CURRENCY_INDEX);
			a -= Math.min(//
					((a * getPerThousandFraction()) / 1000), //
					((maximumReduction * getPerThousandFraction()) / 1000));
			ch.setMoneyAmount(CurrencySet.BASE_CURRENCY_INDEX, //
					a > 0 ? a : 0);
//	super.getAttributeToModify().
		}
	}
}