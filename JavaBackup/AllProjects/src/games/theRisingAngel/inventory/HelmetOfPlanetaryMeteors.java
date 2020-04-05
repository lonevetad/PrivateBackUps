package games.theRisingAngel.inventory;

import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.DamageTypesTRAr;
import games.theRisingAngel.abilities.AFireShpereOrbiting;

public class HelmetOfPlanetaryMeteors extends EINotJewelry {
	private static final long serialVersionUID = 922120283L;

	public static final String NAME = "Helmet Of Planetary Meteors";

	public HelmetOfPlanetaryMeteors(GModalityRPG gmrpg) {
		super(gmrpg, EquipmentTypesTRAr.Head, NAME);
		// TODO Auto-generated constructor stub
	}

	protected AFireShpereOrbiting abilitySphereOrbiting;

	@Override
	protected void enrichWithAbilities(AbilitiesProvider ap) {
		// TODO Auto-generated method stub
		this.abilitySphereOrbiting = (AFireShpereOrbiting) ap.getAbilityByName(null, AFireShpereOrbiting.NAME);
		this.abilitySphereOrbiting.setOwner(this);
		abilitySphereOrbiting.setDamageToDeal(new DamageGeneric(15, DamageTypesTRAr.Magical));
		super.addAbility(this.abilitySphereOrbiting);
	}

}
