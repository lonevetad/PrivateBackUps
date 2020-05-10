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
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityModifyingSingleAttributeRealTime;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.BasePlayerRPG;
import games.theRisingAngel.events.EventDamageTRAn;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.DamageTypesTRAn;
import tools.ObjectWithID;

public class ADamageReductionCurrencyBased extends AbilityModifyingSingleAttributeRealTime implements GEventObserver {
	private static final long serialVersionUID = -69287821202158L;
	public static final String NAME = "Buying Reducion ";
	public static final int RARITY = 3;

	public ADamageReductionCurrencyBased(DamageTypesTRAn dt) {
		super(NAME + dt.getName(), AttributesTRAn.damageReductionByType(dt));
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAn.DamageReceived.getName());
		perThousandFraction = 0;
		maximumReduction = 0;
		setRarityIndex(RARITY);
	}

	protected int perThousandFraction, maximumReduction;
	protected List<String> eventsWatching;

	//

	@Override
	public Integer getObserverID() {
		return getID();
	}

	/**
	 * Get the "percentage" (but it's over a thousand, 1000, not the classical
	 * hundred of "%") of the currency held to be converted to
	 * {@link AttributesTRAn.DamageReductionPhysical}.
	 */
	public int getPerThousandFraction() {
		return perThousandFraction;
	}

	public int getMaximumReduction() {
		return maximumReduction;
	}

	public void setMaximumReduction(int maximumReduction) {
		this.maximumReduction = maximumReduction;
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

	public void setPerThousandFraction(int perThousandFraction) {
		this.perThousandFraction = perThousandFraction;
	}

	//

	@Override
	public void updateAttributesModifiersValues(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca) {
		int reduct;
		AttributeModification am;
		am = super.getAttributesToModify()[0];
		reduct = (getDefaultCurrencyAmount(ah) * getPerThousandFraction()) / 1000;
		if (maximumReduction > 0 && reduct > maximumReduction) {
			reduct = maximumReduction;
		}
		am.setValue(reduct);
		System.out.println(";;ADamagRedCurrBas... attri to mod " + am.getAttributeModified().getName() + " has value "
				+ am.getValue() + ", creature's value : " + this.getEquipItem().getCreatureWearingEquipments()
						.getAttributes().getValue(am.getAttributeModified()));
		System.out.println("MoNeY: " + //
				((BasePlayerRPG) this.getEquipItem().getCreatureWearingEquipments()).getCurrencies().getMoneyAmount(0));
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
			a -= ((a * getPerThousandFraction()) / 1000);
			ch.setMoneyAmount(CurrencySet.BASE_CURRENCY_INDEX, //
					a > 0 ? a : 0);
//	super.getAttributeToModify().
		}
	}
}