package tests.tGame.tgEvent1.oggettiDesempio;

import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;
import games.theRisingAngel.inventory.EIJewelry;
import games.theRisingAngel.inventory.EquipmentTypesTRAr;

/** See {@link AMoreDamageReceivedMoreLifeRegen} */
public class NecklaceOfPainRinvigoring extends EIJewelry {
	AMoreDamageReceivedMoreLifeRegen abilityDamageToLifeRegen;

	public NecklaceOfPainRinvigoring() {
		super(EquipmentTypesTRAr.Necklace);
		this.abilityDamageToLifeRegen = new AMoreDamageReceivedMoreLifeRegen();
		this.abilityDamageToLifeRegen.setOwner(this);
		super.addAbility(this.abilityDamageToLifeRegen);
	}
}