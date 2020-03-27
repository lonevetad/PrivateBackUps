package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.ObjectWithID;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.gameObj.CreatureSimple;
import games.generic.controlModel.inventory.AbilityModifyingAttributeRealTime;
import games.generic.controlModel.inventory.AttributeModification;
import games.generic.controlModel.inventory.EquipmentItem;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.events.EventDamageTRAr;
import games.theRisingAngel.events.EventsTRAr;

/**
 * E collana che da rigenerazione vitale pari al 25% del danno subito, ma ogni
 * secondo tale ammontare cala fino a 0 (quindi ad ogni evento del danno,
 * incrementa il contatore del totale, poi ogni secondo scala di es 4 e aggiorna
 * le statistiche)
 */
public class AMoreDamageReceivedMoreLifeRegen extends AbilityModifyingAttributeRealTime implements GEventObserver {
	public static final int VALUE_DECREMENT_PER_TICK = 2; // it's assumed "2 ticks per second"

	public AMoreDamageReceivedMoreLifeRegen() {
		super(AttributesTRAr.RigenLife);
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(
//				this.getAttributeToModify().getAttributeModified().getName()
				EventsTRAr.DamageReceived.getName());
	}

	protected List<String> eventsWatching;
	protected CreatureSimple creatureReferred;

	@Override
	public Integer getObserverID() {
		return getID();
	}

	public CreatureSimple getCreatureReferred() {
		return creatureReferred;
	}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public ObjectWithID getOwner() {
		return getCreatureReferred();
	}

	//

	@Override
	public void setOwner(ObjectWithID owner) {
		if (owner instanceof CreatureSimple)
			setCreatureReferred((CreatureSimple) owner);
	}

	public void setCreatureReferred(CreatureSimple creatureReferred) {
		this.creatureReferred = creatureReferred;
	}

	//

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		if (EventsTRAr.DamageReceived.getName() == ge.getName()) {
			int d;
			EventDamageTRAr<?> dEvent;
			AttributeModification am;
			dEvent = (EventDamageTRAr<?>) ge;
			if (dEvent.getTarget() == this.creatureReferred // check because it's bounded to the "wearer"
					&& (d = dEvent.getDamage()) >= 4) {
				// increase of 25% of damage, so minimum is 4
				am = this.getAttributeToModify();
				am.setValue(am.getValue() + (d >> 2));
			}
		}
	}

	@Override
	public void updateAttributeModifiersAmount(GModality gm, EquipmentItem ei, CreatureOfRPGs ah,
			CreatureAttributes ca) {
		AttributeModification am;
		am = this.getAttributeToModify();
		am.setValue(am.getValue() - VALUE_DECREMENT_PER_TICK);
		System.out.println("______new life regen value : " + am.getValue() + " .... AMoreDamageMoreRegenBLA");
	}
}