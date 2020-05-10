package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventHealing;
import games.generic.controlModel.gObj.CurrencyHolder;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.EquipmentAbilityBaseImpl;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.HealingTypeExample;
import games.theRisingAngel.events.EventsTRAn;
import tools.ObjectWithID;

/**
 * Upon receiving life healing, make the owner earn N percentage of the base
 * currency, if possible.
 * <p>
 * N = 50.
 */
public class ALifeHealingMakesEarnBaseCurrency extends EquipmentAbilityBaseImpl implements GEventObserver {
	private static final long serialVersionUID = -5898625452208602147L;
	public static final String NAME = "Healthy wallet in healthy body";
	public static final int RARITY = 4;

	public ALifeHealingMakesEarnBaseCurrency() {
		super();
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAn.HealReceived.getName());
		this.hasReceivedOddReneration = false;
		setRarityIndex(RARITY);
	}

	protected boolean hasReceivedOddReneration;
	protected List<String> eventsWatching;

//	public CreatureSimple getCreatureReferred() {return creatureReferred;}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public void performAbility(GModality gm) {
	}

	@Override
	public void resetAbility() {
		this.hasReceivedOddReneration = false;
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		if (EventsTRAn.HealReceived.getName() == ge.getName()) {
			int amoutHealed;
			EventHealing<?> eventHealing;
			CurrencyHolder ch;
			CurrencySet cs;
			LivingObject ol;
			ObjectWithID owid;
			eventHealing = (EventHealing<?>) ge;
			if (eventHealing.getHeal().getType() != HealingTypeExample.Life)
				return;
			amoutHealed = eventHealing.getHeal().getHealAmount();
			owid = getOwner();
			if (eventHealing.getTarget() == owid //
//					&&(amoutHealed > 1
//							|| (hasReceivedOddReneration && amoutHealed > 0))//
					&& (owid instanceof CurrencyHolder)) {
				ol = (LivingObject) owid;
				if ((amoutHealed & 0x1) == 1) {
					if (hasReceivedOddReneration) {
						amoutHealed++;
						hasReceivedOddReneration = false;
					} else {
						hasReceivedOddReneration = true;
					}
				}
				if (ol.getLife() < ol.getLifeMax()) {
					// apply only if the regeneration has really happened
					ch = (CurrencyHolder) owid;
					cs = ch.getCurrencies();
					cs.setMoneyAmount(CurrencySet.BASE_CURRENCY_INDEX,
							cs.getMoneyAmount(CurrencySet.BASE_CURRENCY_INDEX) + (amoutHealed >> 1));
				}
			}
		}
	}
}