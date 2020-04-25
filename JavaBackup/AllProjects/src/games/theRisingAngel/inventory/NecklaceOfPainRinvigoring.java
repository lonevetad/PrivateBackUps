package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;
import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;
import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;

/** See {@link AMoreDamageReceivedMoreLifeRegen} */
public class NecklaceOfPainRinvigoring extends EIJewelry {
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Necklace of Pain Rinvigoring";
	protected AMoreDamageReceivedMoreLifeRegen abilityDamageToLifeRegen;

	public NecklaceOfPainRinvigoring(GModalityRPG gmrpg) {
		super(gmrpg, EquipmentTypesTRAn.Necklace, NAME);
	}

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
		AbilitiesProvider ap;
		ap = ((GameObjectsProvidersHolderRPG) providersHolder).getAbilitiesProvider();
		this.abilityDamageToLifeRegen = (AMoreDamageReceivedMoreLifeRegen) ap.getAbilityByName(gm,
				AMoreDamageReceivedMoreLifeRegen.NAME);
		this.abilityDamageToLifeRegen.setOwner(this);
		super.addAbility(this.abilityDamageToLifeRegen);
	}
}