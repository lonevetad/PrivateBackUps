package games.theRisingAngel.loaders;

import java.util.List;

import games.generic.controlModel.GController;
import games.generic.controlModel.loaders.LoaderConfigurations;
import games.generic.controlModel.loaders.LoaderGMod;
import games.generic.controlModel.loaders.LoaderManager;
import games.generic.controlModel.loaders.LoaderUniqueIDProvidersState;
import games.generic.mods.GModInterface;

public class LoaderManagerTRAn extends LoaderManager {

	public LoaderManagerTRAn(GController gameController) { super(gameController); }

	@Override
	protected LoaderUniqueIDProvidersState newLoaderUniqueIDProvidersState() { // TODO Auto-generated method stub
		return new LoaderUniqueIDProvidersStateTRAn();
	}

	@Override
	protected LoaderConfigurations newLoaderConfigurations() { return new LoaderConfigurationsTRAn(); }

	@Override
	protected LoaderGMod newLoaderGameMods() { // TODO Auto-generated method stub
		return new LoaderGMod(this.getGameController()) {

			@Override
			public LoadStatusResult loadInto(GController gc) { // TODO Auto-generated method stub
				System.out.println("mmmmmmmmmmmmmmmmmmmmMOD MANAGER TRAN");
				return LoadStatusResult.Success;
			}

			@Override
			public List<GModInterface> getAllLoadableGameMods() { // TODO Auto-generated method stub
				return null;
			}
		};
	}
}