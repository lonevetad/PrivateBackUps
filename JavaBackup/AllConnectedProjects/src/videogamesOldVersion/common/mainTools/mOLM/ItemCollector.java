package common.mainTools.mOLM;

import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager.CollectedObject;
import common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;

/*
 * TODO Collector NON MI COMPILA IL "add" SE METTO "? extends Parent_" NELLA
 * PARAMETRIZZAZIONE DEL VALORE DELL'ALBERO
 */
public class ItemCollector extends CollectedObject implements DoSomethingWithNode {
	private static final long serialVersionUID = 78428916503L;

	protected static interface CollectorModeChecker {
		boolean check(int value, int thresHold);
	}

	@Deprecated
	public static enum CollectorMode {
		All//
		, AtLeast((v, t) -> v >= t)//
		, AtMost((v, t) -> v <= t)//
		, PreciseQuantity((v, t) -> v == t)//
		;

		private final CollectorModeChecker checker;

		CollectorMode() {
			this(null);
		}

		CollectorMode(CollectorModeChecker c) {
			checker = c;
		}

		public boolean checkCondition(int value, int thresHold) {
			return checker == null || checker.check(value, thresHold);
		}
	}

	public static final int COLLECT_ALL = -1, COLLECT_FIRST_FOUND_TO_CHECK_EMPITY = 1;
	boolean mustCollectAll, mustCollectNulls;
	int threshold, nullCollected;
	// CollectorMode collectorMode;

	public ItemCollector() {
		this(COLLECT_ALL);
	}

	public ItemCollector(int threshold) {
		this(threshold, true);
	}

	public ItemCollector(int threshold, boolean mustCollectNulls) {
		super();
		this.mustCollectAll = (this.threshold = threshold) < 0;
		this.mustCollectNulls = mustCollectNulls;
		// collectorMode = cm;
		nullCollected = 0;
	}

	public boolean isMustCollectAll() {
		return mustCollectAll;
	}

	public boolean isMustCollectNulls() {
		return mustCollectNulls;
	}

	public int getThreshold() {
		return threshold;
	}

	public int getNullCollected() {
		return nullCollected;
	}

	//

	public ItemCollector setMustCollectAll(boolean mustCollectAll) {
		this.mustCollectAll = mustCollectAll;
		return this;
	}

	public ItemCollector setMustCollectNulls(boolean mustCollectNulls) {
		this.mustCollectNulls = mustCollectNulls;
		return this;
	}

	//

	public void clear() {
		// minimumSize = 0;
		nullCollected = 0;
		this.getCollectedObjects().clear();
	}

	@Override
	public int howManyCollected() {
		return nullCollected + super.howManyCollected();
	}

	@Override
	public Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int xx, int yy) {
		ObjectWithID o;
		o = null;
		if (canContinueCycling()) {
			// o = a[xx].item;
			// if (o != null) {
			// o = manageAdding(o);
			// }
			o = manageAdding(item);
		}
		return o;
	}

	@Override
	public boolean canContinueCycling() {
		return mustCollectAll || (howManyCollected() < threshold);
	}

	public ObjectWithID manageAdding(ObjectWithID o) {
		boolean mustAdd;

		mustAdd = canBeCollected(o);
		if (mustAdd) {
			if (o == null) {
				if (mustCollectNulls) nullCollected = 1;
				return null;
			} else {
				collect(o);
			}
		}
		return o;
	}

	/** Overriding-designed */
	protected boolean canBeCollected(ObjectWithID o) {
		return true;
	}

}
