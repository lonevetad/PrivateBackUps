package dataStructures.isom.matrixBased.pfMatrix;

import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.PathFinderIsomFrontierBased;

/** It's basically useless */
@Deprecated
public abstract class PathFinderIsomMatrix<Distance extends Number> implements PathFinderIsomFrontierBased<Distance> {

	public PathFinderIsomMatrix(
			// MatrixInSpaceObjectsManager<Distance>
			InSpaceObjectsManager<Distance> matrix) {
//this.isSynchronized = isSynchronized;boolean isSynchronized,
		this.isom = matrix;
	}

//protected final boolean isSynchronized;

	protected final InSpaceObjectsManager<Distance> isom; // MatrixInSpaceObjectsManager<Distance> matrix;

//public boolean isSynchronized() {return isSynchronized;}

	public InSpaceObjectsManager<Distance> getSpaceToRunThrough() { return isom; }

//

	public class NodeInfoMatrix extends NodeInfoFrontierBased<Distance> {

		protected NodeInfoMatrix(NodeIsom thisNode) { super(thisNode); }

	}
}