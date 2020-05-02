package games.generic.controlModel.gEvents;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.DamageGeneric;

public class EventDamage extends EventInfo_SourceToTarget<DamageDealerGeneric, LivingObject> {
	private static final long serialVersionUID = 1L;

	public EventDamage(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target, DamageGeneric damage) {
		this(eventIdentifier, source, target, damage, damage.getDamageAmount());
	}

	public EventDamage(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target, DamageGeneric damage,
			int damageAmountToBeApplied) {
		super(eventIdentifier, source, target);
		this.damage = damage;
		this.damageAmountToBeApplied = damageAmountToBeApplied;
	}

	protected DamageGeneric damage;
	protected int damageAmountToBeApplied;

	public DamageGeneric getDamage() {
		return damage;
	}

	public int getDamageAmountToBeApplied() {
		return damageAmountToBeApplied;
	}

	public void setDamage(DamageGeneric damage) {
		this.damage = damage;
	}

	public void setRiductionByTarget(int riductionByTarget) {
		this.damageAmountToBeApplied = riductionByTarget;
	}

	@Override
	public boolean isRequirigImmediateProcessing() {
		return true;
	}

	public boolean isSource(DamageDealerGeneric ddg) {
		return super.source == ddg;
	}

	public boolean isTarget(LivingObject ddg) {
		return super.target == ddg;
	}
}