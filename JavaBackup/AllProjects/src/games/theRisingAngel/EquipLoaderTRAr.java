package games.theRisingAngel;

import games.generic.controlModel.GController;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.misc.LoaderEquipments;
import games.generic.controlModel.misc.ObjGModalityBasedProvider;
import games.generic.controlModel.subImpl.GModalityRPG;
import games.theRisingAngel.inventory.ArmProtectionShieldingDamageByMoney;

public class EquipLoaderTRAr extends LoaderEquipments {

	public EquipLoaderTRAr(ObjGModalityBasedProvider<EquipmentItem> objProvider) {
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
	}

}