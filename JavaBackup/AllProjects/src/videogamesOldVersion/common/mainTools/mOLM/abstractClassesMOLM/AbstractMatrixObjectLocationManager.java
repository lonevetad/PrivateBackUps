package videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM;

import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import dataStructures.MyLinkedList;
import tools.Comparators;
import tools.ObjectWithID;
import videogamesOldVersion.common.FullReloadEnvironment;
import videogamesOldVersion.common.abstractCommon.Memento;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.MementoPatternImplementor;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.ObjectSerializable;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.ShapeSpecificationHolder;
import videogamesOldVersion.common.abstractCommon.shapedObject.AbstractObjectRectangleBoxed;
import videogamesOldVersion.common.mainTools.mOLM.ItemCollector;
import videogamesOldVersion.common.mainTools.mOLM.NodeMatrix;
import videogamesOldVersion.common.mainTools.mOLM.PathFinderOLD;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID_OLD_MOLM.MementoOWID;

/**
 * NOTE BEFORE ALL :<br>
 * This class represent and manage a matrix divided in "cells", A.K.A.
 * <b>"micropixel"</b>.<br>
 * Each dimension, size, coordinates, location, ecc. MUST be considered and
 * expressed using this "dimensional-measure-unit".
 * <p>
 * This class allows to manage {@link ObjectWithID} instances. More precisely,
 * this class store those instances in the matrix.<br>
 * Instances of {@link ObjectWithID} and derived classes (like
 * {@link ObjectShaped}) may have a particular shape, not only single "point",
 * like the ones described in the class {@link ShapeSpecification}. For example,
 * a rectangle or a circle <br>
 * This class lets to draw shapes, adding or removing {@link ObjectWithID} 's
 * instances given as parameter. Not all of possible shapes are implemented:
 * this depends on the {@link AbstractShapeRunners} instance required by this
 * class.
 * <p>
 * This class also allows to find the shortest path from one point to
 * another.<br>
 * If the required path would be "walked" by a {@link ObjectWithID} instance who
 * has a particular shape, then that path could be found considering also a
 * {@link ShapeSpecification}. <br>
 * Those features are offered by a {@link AbstractPathFinder} instance required
 * by this class.
 * <p>
 * GOF Patterns: This class uses some patterns:
 * <ul>
 * <li>Strategy: {@link PathFinderOLD} and {@link AbstractShapeRunners}</li>
 * <li>Singleton : {@link AbstractPathFinder}'s and
 * {@link AbstractShapeRunners}'s instances</li>
 * <li>Decorator: a similar</li>
 * <li>Abstract Factory: {@link ShapeSpecification} produce instances and
 * subclasses of it through static methods</li>
 * </ul>
 * <p>
 * This class shows errors through a {@link LoggerMessages} instance. The
 * default ones uses the standard output.
 */
// <li></li>
public interface AbstractMatrixObjectLocationManager extends ObjectSerializable, MementoPatternImplementor {

	// TODO GETTER

	public int getWidthMicropixel();

	public int getHeightMicropixel();

	public LoggerMessages getLog();

	public AbstractShapeRunners/* _WithCoordinates */ getShapeRunners();

	public AbstractPathFinder getPathFinder();

	// TODO SETTER

	public AbstractMatrixObjectLocationManager setLog(LoggerMessages log);

	public AbstractMatrixObjectLocationManager setShapeRunners(AbstractShapeRunners/* _WithCoordinates */ shapeRunners);

	public AbstractMatrixObjectLocationManager setPathFinder(AbstractPathFinder pathFinder);

	// TODO OTHERS

	// base

	public default NodeMatrix getNewNodeMatrix() {
		return getNewNodeMatrix(0, 0);
	}

	public NodeMatrix getNewNodeMatrix(int xx, int yy);

	public NodeMatrix[][] getMatrix();

	public NodeMatrix getNodeMatrix(int xx, int yy);

	public void forEach(DoSomethingWithNode dswn);

	public default void reallocMatrixMap() {
		reallocMatrixMap(getWidthMicropixel(), getHeightMicropixel());
	}

	public void reallocMatrixMap(int width, int height) throws IllegalArgumentException;

	public boolean resizeMatrix(int width, int height) throws IllegalArgumentException;

	public void clearMatrix();

	// operations

	public List<Point> extractShortestPath(int xStart, int yStart, int xDest, int yDest);

	public List<Point> extractShortestPath(int xStart, int yStart, int xDest, int yDest, ShapeSpecification ss);

	public CollectedObject collectOnShape(ShapeSpecification ss);

	public CollectedObject collectOnShapeReferringTo(ShapeSpecification ss, ObjectWithID whoMustNotBeCollected);

	/**
	 * Returns {@code -1} in case of error, 0 means no-one, a value greater than
	 * zero the number of different objects counted.
	 */
	public default int howManyOnShape(ShapeSpecification ss, ObjectWithID whoMustNotBeCollected) {
		CollectedObject collectedStuffs;
		if (ss == null || whoMustNotBeCollected == null)
			return -1;
		collectedStuffs = collectOnShapeReferringTo(ss, whoMustNotBeCollected);
		if (collectedStuffs == null) {
			getLog().log("ERROR: on isThereSomeoneOnShape, collectedStuffs is null");
			return -1;
		}
		return collectedStuffs.howManyCollected();
		// collectedObjects.size() > 0);
	}

