package games.theRisingAngel.creatures;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.inventoryAbil.EquipmentSet;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.DamageTypeGeneric;
import games.generic.controlModel.misc.HealingType;
import games.generic.controlModel.misc.HealingTypeExample;
import games.generic.controlModel.subimpl.BaseCreatureRPGImpl;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.inventory.EquipmentSetTRAn;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.CreatureAttributesBonusesCalculatorTRAn;
import games.theRisingAngel.misc.DamageTypesTRAn;

public abstract class BaseCreatureTRAn extends BaseCreatureRPGImpl {
	private static final long serialVersionUID = -34551879021102L;

	public BaseCreatureTRAn(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
		this.equipmentSet = newEquipmentSet();
		this.equipmentSet.setCreatureWearingEquipments(this);
		this.addHealingType(HealingTypeExample.Life);
		this.addHealingType(HealingTypeExample.Mana);
	}

	//

	@Override
	public int getTimeSubunitsEachTimeUnits() {
		return 1000;
	}

	@Override
	protected CreatureAttributes newAttributes() {
		CreatureAttributes ca;
		ca = newAttributes(AttributesTRAn.VALUES.length);
		ca.setBonusCalculator(new CreatureAttributesBonusesCalculatorTRAn());
		return ca;
	}

	@Override
	public EquipmentSet newEquipmentSet() {
		return new EquipmentSetTRAn();
	}

	@Override
	public int getLife() {
		return getCurableResourceAmount(HealingTypeExample.Life);
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
	public int getMana() {
		return getCurableResourceAmount(HealingTypeExample.Mana);
	}

	@Override
	public int getManaMax() {
		return this.getAttributes().getValue(AttributesTRAn.ManaMax.getIndex());
	}

	@Override
	public int getManaRegenation() {
		return this.getAttributes().getValue(AttributesTRAn.RigenMana.getIndex());
	}

	@Override
	public int getHealingRegenerationAmount(HealingType healType) {
		if (healType == HealingTypeExample.Life)
			return getLifeRegenation();
		else if (healType == HealingTypeExample.Mana)
			return getManaRegenation();
		return 0;
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

	//

	@Override
	public void setLife(int life) {
//	public void setLife(int life) {this.life = life; }
		if (life > getLifeMax())
			life = getLifeMax();
		if (life <= 0)
			life = (0);
		this.setCurableResourceAmount(HealingTypeExample.Life, life);
	}

	@Override
	public void setMana(int mana) {
		if (mana > getManaMax())
			mana = getManaMax();
		if (mana <= 0)
			mana = (0);
		this.setCurableResourceAmount(HealingTypeExample.Mana, mana);

	}

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

	@Override
	public void setManaMax(int lifeMax) {
		if (lifeMax > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.ManaMax.getIndex(), lifeMax);
			if (this.getMana() > lifeMax)
				this.setMana(lifeMax);
		}
	}

	@Override
	public void setManaRegenation(int lifeRegenation) {
		if (lifeRegenation >= 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.RigenMana.getIndex(), lifeRegenation);
		}
	}

	@Override
	public void setHealingRegenerationAmount(HealingType healType, int value) {
		if (healType == HealingTypeExample.Life)
			setLifeRegenation(value);
		else if (healType == HealingTypeExample.Mana)
			setManaRegenation(value);
	}

	//

	//

	//

	@Override
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent) {
		return maybeDestructionEvent.getName() == EventsTRAn.Destroyed.getName();
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