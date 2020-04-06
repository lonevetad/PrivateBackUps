package dataStructures.isom.matrixBased;

import java.awt.geom.Point2D;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.isom.ObjLocatedCollector;
import geometry.ObjectLocated;
import tools.Comparators;

public class ObjLocatedCollectorMatrix extends PointConsumerRowOptimizer implements ObjLocatedCollector {
	private static final long serialVersionUID = 1L;

	public ObjLocatedCollectorMatrix(MatrixInSpaceObjectsManager misom, Predicate<ObjectLocated> targetFilter) {
		super(misom);
		this.targetFilter = targetFilter;
		objFound = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
	}

	protected MapTreeAVL<Integer, ObjectLocated> objFound;
	protected Predicate<ObjectLocated> targetFilter;

	@Override
	public Predicate<ObjectLocated> getTargetsFilter() {
		return targetFilter;
	}

	@Override
	public Set<ObjectLocated> getCollectedObjects() {
		return objFound.toSetValue(ObjectLocated.KEY_EXTRACTOR);
	}

	@Override
	public void acceptImpl(Point2D t) {
		ObjectLocated o;
		NodeIsomSingleObj n;
		n = (NodeIsomSingleObj) this.rowCache[(int) t.getX()];
		o = n.objectLying;
		if (o == null)
			return;
		if (targetFilter == null || targetFilter.test(o)) {
			objFound.put(o.getID(), o);
		}
	}
}