	public default boolean isThereSomeoneOnShape(ShapeSpecification ss, ObjectWithID whoMustNotBeCollected) {
		return howManyOnShape(ss, whoMustNotBeCollected) > 0;
	}

	public default boolean isThereSomeoneOnShape(ShapeSpecification ss) {
		return isThereSomeoneOnShape(ss, null);
	}

	// matrix modifiers

	public default String addOnShape(ShapeSpecification ss, ObjectWithID toBeAdded) {
		return addOnShape(ss, toBeAdded, false);// false to optimize
	}

	public default String addOnShape(ShapeSpecification ss, ObjectWithID toBeAdded,
			boolean removePrevIfShapeSpecHolder) {
		return replaceOnShape(ss, toBeAdded, null, removePrevIfShapeSpecHolder);
	}

	public default String removeOnShape(ShapeSpecification ss, ObjectWithID toBeRemoved) {
		// return removeOnShape(ss, toBeRemoved, false);
		return replaceOnShape(ss, null, toBeRemoved);
	}

	/*
	 */
	public default String removeOnShape(ShapeSpecification ss, ObjectWithID toBeRemoved,
			boolean removePrevIfShapeSpecHolder) {
		return replaceOnShape(ss, null, toBeRemoved, removePrevIfShapeSpecHolder);
	}

	public default String replaceOnShape(ShapeSpecification ss, ObjectWithID toBeAdded, ObjectWithID toBeRemoved) {
		return replaceOnShape(ss, toBeAdded, toBeRemoved, false);
	}

	public String replaceOnShape(ShapeSpecification ss, ObjectWithID toBeAdded, ObjectWithID toBeRemoved,
			boolean removePrevIfShapeSpecHolder);

	public String runOnShape(ShapeSpecification ss, DoSomethingWithNode dswc);

	public default Polygon boundingPolygon(ShapeSpecification.SS_Rectangular rect) {
		return rect == null ? null : boundingPolygon((AbstractObjectRectangleBoxed) rect);
	}

	/**
	 * Note: ivoke {@link #boundingPolygon(int, int, int, int)}, considering the
	 * Left-Bottom corner, not the center, as the (x,y) point.
	 */
	public default Polygon boundingPolygon(AbstractObjectRectangleBoxed orb) {
		if (orb != null && orb.getWidth() > 0 && orb.getHeight() > 0) {
			return boundingPolygon(orb.getXLeftBottom(), orb.getYLeftBottom(), orb.getWidth(), orb.getHeight());
		}
		return null;
	}

	public default Polygon boundingPolygon(int width, int height) {
		return boundingPolygon(0, 0, width, height);
	}

	public Polygon boundingPolygon(int x, int y, int width, int height);

	public void resetDefaultInstances();

	//

	public static class CollectedObject implements Serializable {
		private static final long serialVersionUID = 87408632014787887L;
		SortedMap<Integer, ObjectWithID> collectedObjects;

		public CollectedObject() {
			collectedObjects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
					Comparators.INTEGER_COMPARATOR);
		}

		public SortedMap<Integer, ObjectWithID> getCollectedObjects() {
			return collectedObjects;
		}

		public boolean hasCollected() {
			return howManyCollected() > 0;
		}

		public int howManyCollected() {
			return collectedObjects.size();
		}

		public boolean collect(ObjectWithID o) {
			if (o != null && (!collectedObjects.containsKey(o.getID()))) {
				collectedObjects.put(o.getID(), o);
				return true;
			}
			return false;
		}

		public boolean remove(ObjectWithID o) {
			return (o != null && (collectedObjects.remove(o.getID()) != null));
		}

