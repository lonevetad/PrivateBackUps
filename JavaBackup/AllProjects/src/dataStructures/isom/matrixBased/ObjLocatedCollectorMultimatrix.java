package dataStructures.isom.matrixBased;

import java.awt.Point;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.isom.MultiISOMRetangularCaching;
import dataStructures.isom.MultiISOMRetangularMap;
import dataStructures.isom.MultiISOMRetangularMap.MISOMLocatedInSpace;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.ObjLocatedCollectorIsom;
import geometry.ObjectLocated;

/**
 * Not advised to be used.
 * <p>
 * An {@link ObjLocatedCollectorIsom} that benefits from 2 types of caches:
 * <ul>
 * <li>{@link MatrixInSpaceObjectsManager} in case of use under multi-"misom"
 * environment (i.e.: {@link MultiISOMRetangularMap}), cached tanks to
 * {@link NodeIsomProviderCachingMISOM}.</li>
 * <li>{@link NodeIsom}, cached thanks to {@link PointConsumerRowOptimizer}
 * (it's a inner instance, not a superclass).</li>
 * </ul>
 */
public class ObjLocatedCollectorMultimatrix<Distance extends Number> implements ObjLocatedCollectorIsom<Distance> {
	private static final long serialVersionUID = 43L;

	public ObjLocatedCollectorMultimatrix(MultiISOMRetangularMap<Distance> multiMatrix,
			Predicate<ObjectLocated> targetFilter) {
		this.isomProvider = multiMatrix;
		this.olcm = new ObjLocatedCollectorMatrix<>(null, targetFilter) {
			private static final long serialVersionUID = 6390384107528L;

			@Override
			public NodeIsomProvider<Distance> getNodeIsomProvider() { return isomProvider; }
		};
	}

	protected MultiISOMRetangularMap<Distance> isomProvider;
	protected ObjLocatedCollectorMatrix<Distance> olcm; // this is the implementation for SINGLE matrix!!

	@Override
	public void accept(Point location) {
		MISOMLocatedInSpace<Distance> ml;
		ml = isomProvider.getMapLocatedContaining(location);
		if (ml == null)
			return; // no null allowed here!
		if (ml.misom != olcm.getMisom()) {
			// reset the row-cache
			olcm.setMisom(ml.misom);
		}
		// shift the point to the offset
		location = new Point(location.x - ml.x, location.y - ml.y);
		olcm.accept(location);
//		consume(isomProvider.getNodeAt(location));
	}

	// proxies

	@Override
	public Predicate<ObjectLocated> getTargetsFilter() { return olcm.targetFilter; }

	@Override
	public Set<ObjectLocated> getCollectedObjects() { return olcm.getCollectedObjects(); }

	@Override
	public NodeIsomProvider<Distance> getNodeIsomProvider() { return isomProvider; }

	@Override
	public void setNodeIsomProvider(NodeIsomProvider<Distance> nodeIsomProvider) {
		if (nodeIsomProvider instanceof MultiISOMRetangularCaching<?>)
			this.isomProvider = (MultiISOMRetangularCaching<Distance>) nodeIsomProvider;
	}

}