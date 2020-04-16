package games.generic.controlModel.inventoryAbil;

import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;

public class EquipmentItemImpl extends EquipmentItem {
	private static final long serialVersionUID = -56956980L;

	public EquipmentItemImpl(GModalityRPG gmrpg, EquipmentType equipmentType, String name, List<String> abilitiesName) {
		super(gmrpg, equipmentType, name);
		this.abilitiesName = abilitiesName;
		onCreateAfterAssigningAbilitiesName(gmrpg);
	}

	protected List<String> abilitiesName; // TODO what about

	public List<String> getAbilitiesName() {
		return abilitiesName;
	}

	public void setAbilitiesName(List<String> abilitiesName) {
		this.abilitiesName = abilitiesName;
	}

	@Override
	protected void onCreate(GModality gm) {
		// nullify the super.onCreate(gm);
	}

	protected void onCreateAfterAssigningAbilitiesName(GModality gm) {
		super.onCreate(gm);
	}

	@Override
	protected void enrichWithAbilities(GModality gm, GameObjectsProvidersHolder providersHolder) {
		AbilityGeneric a;
		AbilitiesProvider ap;
		GameObjectsProvidersHolderRPG prpg;
		prpg = (GameObjectsProvidersHolderRPG) providersHolder;
//		super.enr
		ap = prpg.getAbilitiesProvider();
		if (this.abilitiesName != null) {
			for (String abilityName : this.abilitiesName) {
				a = ap.getNewObjByName(gm, abilityName);
				this.addAbility((EquipItemAbility) a);
			}
			this.abilitiesName = null;
		}
	}

}