		public boolean forEach(BiConsumer<Integer, ObjectWithID> doSome) {
			if (hasCollected()) {
				collectedObjects.forEach(doSome);
				return true;
			}
			return false;
		}
	}

	public static ObjectWithID addObjectWithIDFromItsMemento(AbstractMatrixObjectLocationManager molm,
			MementoOWID mowid) {
		ObjectWithID owid;
		Object o;
		ShapeSpecificationHolder ssh;
		owid = null;
		try {
			o = mowid.reinstanceFromMe();
			if (o != null && o instanceof ObjectWithID) {
				owid = (ObjectWithID) o;
				if (owid instanceof ShapeSpecificationHolder) {
					ssh = (ShapeSpecificationHolder) owid;
					// ok, can be added
					molm.addOnShape(ssh.getShapeSpecification(), owid);
				} else {
					molm.getLog().log("Don't know how to add the given " + owid.getClass().getSimpleName()
							+ " to this molm. Discarded. Its toString():");
					molm.getLog().log(owid.toString());
					owid = null;
				}
			}
		} catch (Exception e) {
			molm.getLog().log("AH AH EXCEPTION IN AMOLM.addObjectWithIDFromItsMemento : \n\t");
			molm.getLog().logException(e);
		}
		return owid;
	}

	//

	@Override
	public default String doAfterDeserialization(Object arguments) {
		// forEach(new DoAfterDeserializationToAllOWIDStored(arguments));
		return null;
	}

	//

	// TODO MEMENTO

	public default void fillMementoMOLM(AbstractMementoMOLM m) {
		ItemCollector col;
		List<ObjectWithID.MementoOWID> mr;

		col = new ItemCollector();
		forEach(col);
		if (col.hasCollected()) {
			m.mementosRecorded = mr = new MyLinkedList<>();
			col.forEach((i, o) -> {
				MementoOWID mem;
				if (o != null) {
					mem = o.createMemento();
					if (mem != null)
						mr.add(mem);
				}
			});
		}
	}

	public default void processOWIDReloadedFromMemento(ObjectWithID owid) {
		/**
		 * GameObjectInMap otiled, originalOTiled;//<br>
		 * if (owid != null && fre != null && owid instanceof GameObjectInMap &&
		 * fre.canBeUsedToReload()) { otiled = (GameObjectInMap) owid; //<br>
		 * originalOTiled = null; //<br>
		 * // fre.getListTileMapEnum().getTileMapInstance(fre.getMain(), //<br>
		 * // otiled.getImageFilename()); //<br>
		 * // share original images //<br>
		 * otiled.shareImagesFrom(originalOTiled); //<br>
		 * }
		 */
	}

	@Override
	public default boolean reloadState(Memento m) {
		return reloadState(m, null);
	}

	/**
	 * NOTE: all {@link ObjectWithID}'s {@link ObjectWithID.MementoOWID} are, in
	 * fact, a deep copy of the originator's instance. So, don't save its Mementos,
	 * but save a matrix of <code>ObjectWithID</code>, filled with a deep-copy of
	 * MOLMS (or serialize the matrix of {@link NodeMatrix}).
	 */
	public default boolean reloadState(Memento m, FullReloadEnvironment fre) {
		AbstractMementoMOLM mmolm;
		List<ObjectWithID.MementoOWID> mr;
		if (m != null && m instanceof AbstractMementoMOLM) {
			getLog().log("Reloading memento from AbstractMatrixObjectLocationManager");
			mmolm = (AbstractMementoMOLM) m;
			this.reallocMatrixMap(mmolm.widthMicropixel, mmolm.heightMicropixel);
			this.setShapeRunners(AbstractShapeRunners.getOrDefault(null));
			this.setPathFinder(AbstractPathFinder.getOrDefault(null));

			mr = mmolm.mementosRecorded;
			if (mr != null && mr.size() > 0) {
				for (MementoOWID mowid : mr) {
					try {
						processOWIDReloadedFromMemento(addObjectWithIDFromItsMemento(this, mowid));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public AbstractMementoMOLM createMemento();

	// class

	public static abstract class AbstractMementoMOLM extends Memento {
		private static final long serialVersionUID = 81080591095629811L;
		int widthMicropixel, heightMicropixel;
		// AbstractShapeRunners shapeRunners;
		// AbstractPathFinder pathFinder;
		List<ObjectWithID.MementoOWID> mementosRecorded;

		public AbstractMementoMOLM() {
			super();
		}

		public AbstractMementoMOLM(AbstractMatrixObjectLocationManager molm) {
			super();
			this.widthMicropixel = molm.getWidthMicropixel();
			this.heightMicropixel = molm.getHeightMicropixel();
			/*
			 * shapeRunners = (molm.getShapeRunners() !=
			 * AbstractShapeRunners.getOrDefault(null)) ? molm.getShapeRunners() : null;
			 * pathFinder = (molm.getPathFinder() != AbstractPathFinder.getOrDefault(null))
			 * ? molm.getPathFinder() : null;
			 */
		}

		public List<ObjectWithID.MementoOWID> getMementosRecorded() {
			return mementosRecorded;
		}

		/**
		 * WARNING: it did not call {@link #reallocMatrixMap(int, int)}.<br>
		 * The caller must call it !!
		 */
		@Override
		public AbstractMatrixObjectLocationManager reinstanceFromMe() {
			return reinstanceFromMe(null);
		}

		/**
		 * WARNING: it did not call {@link #reallocMatrixMap(int, int)}.<br>
		 * The caller must call it !!
		 */
		public abstract AbstractMatrixObjectLocationManager reinstanceFromMe(FullReloadEnvironment fre);
	}

	//

	//

	/** Useless */
	@Deprecated
	public static class DoAfterDeserializationToAllOWIDStored implements DoSomethingWithNode {
		private static final long serialVersionUID = 651510410984946404L;
		SortedMap<Integer, ObjectWithID> visitedOwid;
		// AbstractMatrixObjectLocationManager molm;
		Object argument;

		public DoAfterDeserializationToAllOWIDStored(Object argument) {
			super();
			this.argument = argument;
			visitedOwid = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
					Comparators.INTEGER_COMPARATOR);
		}

		@Override
		public Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int x, int y) {
			if (item != null && (!(visitedOwid.containsKey(item.getID())))) {
				visitedOwid.put(item.getID(), item);
				item.doAfterDeserialization(argument);
			}
			return null;
		}
	}
}