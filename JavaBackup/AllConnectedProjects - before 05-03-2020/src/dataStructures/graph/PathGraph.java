package dataStructures.graph;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * TODO: mettere a disposizione l'iterazione dei vari step e nacondere la
 * {@link java.util.List} sottostante.
 * 
 * @author lonevetad
 *
 * @param <E>
 */
public class PathGraph<E, Distance> implements Serializable {
	private static final long serialVersionUID = 804954560330304L;

	public PathGraph(NodeDistanceManager<Distance> distanceManager) {
		distanceTotal = null;
		path = new LinkedList<>();
		this.distanceManager = distanceManager;
	}

	private Distance distanceTotal;
	private final NodeDistanceManager<Distance> distanceManager;
	// The first step has 0 distance
	private LinkedList<PathStep<E, Distance>> path;

	//

	public Distance getDistanceTotal() {
		return distanceTotal;
	}

	public List<PathStep<E, Distance>> getPath() {
		return path;
	}

	protected void setDistanceTotal(Distance distanceTotal) {
		this.distanceTotal = distanceTotal;
	}

	public NodeDistanceManager<Distance> getDistanceManager() {
		return distanceManager;
	}

	//

	public void addStep(E e, Distance distance) {
//		if (distance >= 0) {
		this.distanceTotal = distanceManager.getAdder().apply(distanceTotal, distance);
		this.path.addFirst(new PathStep<E, Distance>(e, distance));
//		}
	}

	public void setStartStep(E e) {
		if (!Objects.equals(this.path.getFirst().elem, e))
			this.path.addFirst(new PathStep<E, Distance>(e, distanceManager.getZeroValue()));
	}

	public void forEachStep(Consumer<E> elementConsumer) {
		this.forEachStep((e, d) -> elementConsumer.accept(e));
	}

	public void forEachStep(BiConsumer<E, Distance> elementDistanceConsumer) {
		if (this.path != null && (!this.path.isEmpty()))
			this.path.forEach(ps -> elementDistanceConsumer.accept(ps.elem, ps.distFromPrevious));
	}

	@Override
	public String toString() {
		int i;
		StringBuilder sb;
		sb = new StringBuilder(1024);
		sb.append("Path long ").append(distanceTotal).append(": [\n");
		i = 0;
		for (PathStep<E, Distance> p : path) {
			sb.append(i++).append(") ").append(p.elem).append(", far: ").append(p.distFromPrevious).append('\n');
		}
		return sb.append(']').toString();
	}

	//

	public static class PathStep<T, D> {
		D distFromPrevious;
		T elem;

		PathStep(T e, D d) {
			this.elem = e;
			this.distFromPrevious = d;
		}
	}
}