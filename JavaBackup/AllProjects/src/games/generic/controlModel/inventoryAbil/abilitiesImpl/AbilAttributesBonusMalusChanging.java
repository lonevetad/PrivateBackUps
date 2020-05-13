package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.AttributesHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.subimpl.TimedObjectSimpleImpl;
import tools.ObjectWithID;

/**
 * Provides some {@link CreatureAttributes} bonus and some malus. Which is bonus
 * and which is malus changes over time and lasts for
 * {@link #getTimeThreshold()} "time units". Each time this amount of time has
 * been elapsed, an update must be performed (for instance, changing the bonus
 * set and the non-intersecting malus set, maybe randomly)
 */
public abstract class AbilAttributesBonusMalusChanging extends AbilityBaseImpl implements TimedObjectSimpleImpl {
	private static final long serialVersionUID = -87835423666663L;

	public AbilAttributesBonusMalusChanging(String name) {
		super(name);
	}

	protected long accumulatedTimeElapsed;

	@Override
	public long getAccumulatedTimeElapsed() {
		return 0;
	}

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) {
		this.accumulatedTimeElapsed = newAccumulated;
	}

	@Override
	public void resetAbility() {
		setAccumulatedTimeElapsed(0);
	}

	@Override
	public void onAddingToOwner(GModality gm) {
		performAbility(gm);
	}

	@Override
	public void executeAction(GModality modality) {
		performAbility(modality);
	}

	@Override
	public void performAbility(GModality gm) {
		ObjectWithID ow;
//		BaseCreatureRPG ah;
		CreatureAttributes ca;
		ow = this.owner;
		if (ow == null || (!(ow instanceof AttributesHolder)))
			return;
		ca = ((AttributesHolder) ow).getAttributes();
		if (ca == null)
			return;
		removeAttributesBonusesMaluses(gm, ca);
		updateAttributesBonuses(gm, ca);
		updateAttributesMaluses(gm, ca);
		applyAttributesBonusesMaluses(gm, ca);
	}

	public abstract void removeAttributesBonusesMaluses(GModality gm, CreatureAttributes ca);

	public abstract void updateAttributesBonuses(GModality gm, CreatureAttributes ca);

	public abstract void updateAttributesMaluses(GModality gm, CreatureAttributes ca);

	public abstract void applyAttributesBonusesMaluses(GModality gm, CreatureAttributes ca);
}