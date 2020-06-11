package geometry;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.isom.ObjLocatedCollectorIsom;
import geometry.pointTools.impl.ObjCollector;
import tests.tDataStruct.Test_MultiISOMRetangularMap_V1;

/** Refers to {@link Test_MultiISOMRetangularMap_V1}. */
public abstract class AbstractMultiOISMRectangular implements AbstractObjectsInSpaceManager {
	private static final long serialVersionUID = 1L;

	public AbstractMultiOISMRectangular() {
		// TODO Auto-generated constructor stub }
	}

	@Override
	public Iterator<ObjectLocated> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractShape2D getBoundingShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProviderShapeRunner getProviderShapeRunner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) { // TODO Auto-generated method stub
	}

	@Override
	public boolean add(ObjectLocated o) { // TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(ObjectLocated o) { // TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(ObjectLocated o) { // TODO Auto-generated method stub
		return false;
	}

	@Override
	public ObjLocatedCollectorIsom newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) { // TODO
																									// Auto-generated
																									// method stub
		return null;
	}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, ObjCollector<ObjectLocated> collector,
			List<Point> path) {
		// TODO Auto-generated method stub
		return null;
	}

}
