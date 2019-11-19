package geometry;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface ProviderObjectsInSpace extends Iterator<ObjectLocated> {

	/**
	 * Representation of the space managed by this instance.
	 */
	public AbstractShape2D getBoundingShape();

	/**
	 * Add the given {@link ObjectLocated} into this space (space-manager, to be
	 * precise). <br>
	 * Note: Should check if the given {@link ObjectLocated} lies inside the
	 * {@link AbstractShape2D} (provided by {@link #getBoundingShape()}) through its
	 * method {@link AbstractShape2D#contains(Point2D)}.
	 */
	public boolean add(ObjectLocated o);

	/** {@link} */
	public boolean add(ObjectShaped o);

	//

	public boolean remove(ObjectLocated o);

	public boolean remove(ObjectShaped o);

	public boolean remove(AbstractShape2D areaToClear);

	public boolean remove(AbstractShape2D areaToClear, Predicate<ObjectLocated> objectFilter);

	//

	public List<ObjectLocated> fetch(double x, double y, Predicate<ObjectLocated> objectFilter);

	public default List<ObjectLocated> fetch(double x, double y) {
		return fetch(x, y, null);
	}

	public default List<ObjectLocated> fetch(Point2D whereToLookFor, Predicate<ObjectLocated> objectFilter) {
		return this.fetch(whereToLookFor.getX(), whereToLookFor.getY(), objectFilter);
	}

	public default List<ObjectLocated> fetch(Point2D whereToLookFor) {
		return this.fetch(whereToLookFor.getX(), whereToLookFor.getY());
	}

	public List<ObjectLocated> fetch(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter);

	public default List<ObjectLocated> fetch(AbstractShape2D areaToLookInto) {
		return this.fetch(areaToLookInto, null);
	}

	//

	public void forEach(ObjectsInSpaceConsumer consumer);

	//

	public interface ObjectsInSpaceConsumer extends BiConsumer<Point2D, ObjectLocated> {

		/**
		 * Filter the whole space to the given one. If <code>null</code>, then the whole
		 * space is used
		 */
		public AbstractShape2D getAreaToLookInto();

		/**
		 * Filter the objects provided. If <code>null</code>, then every objects are
		 * accepted.
		 */
		public Predicate<ObjectLocated> getObjectFilter();

		public ObjectsInSpaceConsumer setAreaToLookInto(AbstractShape2D areaToLookInto);

		public ObjectsInSpaceConsumer setObjectFilter(Predicate<ObjectLocated> objectFilter);

		@Override
		public default void accept(Point2D location, ObjectLocated objectInSpace) {
			AbstractShape2D areaToLookInto;
			Predicate<ObjectLocated> objectFilter;
			areaToLookInto = getAreaToLookInto();
			objectFilter = getObjectFilter();
			if (areaToLookInto != null && (!areaToLookInto.contains(objectInSpace.getLocation())))
				return;
			if (objectFilter != null && (!objectFilter.test(objectInSpace)))
				return;
			acceptImpl(location, objectInSpace);
		}

		public void acceptImpl(Point2D t, ObjectLocated u);
	}

	//

	// TODO CLASSES

	public static abstract class ObjectsInSpaceConsumerImpl implements ObjectsInSpaceConsumer {
		protected AbstractShape2D areaToLookInto;
		protected Predicate<ObjectLocated> objectFilter;

		public ObjectsInSpaceConsumerImpl() {
			this(null, null);
		}

		public ObjectsInSpaceConsumerImpl(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter) {
			super();
			this.areaToLookInto = areaToLookInto;
			this.objectFilter = objectFilter;
		}

		//

		@Override
		public AbstractShape2D getAreaToLookInto() {
			return areaToLookInto;
		}

		@Override
		public Predicate<ObjectLocated> getObjectFilter() {
			return objectFilter;
		}

		@Override
		public ObjectsInSpaceConsumer setAreaToLookInto(AbstractShape2D areaToLookInto) {
			this.areaToLookInto = areaToLookInto;
			return this;
		}

		@Override
		public ObjectsInSpaceConsumer setObjectFilter(Predicate<ObjectLocated> objectFilter) {
			this.objectFilter = objectFilter;
			return this;
		}

		@Override
		public final void accept(Point2D location, ObjectLocated objectInSpace) {
			if (areaToLookInto != null && (!areaToLookInto.contains(objectInSpace.getLocation())))
				return;
			if (objectFilter != null && (!objectFilter.test(objectInSpace)))
				return;
			acceptImpl(location, objectInSpace);
		}
	}
}