package dataStructures.isom;

import java.awt.Point;

import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import dataStructures.isom.pathFinders.PathFinderIsomDijkstra;
import geometry.AbstractShapeRunner;

/**
 * Implementation of {@link NodeIsomProvider} optimized for iterating (as
 * {@link AbstractShapeRunner} subclasses does) over sets of {@link Point} (used
 * to obtain {@link NodeIsom}).<br>
 * It's optimized because it caches the instances of
 * {@link MatrixInSpaceObjectsManager}
 */
public class MultiISOMRetangularCaching<Dd extends Number> extends MultiISOMRetangularMap<Dd> {
	private static final long serialVersionUID = -58691725492810L;

	public MultiISOMRetangularCaching() {
		super();
		this.cachedMW = null;
		setPathFinder(new PathFinderIsomDijkstra<>(this));
	}

	public MultiISOMRetangularCaching(int maximumSubmapsEachSection) {
		super(maximumSubmapsEachSection);
		this.cachedMW = null;
	}

	protected MISOMLocatedInSpace<Dd> cachedMW;

	public MatrixInSpaceObjectsManager<Dd> getCachedMisom() { return cachedMW == null ? null : cachedMW.misom; }

	///

	@Override
	public MISOMLocatedInSpace<Dd> getMapLocatedContaining(int x, int y) {
		if (cachedMW == null || (!cachedMW.contains(x, y))) { cachedMW = super.getMapLocatedContaining(x, y); }
		return cachedMW;
	}
}