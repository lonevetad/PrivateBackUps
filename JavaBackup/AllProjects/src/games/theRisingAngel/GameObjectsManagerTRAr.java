package games.theRisingAngel;

import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.subimpl.GObjectsInSpaceManagerImpl;

public class GameObjectsManagerTRAr implements GameObjectsManager {

	public GameObjectsManagerTRAr(GModalityTRAr gmodalityTrar) {
		super();
		setGameModality(gmodalityTrar);
		this.goism = new GObjectsInSpaceManagerImpl();
	}

	protected GModalityTRAr gmodalityTrar;
	protected GObjectsInSpaceManager goism;

	@Override
	public GModality getGameModality() {
		return gmodalityTrar;
	}

	@Override
	public void setGameModality(GModality gameModality) {
		this.gmodalityTrar = (GModalityTRAr) gameModality;
	}

	@Override
	public GObjectsInSpaceManager getGObjectInSpaceManager() {
		return goism;
	}

	@Override
	public GEventInterface getGEventInterface() {
		return gmodalityTrar.getEventInterface();
	}

	@Override
	public void setGObjectsInSpaceManager(GObjectsInSpaceManager gisom) {
		this.goism = gisom;
	}

	@Override
	public void setGEventInterface(GEventInterface gei) {
//		this.gei = (GEventInterfaceTRAr) gei;
		gmodalityTrar.setEventInterface(gei);
	}
}