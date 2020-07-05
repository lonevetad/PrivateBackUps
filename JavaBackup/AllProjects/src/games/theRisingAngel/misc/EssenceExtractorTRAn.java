package games.theRisingAngel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.inventoryAbil.EquipmentUpgradesProvider;
import games.generic.controlModel.misc.EssenceExtractor;

public class EssenceExtractorTRAn extends EssenceExtractor {

	public EssenceExtractorTRAn() {}

	@Override
	public String getEquipmentUpgradeObjProviderName(GModality gm) { return EquipmentUpgradesProvider.NAME; }

	@Override
	public String getAbilityObjProviderName(GModality gm) { return AbilitiesProvider.NAME; }
}