package dataStructures.isom.matrixBased;

import java.awt.geom.Point2D;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.ObjLocatedCollector;
import geometry.ObjectLocated;
import tools.Comparators;

public class ObjLocatedCollectorMatrix<Distance extends Number> extends PointConsumerRowOptimizer<Distance>
		implements ObjLocatedCollector {
	private static final long serialVersionUID = 1L;

	public ObjLocatedCollectorMatrix(MatrixInSpaceObjectsManager<Distance> misom,
			Predicate<ObjectLocated> targetFilter) {
		super(misom);
		this.targetFilter = targetFilter;
		objFoundBack = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		objFound = objFoundBack.toSetValue(ObjectLocated.KEY_EXTRACTOR);
	}

	protected MapTreeAVL<Integer, ObjectLocated> objFoundBack;
	protected Set<ObjectLocated> objFound;
	protected Predicate<ObjectLocated> targetFilter;

	@Override
	public Predicate<ObjectLocated> getTargetsFilter() {
		return targetFilter;
	}

	@Override
	public Set<ObjectLocated> getCollectedObjects() {
		return objFound;
	}

	@Override
	public NodeIsom getNodeAt(Point2D location) {
		return this.rowCache[(int) location.getX()];
	}

	@Override
	public void acceptImpl(Point2D location) {
		/*
		 * NodeIsom n; n = this.rowCache[(int) t.getX()];
		 * n.forEachAcceptableObject(targetFilter, o -> { if (o == null) return;
		 * objFound.put(o.getID(), o); });
		 */
		ObjLocatedCollector.super.accept(location);
	}

}