package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;
import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;
import games.theRisingAngel.abilities.ADamageReductionCurrencyBased;
import games.theRisingAngel.misc.DamageTypesTRAr;

/**
 * See {@link ADamageReductionCurrencyBased} , grants 10% of money as damage
 * reduction.
 */
public class ArmProtectionShieldingDamageByMoney extends EINotJewelry {
	private static final long serialVersionUID = 4778562323222481L;
	public static final String NAME = "Arm protection of Goldmade Fat Cat";
	protected ADamageReductionCurrencyBased abilityDamageReductionByPaying;

	public ArmProtectionShieldingDamageByMoney(GModalityRPG gmrpg) {
		super(gmrpg, EquipmentTypesTRAr.Arms, NAME);
	}

	@Override
	protected void enrichWithAbilities(GModality gm, GameObjectsProvidersHolder providersHolder) {
		AbilitiesProvider ap;
		ap = ((GameObjectsProvidersHolderRPG) providersHolder).getAbilitiesProvider();
		this.abilityDamageReductionByPaying = (ADamageReductionCurrencyBased) ap.getAbilityByName(gm,
				ADamageReductionCurrencyBased.NAME + DamageTypesTRAr.Physical.getName());
		this.abilityDamageReductionByPaying.setPerThousandFraction(100);
		this.abilityDamageReductionByPaying.setOwner(this);
		super.addAbility(this.abilityDamageReductionByPaying);
	}
}