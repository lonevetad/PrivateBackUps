package games.theRisingAngel.inventory;

import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;

/** See {@link AMoreDamageReceivedMoreLifeRegen} */
public class NecklaceOfPainRinvigoring extends EIJewelry {
	private static final long serialVersionUID = 1L;
	AMoreDamageReceivedMoreLifeRegen abilityDamageToLifeRegen;

	public NecklaceOfPainRinvigoring() {
		super(EquipmentTypesTRAr.Necklace, "Necklace of Pain Rinvigoring");
		this.abilityDamageToLifeRegen = new AMoreDamageReceivedMoreLifeRegen();
		this.abilityDamageToLifeRegen.setOwner(this);
		super.addAbility(this.abilityDamageToLifeRegen);
	}
}