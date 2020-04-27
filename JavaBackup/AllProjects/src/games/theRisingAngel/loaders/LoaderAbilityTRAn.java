package games.theRisingAngel.loaders;

import games.generic.controlModel.GController;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.subimpl.LoaderAbilities;
import games.theRisingAngel.GModalityTRAn;
import games.theRisingAngel.abilities.ADamageReductionCurrencyBased;
import games.theRisingAngel.abilities.AFireShpereOrbiting;
import games.theRisingAngel.abilities.AHealingMakesEarnBaseCurrency;
import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;
import games.theRisingAngel.abilities.ARandomScatteringOrbs;
import games.theRisingAngel.abilities.ARandomScatteringOrbsIMpl;
import games.theRisingAngel.abilities.AShiedlingButWeakining;
import games.theRisingAngel.misc.DamageTypesTRAn;

public class LoaderAbilityTRAn extends LoaderAbilities {

	public LoaderAbilityTRAn(GameObjectsProvider<AbilityGeneric> objProvider) {
		super(objProvider);
	}

	@Override
	public void loadInto(GController gcontroller) {
		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAn.Physical.getName(),
				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAn.Physical));
		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAn.Magical.getName(),
				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAn.Magical));
		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, gc -> new AMoreDamageReceivedMoreLifeRegen());
		objProvider.addObj(AFireShpereOrbiting.NAME, gc -> new AFireShpereOrbiting());
		objProvider.addObj(AShiedlingButWeakining.NAME, gm -> new AShiedlingButWeakining());
		objProvider.addObj(AHealingMakesEarnBaseCurrency.NAME, gm -> new AHealingMakesEarnBaseCurrency());
		objProvider.addObj(ARandomScatteringOrbs.NAME, gm -> new ARandomScatteringOrbsIMpl((GModalityTRAn) gm));
	}
}