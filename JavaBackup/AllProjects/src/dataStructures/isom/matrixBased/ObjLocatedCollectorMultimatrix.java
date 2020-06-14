package dataStructures.isom.matrixBased;

import java.awt.Point;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.isom.MultiISOMRetangularMap;
import dataStructures.isom.MultiISOMRetangularMap.NodeIsomProviderCachingMISOM;
import dataStructures.isom.MultiISOMRetangularMap.NodeIsomProviderCachingMISOM.MISOMAndLocation;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.ObjLocatedCollectorIsom;
import geometry.ObjectLocated;

/**
 * An {@link ObjLocatedCollectorIsom} that benefits from 2 types of caches:
 * <ul>
 * <li>{@link MatrixInSpaceObjectsManager} in case of use under multi-"misom"
 * environment (i.e.: {@link MultiISOMRetangularMap}), cached tanks to
 * {@link NodeIsomProviderCachingMISOM}.</li>
 * <li>{@link NodeIsom}, cached thanks to {@link PointConsumerRowOptimizer}
 * (it's a inner instance, not a superclass).</li>
 * </ul>
 */
public class ObjLocatedCollectorMultimatrix<Distance extends Number> implements ObjLocatedCollectorIsom {
	private static final long serialVersionUID = 43L;

	public ObjLocatedCollectorMultimatrix(NodeIsomProviderCachingMISOM<Distance> multiMatrix,
			Predicate<ObjectLocated> targetFilter) {
		this.isomProvider = multiMatrix;
		this.olcm = new ObjLocatedCollectorMatrix<Distance>(null, targetFilter);
	}

	protected NodeIsomProviderCachingMISOM<Distance> isomProvider;
	protected ObjLocatedCollectorMatrix<Distance> olcm; // this is the implementation for SINGLE matrix!!

	@Override
	public void accept(Point location) {
		MISOMAndLocation<Distance> ml;
		ml = isomProvider.getMisomAt(location);
		if (ml == null)
			return; // no null allowed here!
		if (ml.misom != olcm.getMisom()) {
			// reset the row-cache
			olcm.setMisom(ml.misom);
		}
		// shift the point to the offset
		location = new Point(location.x - ml.locationMisom.x, location.y - ml.locationMisom.y);
		olcm.accept(location);
	}

	// proxies

	@Override
	public Predicate<ObjectLocated> getTargetsFilter() { return olcm.targetFilter; }

	@Override
	public Set<ObjectLocated> getCollectedObjects() { return olcm.getCollectedObjects(); }

	@Override
	public NodeIsomProvider getNodeIsomProvider() { return isomProvider; }

	@SuppressWarnings("unchecked")
	@Override
	public void setNodeIsomProvider(NodeIsomProvider nodeIsomProvider) {
		if (nodeIsomProvider instanceof NodeIsomProviderCachingMISOM<?>)
			this.isomProvider = (NodeIsomProviderCachingMISOM<Distance>) nodeIsomProvider;
	}

}