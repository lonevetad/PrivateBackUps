package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;
import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;
import games.theRisingAngel.inventory.EINotJewelry;
import games.theRisingAngel.inventory.EquipmentTypesTRAn;

/**
 * See {@link ADamageReductionCurrencyBased} , grants 10% of money as damage
 * reduction, for at maximum of 50 of reduction.
 */
public class ArmProtectionShieldingDamageByMoney extends EINotJewelry {
	private static final long serialVersionUID = 4778562323222481L;
	public static final String NAME = "Arm protection of Goldmade Fat Cat";
	protected ADamageReductionCurrencyBased abilityDamageReductionByPaying;

	public ArmProtectionShieldingDamageByMoney(GModalityRPG gmrpg) { super(gmrpg, EquipmentTypesTRAn.Arms, NAME); }

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
		AbilitiesProvider ap;
		ap = ((GameObjectsProvidersHolderRPG) providersHolder).getAbilitiesProvider();
		this.abilityDamageReductionByPaying = (ADamageReductionCurrencyBased) ap.getAbilityByName(gm,
				ADamageReductionCurrencyBased.NAME // + DamageTypesTRAn.Physical.getName()
						+ ADamageReductionCurrencyBased.RARITY);
//		this.abilityDamageReductionByPaying.setPerThousandFraction(100);
//		this.abilityDamageReductionByPaying.setMaximumReduction(50);
		this.abilityDamageReductionByPaying.setOwner(this);
		super.addAbility(this.abilityDamageReductionByPaying);
	}
}