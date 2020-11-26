package games.theRisingAngel.abilities;

import java.util.Arrays;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.damage.EventDamage;
import games.generic.controlModel.gEvents.EventMoviment;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.GModalityTRAn;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.misc.AttributesTRAn;
import tools.ObjectWithID;

/**
 * If the owner does not move, deals damage (approximately, because there's no
 * event of "attack performed") or receive for a certain amount of time, it
 * starts regenerating.<br>
 * This class depends on its "level" (defined in the constructor): its
 * {@link #getTimeThreshold()} depends on the
 * <code>(level+1)*{@link GModalityTRAn#TIME_SUBUNITS_EACH_TIME_UNIT_TRAn}</code>
 * and the amount of healing is <code>(level+1)*N</code>.
 * <p>
 * Currently, N = 4.
 */
public class AMeditationMoreRegen extends AbilityModifyingAttributesRealTime implements GEventObserver {
	private static final long serialVersionUID = -95598741022024L;
	public static final int HEALING_FACTOR = 4;
	public static final String NAME = "Meditation ";
	protected static List<String> EVENTS_WATCHING_MMR = null;
	protected static final AttributeIdentifier[] ATTRIBUTES_MODIFIED_MMR = new AttributeIdentifier[] {
			AttributesTRAn.RegenLife, AttributesTRAn.RegenMana };

	protected static List<String> getEventsWatching_MMR() {
		if (EVENTS_WATCHING_MMR == null)
			EVENTS_WATCHING_MMR = Arrays
					.asList(new String[] { EventsTRAn.ObjectMoved.getName(), EventsTRAn.DamageReceived.getName(),
							EventsTRAn.DamageAvoided.getName(), EventsTRAn.DamageInflicted.getName() });
		return EVENTS_WATCHING_MMR;
	}

	public AMeditationMoreRegen(int level) {
		super(NAME + level, ATTRIBUTES_MODIFIED_MMR);
		if (level < 0)
			throw new IllegalArgumentException("Negative level: " + level);
		this.level = level;
		this.isActive = false;
		this.accumulatedTimeElapsedForUpdating = 0;
		resetLevelDependentStuffs();
	}

	// TODO

	protected boolean isActive;
	protected final int level;
	protected long timeThreshold;

	//

	protected void resetLevelDependentStuffs() {
		int val;
		this.timeThreshold = (level + 1) * GModalityTRAn.TIME_SUBUNITS_EACH_TIME_UNIT_TRAn;
		// set values
		val = (level + 1) * HEALING_FACTOR;
		for (AttributeModification am : this.attributesToModify) {
			am.setValue(val);
		}
	}

	@Override
	public long getTimeThreshold() { return timeThreshold; }

	@Override
	public List<String> getEventsWatching() { return getEventsWatching_MMR(); }

//	public void performAbility(GModality gm) {} 

	@Override
	public void act(GModality modality, int timeUnits) {
		if (isActive)
			return;
		super.act(modality, timeUnits);
	}

	@Override
	public void resetAbility() {
		isActive = false;
		super.resetAbility();
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		boolean isMov, isDamage;
		ObjectWithID eventRelatedObject;
		isMov = (ge instanceof EventMoviment);
		isDamage = (ge instanceof EventDamage);
		if (isMov) {
			eventRelatedObject = ((EventMoviment) ge).getMovingObject();
		} else if (isDamage) {
			EventDamage ed;
			ed = ((EventDamage) ge);
			if (ed.getDamage().getType() == EventsTRAn.DamageInflicted)
				eventRelatedObject = ed.getSource();
			else
				eventRelatedObject = ed.getTarget();
		} else {
			System.out.println("WERID event in AMeditationMoreRegen: " + ge);
			return;
		}
		if (eventRelatedObject != getOwner())
			return; // not related to me
		// if modifiers are applied, then remove them
		if (isActive) { removeModifyingEffecs(); }
		resetAbility();
		// TODO
	}

	@Override
	public void updateAttributesModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca) {
		isActive = true;
		applyModifyingEffecsOnEquipping();
	}
}