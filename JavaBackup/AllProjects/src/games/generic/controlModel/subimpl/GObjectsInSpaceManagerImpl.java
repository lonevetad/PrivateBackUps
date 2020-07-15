package games.generic.controlModel.subimpl;

import java.util.Set;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.gObj.ObjectInSpace;
import geometry.pointTools.HeuristicManhattan;
import oldToBeDeleted.PathFinderIsomAStar_Naive;
import tools.ObjectWithID;

/**
 * Based on a {@link InSpaceObjectsManager}.
 */
public abstract class GObjectsInSpaceManagerImpl implements GObjectsInSpaceManager {

	public GObjectsInSpaceManagerImpl(InSpaceObjectsManager<Double> isom) {
		objWID = null;
		this.isom = isom;
		this.isom.setPathFinder(new PathFinderIsomAStar_Naive<Double>(this.isom, HeuristicManhattan.SINGLETON));
	}

	protected Set<ObjectWithID> objWID;
	protected InSpaceObjectsManager<Double> isom;
	protected GModality gameModality;

	@Override
	public InSpaceObjectsManager<Double> getOIMManager() { return isom; }

	@Override
	public GModality getGameModality() { return gameModality; }

	@Override
	public Set<ObjectWithID> getObjects() {
		if (this.objWID == null) { this.objWID = GObjectsInSpaceManager.super.getObjects(); }
		return this.objWID;
	}

	@Override
	public void setGameModality(GModality gameModality) { this.gameModality = gameModality; }

	@Override
	public int objectsHeldCount() { return this.objWID.size(); }

	@Override
	public boolean contains(ObjectWithID o) { return (o == null) ? false : this.getObjects().contains(o); }

	@Override
	public boolean containsObject(ObjectInSpace o) { return contains(o); }
}