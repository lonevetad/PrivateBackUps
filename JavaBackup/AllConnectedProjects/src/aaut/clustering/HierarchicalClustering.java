package aaut.clustering;

import java.util.Map;

public class HierarchicalClustering {

	public static interface DistanceMethod<T> {
		public double distance(T t1, T t2);
	}

	public HierarchicalClustering() {
	}

	public static <T> HierarchicalTree<T> constructHierarchicalTree(java.util.List<T> dataset,
			DistanceMethod<T> distance) {
		HierarchicalTree<T> t;
		Map<Long, HierarchicalTree<T>.HTNode> nodes;

		t = new HierarchicalTree<T>();

		return null;
	}

	//

	public static class HierarchicalTree<T> {
		long progressiveID = 0;
		HTNode root;

		public HierarchicalTree() {
			this.progressiveID = 0;
			this.root = null;
		}

		public HTNode newNode() {
			return new HTNode(this.progressiveID++);
		}

		//

		public class HTNode {
			int level;
			final Long ID;
			HTNode father, left, right;

			protected HTNode(long id) {
				this.ID = id;
			}

			public int getLevel() {
				return level;
			}

			public Long getID() {
				return ID;
			}
		}
	}
}
