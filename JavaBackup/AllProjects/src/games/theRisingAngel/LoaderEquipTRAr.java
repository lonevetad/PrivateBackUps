package games.theRisingAngel;

import games.generic.controlModel.GController;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.misc.LoaderEquipments;
import games.generic.controlModel.misc.ObjGModalityBasedProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.inventory.ArmProtectionShieldingDamageByMoney;
import games.theRisingAngel.inventory.HelmetOfPlanetaryMeteors;
import games.theRisingAngel.inventory.NecklaceOfPainRinvigoring;

public class LoaderEquipTRAr extends LoaderEquipments {

	public LoaderEquipTRAr(ObjGModalityBasedProvider<EquipmentItem> objProvider) {
		super(objProvider);
	}

	@Override
	public void loadInto(GController gc) {
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Physical));
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Magical));
//		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, gmm -> new AMoreDamageReceivedMoreLifeRegen());
		objProvider.addObj(ArmProtectionShieldingDamageByMoney.NAME,
				(gm) -> new ArmProtectionShieldingDamageByMoney((GModalityRPG) gm));
		objProvider.addObj(NecklaceOfPainRinvigoring.NAME, //
				(gm) -> new NecklaceOfPainRinvigoring((GModalityRPG) gm));
		objProvider.addObj(HelmetOfPlanetaryMeteors.NAME, (gm) -> new HelmetOfPlanetaryMeteors((GModalityRPG) gm));

		// TODO
	}

}