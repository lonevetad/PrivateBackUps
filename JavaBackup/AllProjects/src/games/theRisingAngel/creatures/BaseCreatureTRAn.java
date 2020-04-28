package games.theRisingAngel.creatures;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.DamageTypeGeneric;
import games.generic.controlModel.subimpl.BaseCreatureRPGImpl;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.events.EventsTRAr;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.CreatureAttributesBonusesCalculatorTRAr;
import games.theRisingAngel.misc.DamageTypesTRAn;

public abstract class BaseCreatureTRAn extends BaseCreatureRPGImpl {
	private static final long serialVersionUID = -34551879021102L;

	public BaseCreatureTRAn(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
	}

	//

	@Override
	protected CreatureAttributes newAttributes() {
		CreatureAttributes ca;
		ca = newAttributes(AttributesTRAn.VALUES.length);
		ca.setBonusCalculator(new CreatureAttributesBonusesCalculatorTRAr());
		return ca;
	}

	@Override
	public int getLifeMax() {
		return this.getAttributes().getValue(AttributesTRAn.LifeMax.getIndex());
	}

	@Override
	public int getLifeRegenation() {
		return this.getAttributes().getValue(AttributesTRAn.RigenLife.getIndex());
	}

	@Override
	public int getProbabilityPerThousandAvoid(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.ProbabilityAvoidPhysical
				: AttributesTRAn.ProbabilityAvoidMagical;
		return this.getAttributes().getValue(ai.getIndex());
	}

	@Override
	public int getProbabilityPerThousandHit(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.ProbabilityHitPhysical
				: AttributesTRAn.ProbabilityHitMagical;
		return this.getAttributes().getValue(ai.getIndex());
	}

	@Override
	public int getProbabilityPerThousandCriticalStrike(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalProbability.getIndex());
	}

	@Override
	public int getPercentageCriticalStrikeMultiplier(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalMultiplier.getIndex());
	}

	//

	@Override
	public void setLifeMax(int lifeMax) {
		if (lifeMax > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.LifeMax.getIndex(), lifeMax);
			if (this.getLife() > lifeMax)
				this.setLife(lifeMax);
		}
	}

	@Override
	public void setLifeRegenation(int lifeRegenation) {
		if (lifeRegenation >= 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.RigenLife.getIndex(), lifeRegenation);
		}
	}

	//

	//

	//

	@Override
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent) {
		return maybeDestructionEvent.getName() == EventsTRAr.Destroyed.getName();
	}

	// TODO FIRE EVENTS

	@Override
	public void fireDestructionEvent(GModality gm) {
		GModalityET gmet;
		GEventInterfaceRPG gei;
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
		gmet = (GModalityET) gm;
		gei = (GEventInterfaceRPG) gmet.getEventInterface();
		gei.fireDestructionObjectEvent((GModalityET) gm, this);
	}
}