package games.theRisingAngel.loaders;

import games.generic.controlModel.GController;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.subimpl.LoaderAbilities;
import games.theRisingAngel.abilities.ADamageReductionCurrencyBased;
import games.theRisingAngel.abilities.AFireShpereOrbiting;
import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;
import games.theRisingAngel.misc.DamageTypesTRAr;

public class LoaderAbilityTRAr extends LoaderAbilities {

	public LoaderAbilityTRAr(GameObjectsProvider<AbilityGeneric> objProvider) {
		super(objProvider);
	}

	@Override
	public void loadInto(GController gcontroller) {
		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAr.Physical.getName(),
				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Physical));
		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAr.Magical.getName(),
				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Magical));
		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, gc -> new AMoreDamageReceivedMoreLifeRegen());
		objProvider.addObj(AFireShpereOrbiting.NAME, gc -> new AFireShpereOrbiting());

	}

}