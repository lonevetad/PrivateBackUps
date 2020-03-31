package games.theRisingAngel;

import games.generic.controlModel.GController;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.misc.LoaderGameObjects;
import games.generic.controlModel.misc.ObjGModalityBasedProvider;

public class LoaderCreatureTRAr extends LoaderGameObjects<BaseCreatureRPG> {

	public LoaderCreatureTRAr(ObjGModalityBasedProvider<BaseCreatureRPG> objProvider) {
		super(objProvider);
	}

	@Override
	public void loadInto(GController gc) {
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Physical));
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Magical));
//		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, gmm -> new AMoreDamageReceivedMoreLifeRegen());
	}
}