package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AOrbitingSpawningBlobs;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.misc.DamageTypeGeneric;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.GModalityTRAn;
import geometry.AbstractShape2D;
import geometry.ObjectShaped;
import geometry.implementations.shapes.ShapeCircle;
import tools.UniqueIDProvider;

/**
 * Spawns orbs rotating over a point and dealing damage. Adds a 10% of
 * probability of hitting.
 */
public abstract class AOrbitingDamagingObj extends AOrbitingSpawningBlobs {
	private static final long serialVersionUID = 1L;
	protected static final int HIT_PROBAB_PER_THOUSAND_BONUS = 100;

	public AOrbitingDamagingObj() {
		super();
		idProvOrbs = UniqueIDProvider.newBasicIDProvider();
	}

	protected DamageGeneric damageToDeal;
	protected UniqueIDProvider idProvOrbs;

	public DamageGeneric getDamageToDeal() {
		return damageToDeal;
	}

	public void setDamageToDeal(DamageGeneric damageToDeal) {
		this.damageToDeal = damageToDeal;
	}

	//

	@Override
	protected ObjectShaped newOrbitingObj() {
		return new OrbDamaging();
	}

	@Override
	protected boolean isValidTarget(ObjectShaped possibleTarget) {
		return possibleTarget instanceof CreatureSimple;
	}

	@Override
	protected void interactWith(GModality modality, int index, ObjectShaped os, ObjectShaped target) {
		GModalityTRAn gmtrar;
		GameObjectsManager gom;
		gmtrar = (GModalityTRAn) modality;
		gom = gmtrar.getGameObjectsManager();
		gom.dealsDamageTo((DamageDealerGeneric) os, (CreatureSimple) target, damageToDeal);
	}

	protected class OrbDamaging implements ObjectShaped, DamageDealerGeneric {
		Integer ID;
		AbstractShape2D shape;

		public OrbDamaging() {
			super();
			ID = idProvOrbs.getNewID();
			shape = new ShapeCircle(0, 0, true, GModalityRPG.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE);
		}

		@Override
		public Integer getID() {
			return ID;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public int getProbabilityPerThousandHit(DamageTypeGeneric damageType) {
			int p;
			DamageDealerGeneric caster;
			p = HIT_PROBAB_PER_THOUSAND_BONUS;
			if (owner instanceof DamageDealerGeneric) {
				caster = (DamageDealerGeneric) owner;
				p += caster.getPercentageCriticalStrikeMultiplier(damageType);
			}
			return p;
		}

		@Override
		public int getProbabilityPerThousandCriticalStrike(DamageTypeGeneric damageType) {
			return 0;
		}

		@Override
		public int getPercentageCriticalStrikeMultiplier(DamageTypeGeneric damageType) {
			return 0;
		}

		@Override
		public AbstractShape2D getShape() {
			return null;
		}

		@Override
		public void setShape(AbstractShape2D shape) {
			// TODO Auto-generated method stub

		}

	}
}