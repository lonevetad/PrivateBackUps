package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.ObjectWithID;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AbilityModifyingAttributeRealTime;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.events.EventDamageTRAr;
import games.theRisingAngel.events.EventsTRAr;

/**
 * Grants a life regeneration equals to the 25% of received damage, accumulated
 * each time it's received.<br>
 * At each "tick", equals to
 * {@link AbilityModifyingAttributeRealTime#MILLISEC_ATTRIBUTE_UPDATE}, it
 * decrease by {@link #VALUE_DECREMENT_PER_TICK}.
 */
public class AMoreDamageReceivedMoreLifeRegen extends AbilityModifyingAttributeRealTime implements GEventObserver {
	private static final long serialVersionUID = 5411087000163L;
	public static final int VALUE_DECREMENT_PER_TICK = 2;
	public static final String NAME = "Pain Rinvigoring";

	public AMoreDamageReceivedMoreLifeRegen() {
		super(AttributesTRAr.RigenLife, NAME);
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(
//				this.getAttributeToModify().getAttributeModified().getName()
				EventsTRAr.DamageReceived.getName());
		ticks = 0;
		thresholdTime = 1000;
	}

	protected long ticks, thresholdTime;
	protected List<String> eventsWatching;
//	protected CreatureSimple creatureReferred;

	@Override
	public Integer getObserverID() {
		return getID();
	}

//	public CreatureSimple getCreatureReferred() {return creatureReferred;}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public ObjectWithID getOwner() {
		return this.getEquipItem().getCreatureWearingEquipments();
	}

	@Override
	public long getTimeThreshold() {
		return thresholdTime;
	}

	//

	@Override
	public void setOwner(ObjectWithID owner) {
		if (owner instanceof BaseCreatureRPG)
//			setCreatureReferred
			this.getEquipItem().getBelongingEquipmentSet().setCreatureWearingEquipments((BaseCreatureRPG) owner);
	}

//	public void setCreatureReferred(CreatureSimple creatureReferred) {this.creatureReferred = creatureReferred;}

	//

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		if (EventsTRAr.DamageReceived.getName() == ge.getName()) {
			int d;
			EventDamageTRAr<?> dEvent;
			AttributeModification am;
			CreatureAttributes ca;
			dEvent = (EventDamageTRAr<?>) ge;
			if (dEvent.getTarget() == //
			this.getEquipItem() //
					.getCreatureWearingEquipments() // check because it's bounded to the "wearer"
					&& (d = dEvent.getDamage().getDamageAmount()) >= 4) {
				// increase of 25% of damage, so minimum is 4

				am = this.getAttributeToModify();
				d = am.getValue() + (d >> 2);

				ca = this.getEquipItem().getCreatureWearingEquipments().getAttributes();
				// update: remove previous, add new value
				ca.removeAttributeModifier(am);
				am.setValue(d > 0 ? d : 0);
				ca.applyAttributeModifier(am);
				System.out.println("같같같같같� now regen is: " + am.getValue());
			}
		}
	}

	@Override
	public void updateAttributeModifiersAmount(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca) {
		int v;
		AttributeModification am;
		am = this.getAttributeToModify();
		v = am.getValue();
		if (v > 0) {
			if (++ticks >= (1000 / AbilityModifyingAttributeRealTime.MILLISEC_ATTRIBUTE_UPDATE))
				ticks = 0;
			v -= VALUE_DECREMENT_PER_TICK;
			am.setValue(v > 0 ? v : 0);
			System.out.println("______new life regen value : " + am.getValue() + " .... AMoreDamageMoreRegenBLA");
		}
	}
}