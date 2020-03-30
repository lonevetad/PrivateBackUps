package games.theRisingAngel.inventory;

import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.subImpl.GModalityRPG;
import games.theRisingAngel.abilities.ADamageReductionCurrencyBased;

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
	protected void enrichWithAbilities(AbilitiesProvider ap) {
		this.abilityDamageReductionByPaying = (ADamageReductionCurrencyBased) ap.getAbilityByName(null,
				ADamageReductionCurrencyBased.NAME);
		this.abilityDamageReductionByPaying.setPerThousandFraction(100);
		this.abilityDamageReductionByPaying.setOwner(this);
		super.addAbility(this.abilityDamageReductionByPaying);
	}

}