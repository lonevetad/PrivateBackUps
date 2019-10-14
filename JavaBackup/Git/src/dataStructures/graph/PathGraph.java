package dataStructures.graph;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PathGraph<E> implements Serializable {
	private static final long serialVersionUID = 804954560330304L;

	public PathGraph() {
		distanceTotal = 0;
		path = new LinkedList<>();
	}

	private double distanceTotal;
	// The first step has 0 distance
	private LinkedList<PathStep<E>> path;

	//

	public double getDistanceTotal() {
		return distanceTotal;
	}

	public List<PathStep<E>> getPath() {
		return path;
	}

	protected void setDistanceTotal(double distanceTotal) {
		this.distanceTotal = distanceTotal;
	}

	//

	public void addStep(E e, double distance) {
		if (distance >= 0) {
			this.distanceTotal += distance;
			this.path.addFirst(new PathStep<E>(e, distance));
		}
	}

	public void setStartStep(E e) {
		if (!Objects.equals(this.path.getFirst().elem, e))
			this.path.addFirst(new PathStep<E>(e, 0));
	}

	@Override
	public String toString() {
		int i;
		StringBuilder sb;
		sb = new StringBuilder(1024);
		sb.append("Path long ").append(distanceTotal).append(": [\n");
		i = 0;
		for (PathStep<E> p : path) {
			sb.append(i++).append(") ").append(p.elem).append(", far: ").append(p.distFromPrevious).append('\n');
		}
		return sb.append(']').toString();
	}

	//

	public static class PathStep<T> {
		double distFromPrevious;
		T elem;

		PathStep(T e, double d) {
			this.elem = e;
			this.distFromPrevious = d;
		}
	}
}