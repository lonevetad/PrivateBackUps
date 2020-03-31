package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.OrbitingSpawningBlobs;
import games.generic.controlModel.misc.DamageGeneric;
import games.theRisingAngel.GModalityTRAr;
import geometry.ObjectShaped;

public abstract class AOrbitingDamagingObj extends OrbitingSpawningBlobs {
	private static final long serialVersionUID = 1L;

	public AOrbitingDamagingObj() {
		super();
	}

	protected DamageGeneric damageToDeal;

	public DamageGeneric getDamageToDeal() {
		return damageToDeal;
	}

	public void setDamageToDeal(DamageGeneric damageToDeal) {
		this.damageToDeal = damageToDeal;
	}

	//

	@Override
	protected boolean isValidTarget(ObjectShaped possibleTarget) {
		return possibleTarget instanceof CreatureSimple;
	}

	@Override
	protected void interactWith(GModality modality, int index, ObjectShaped os, ObjectShaped target) {
		GModalityTRAr gmtrar;
		GameObjectsManager gom;
		gmtrar = (GModalityTRAr) modality;
		gom = gmtrar.getGameObjectsManagerDelegated();
		gom.dealsDamageTo(os, (CreatureSimple) target, damageToDeal);
	}

}