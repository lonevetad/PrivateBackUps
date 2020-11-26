package games.theRisingAngel.creatures;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.damage.DamageTypeGeneric;
import games.generic.controlModel.gEvents.DestructionObjEvent;
import games.generic.controlModel.heal.AHealableResource;
import games.generic.controlModel.heal.HealableResource;
import games.generic.controlModel.heal.IHealableResourceType;
import games.generic.controlModel.heal.resExample.ExampleHealingType;
import games.generic.controlModel.inventoryAbil.EquipmentSet;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.subimpl.BaseCreatureRPGImpl;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.inventory.EquipmentSetTRAn;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.CreatureAttributesTRAn;
import games.theRisingAngel.misc.DamageTypesTRAn;

public // abstract
class BaseCreatureTRAn extends BaseCreatureRPGImpl {
	private static final long serialVersionUID = -34551879021102L;

	public BaseCreatureTRAn(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
		this.setEquipmentSet(newEquipmentSet());
		this.equipmentSet.setCreatureWearingEquipments(this);
	}

	//

	@Override
	protected CreatureAttributes newAttributes() {
		CreatureAttributes ca;
		ca = newAttributes(AttributesTRAn.VALUES.length);
		return ca;
	}

	@Override
	protected CreatureAttributes newAttributes(int attributesAmount) { return new CreatureAttributesTRAn(); }

	@Override
	public void defineAllHealableResources() {
		this.addHealableResource(new HealableResourceTRAn(ExampleHealingType.Life) {
			private static final long serialVersionUID = 78748098434280452L;

			@Override
			public int getAmountMax() { return getLifeMax(); }

			@Override
			public int getRegenerationAmount() { return getLifeRegenation(); }

			@Override
			public void setAmountMax(int resourceAmountMax) { setLifeMax(resourceAmountMax); }

			@Override
			public void setRegenerationAmount(int resourceRegen) { setLifeRegenation(resourceRegen); }
		});
		this.addHealableResource(new HealableResourceTRAn(ExampleHealingType.Mana) {
			private static final long serialVersionUID = 78748098434280452L;

			@Override
			public int getAmountMax() { return getManaMax(); }

			@Override
			public int getRegenerationAmount() { return getManaRegenation(); }

			@Override
			public void setAmountMax(int resourceAmountMax) { setManaMax(resourceAmountMax); }

			@Override
			public void setRegenerationAmount(int resourceRegen) { setManaRegenation(resourceRegen); }
		});
		this.addHealableResource(new HealableResourceTRAn(ExampleHealingType.Shield) {
			private static final long serialVersionUID = 78748098434280452L;

			@Override
			public int getAmountMax() { return getShieldMax(); }

			@Override
			public int getRegenerationAmount() { return getShieldRegenation(); }

			@Override
			public void setAmountMax(int resourceAmountMax) { setShieldMax(resourceAmountMax); }

			@Override
			public void setRegenerationAmount(int resourceRegen) { setShieldRegenation(resourceRegen); }
		});
	}

	@Override
	public EquipmentSet newEquipmentSet() { return new EquipmentSetTRAn(); }

	// TODO GETTER

	@Override
	public int getLife() { return getHealableResourceAmount(ExampleHealingType.Life); }

	@Override
	public int getLifeMax() { return this.getAttributes().getValue(AttributesTRAn.LifeMax); }

	@Override
	public int getLifeRegenation() { return this.getAttributes().getValue(AttributesTRAn.RegenLife); }

	@Override
	public int getMana() { return getHealableResourceAmount(ExampleHealingType.Mana); }

	@Override
	public int getManaMax() { return this.getAttributes().getValue(AttributesTRAn.ManaMax); }

	@Override
	public int getManaRegenation() { return this.getAttributes().getValue(AttributesTRAn.RegenMana); }

	@Override
	public int getShield() { return getHealableResourceAmount(ExampleHealingType.Shield); }

	@Override
	public int getShieldMax() { return this.getAttributes().getValue(AttributesTRAn.ShieldMax); }

	@Override

	public int getShieldRegenation() { return this.getAttributes().getValue(AttributesTRAn.RegenShield); }

	@Override
	public IHealableResourceType getLifeResourceType() { return ExampleHealingType.Life; }

	@Override
	public int getHealableResourceRegeneration(IHealableResourceType healType) {
		if (healType == ExampleHealingType.Life)
			return getLifeRegenation();
		else if (healType == ExampleHealingType.Mana)
			return getManaRegenation();
		else if (healType == ExampleHealingType.Shield)
			return getShieldRegenation();
		return 0;
	}

