package tests.tGame.tgEvent1.oggettiDesempio;

import games.theRisingAngel.abilities.ADamageReductionPhysicalCurrencyBased;
import games.theRisingAngel.inventory.EINotJewelry;
import games.theRisingAngel.inventory.EquipmentTypesTRAr;

/**
 * See {@link ADamageReductionPhysicalCurrencyBased} , grants 10% of money as
 * damage reduction.
 */
public class ArmProtectionShieldingDamageByMoney extends EINotJewelry {

	ADamageReductionPhysicalCurrencyBased abilityDamageReductionByPaying;

	public ArmProtectionShieldingDamageByMoney() {
		super(EquipmentTypesTRAr.Arms);
		this.abilityDamageReductionByPaying = new ADamageReductionPhysicalCurrencyBased();
		this.abilityDamageReductionByPaying.setPerThousandFraction(100);
		super.addAbility(this.abilityDamageReductionByPaying);
	}
}