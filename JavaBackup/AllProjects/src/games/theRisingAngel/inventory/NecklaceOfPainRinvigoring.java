package games.theRisingAngel.inventory;

import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.subImpl.GModalityRPG;
import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;

/** See {@link AMoreDamageReceivedMoreLifeRegen} */
public class NecklaceOfPainRinvigoring extends EIJewelry {
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Necklace of Pain Rinvigoring";
	protected AMoreDamageReceivedMoreLifeRegen abilityDamageToLifeRegen;

	public NecklaceOfPainRinvigoring(GModalityRPG gmrpg, EquipmentTypesTRAr et, String name) {
		super(gmrpg, EquipmentTypesTRAr.Necklace, NAME);
	}

	@Override
	protected void enrichWithAbilities(AbilitiesProvider ap) {
		this.abilityDamageToLifeRegen = (AMoreDamageReceivedMoreLifeRegen) ap.getAbilityByName(null,
				AMoreDamageReceivedMoreLifeRegen.NAME);
		this.abilityDamageToLifeRegen.setOwner(this);
		super.addAbility(this.abilityDamageToLifeRegen);
	}

}