	@Override
	public int getHealableResourceMax(IHealableResourceType healType) {
		if (healType == ExampleHealingType.Life)
			return getLifeMax();
		else if (healType == ExampleHealingType.Mana)
			return getManaMax();
		else if (healType == ExampleHealingType.Shield)
			return getShieldMax();
		return 0;
	}

	// TODO damage dealer receiver

	@Override
	public int getProbabilityPerThousandAvoid(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.ProbabilityPerThousandAvoidPhysical
				: AttributesTRAn.ProbabilityPerThousandAvoidMagical;
		return this.getAttributes().getValue(ai);
	}

	@Override
	public int getProbabilityPerThousandHit(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.ProbabilityPerThousandHitPhysical
				: AttributesTRAn.ProbabilityPerThousandHitMagical;
		return this.getAttributes().getValue(ai);
	}

	@Override
	public int getProbabilityPerThousandCriticalStrike(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalProbabilityPerThousand);
	}

	@Override
	public int getPercentageCriticalStrikeMultiplier(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalMultiplierPercentage);
	}

	@Override
	public int getProbabilityPerThousandAvoidCritical(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalProbabilityPerThousandAvoid);
	}

	/** Percentage amount of multiplication of the damage. Should be positive. */
	@Override
	public int getPercentageCriticalStrikeReduction(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalMultiplierPercentageReduction);
	}

	//

	//

	@Override
	public void setLife(int life) {
//	public void setLife(int life) {this.life = life; }
		if (life > getLifeMax())
			life = getLifeMax();
		if (life <= 0)
			life = 0;
		this.setHealableResourceAmount(ExampleHealingType.Life, life);
		if (getLife() <= 0) {
			DestructionObjEvent de;
			de = fireDestructionEvent(getgModalityRPG());
			if (de == null || de.isDestructionValid()) { destroy(); }
		}
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
			this.getAttributes().setOriginalValue(AttributesTRAn.RegenLife.getIndex(), lifeRegenation);
		}
	}

	@Override
	public void setMana(int mana) {
		if (mana > getManaMax())
			mana = getManaMax();
		if (mana <= 0)
			mana = (0);
		this.setHealableResourceAmount(ExampleHealingType.Mana, mana);

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
			this.getAttributes().setOriginalValue(AttributesTRAn.RegenMana.getIndex(), lifeRegenation);
		}
	}

	@Override
	public void setShield(int shield) {
		if (shield > getShieldMax())
			shield = getShieldMax();
		if (shield <= 0)
			shield = (0);
		this.setHealableResourceAmount(ExampleHealingType.Shield, shield);

	}

	@Override
	public void setShieldMax(int lifeMax) {
		if (lifeMax > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.ShieldMax.getIndex(), lifeMax);
			if (this.getShield() > lifeMax)
				this.setShield(lifeMax);
		}
	}

	@Override
	public void setShieldRegenation(int lifeRegenation) {
		if (lifeRegenation >= 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.RegenShield.getIndex(), lifeRegenation);
		}
	}

	@Override
	public void setHealableResourceAmountMax(IHealableResourceType healType, int value) {
		if (healType == ExampleHealingType.Life)
			setLifeRegenation(value);
		else if (healType == ExampleHealingType.Mana)
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
	public DestructionObjEvent fireDestructionEvent(GModality gm) {
		GModalityET gmet;
		GEventInterfaceRPG gei;
		if (gm == null || (!(gm instanceof GModalityET)))
			return null;
		gmet = (GModalityET) gm;
		gei = (GEventInterfaceRPG) gmet.getEventInterface();
		return gei.fireDestructionObjectEvent(gmet, this);
	}

	//

	// TODO CLASS

	/**
	 * Default implementation that let to be defined
	 * {@link HealableResource#getResourceAmountMax()} and
	 * {@link HealableResource#getResourceRegen()}.
	 */
	public abstract class HealableResourceTRAn extends AHealableResource {
		private static final long serialVersionUID = 54405L;

		public HealableResourceTRAn(IHealableResourceType ht) { super(ht); }

		protected int amount;

		@Override
		public int getAmount() { return amount; }

		@Override
		public void setAmount(int resourceAmount) {
			int max;
			max = getAmountMax();
			if (resourceAmount < 0 && (!this.resourceType.acceptsNegative())) { resourceAmount = 0; }
			if (resourceAmount > max) { resourceAmount = max; }
			this.amount = resourceAmount;
		}

		@Override
		public IHealableResourceType getResourceType() { return resourceType; }
	}
}