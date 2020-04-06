package dataStructures.isom;

import java.util.Set;
import java.util.function.Predicate;

import geometry.ObjectLocated;
import geometry.pointTools.PointConsumer;

public interface ObjLocatedCollector extends PointConsumer {

	public Predicate<ObjectLocated> getTargetsFilter();

	public Set<ObjectLocated> getCollectedObjects();
}