package games.theRisingAngel;

import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GControllerRPG;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;
import games.theRisingAngel.loaders.LoaderAbilityTRAn;
import games.theRisingAngel.loaders.LoaderCreatureTRAn;
import games.theRisingAngel.loaders.LoaderEquipTRAn;
import games.theRisingAngel.loaders.LoaderEquipUpgradesTRAn;

public class GControllerTRAn extends GControllerRPG {
	public static final String GM_NAME_TRAR_BASE = "gc_tran_base";

	public GControllerTRAn() { super(); }

	protected GameOptionsTRAn gameOptionsTRAn;

	//

	public GameOptionsTRAn getGameOptionsTRAn() { return gameOptionsTRAn; }

	//

	public void setGameOptionsTRAn(GameOptionsTRAn gameOptionsTRAn) { this.gameOptionsTRAn = gameOptionsTRAn; }

	//

	@Override
	protected GameObjectsProvidersHolderRPG newGameObjectsProvider() {
		return new GameObjectsProvidersHolderTRAn(null);
	}

	@Override
	protected void defineGameModalitiesFactories() {
		this.getGameModalitiesFactories().put(GM_NAME_TRAR_BASE, (name, gc) -> { return new GModalityTRAn(name, gc); });
	}

	@Override
	protected UserAccountGeneric newUserAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initNonFinalStuffs() {
		((GameObjectsProvidersHolderTRAn) getGameObjectsProvider())
				.setgModality((GModalityRPG) getCurrentGameModality());
		super.addGameObjectLoader(new LoaderAbilityTRAn(this.gameObjectsProvidersHolderRPG.getAbilitiesProvider()));
		super.addGameObjectLoader(
				new LoaderEquipUpgradesTRAn(this.gameObjectsProvidersHolderRPG.getEquipUpgradesProvider()));
		super.addGameObjectLoader(new LoaderEquipTRAn(this.gameObjectsProvidersHolderRPG.getEquipmentsProvider()));
		super.addGameObjectLoader(new LoaderCreatureTRAn(this.gameObjectsProvidersHolderRPG.getCreaturesProvider()));

		super.initNonFinalStuffs();
		System.out.println("GControllerTRAn init non final stuff done\n\n");
//		this.gameObjectsProvidersHolderRPG.getEquipmentsProvider().getObjectsIdentified().forEach((n, f) -> {
//			System.out.println("daffaking equip name: " + n);
//		});
	}

}