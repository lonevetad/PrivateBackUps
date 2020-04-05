package common.mainTools.mOLM.abstractClassesMOLM;

import java.io.Serializable;
import java.util.List;

import common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager;
import common.mainTools.mOLM.MatrixObjectLocationManager;

/**
 * Used to notify some observer of {@link MatrixObjectLocationManager}'s
 * changes.
 */
public interface ObservableMolm extends Serializable {

	public static enum MolmEvent {
		Undefined, ObjectAdded, ObjectRemoved, MapCleaned, MapResized, MolmsInstancesChanged, CustomOperation;
	}

	public List<ObserverMolm> getMolmObservers();

	public ObservableMolm setMolmObservers(List<ObserverMolm> observers);

	public default boolean addMolmObserver(ObserverMolm o) {
		boolean added;
		List<ObserverMolm> lo;
		added = false;
		if (o != null) {
			lo = getMolmObservers();
			if (lo != null && (!lo.contains(o))) {
				lo.add(o);
				added = true;
			}
		}
		return added;
	}

	public default boolean addMolmObservers(Iterable<ObserverMolm> os) {
		boolean added;
		List<ObserverMolm> lo;
		added = false;
		if (os != null) {
			lo = getMolmObservers();
			if (lo != null) {
				for (ObserverMolm o : os) {
					if (o != null && (!lo.contains(o))) {
						lo.add(o);
						added = true;
					}
				}
			}
		}
		return added;
	}

	public default void removeMolmObservers() {
		List<ObserverMolm> lo;
		lo = getMolmObservers();
		if (lo != null) lo.clear();
	}

	public default void removeMolmObserver(ObserverMolm o) {
		List<ObserverMolm> lo;
		lo = getMolmObservers();
		if (o != null && lo != null) lo.remove(o);
	}

	public default void notifyMolmObservers() {
		notifyMolmObservers(MolmEvent.Undefined, null, null);
	}

	public default void notifyMolmObservers(MolmEvent me, Object o) {
		notifyMolmObservers(me, o, null);
	}

	public default void notifyMolmObservers(MolmEvent me, AbstractMOLMManager molmManager) {
		notifyMolmObservers(me, null, molmManager);
	}

	/**
	 * The integer parameter <code>indexMolm</code> is the index of the MOLM
	 * modified, if any, a negative number otherwise
	 * <p>
	 * Removed 08/02/2018
	 */
	/***/
	public default void notifyMolmObservers(MolmEvent me, Object extraArguments, AbstractMOLMManager molmManager) {
		List<ObserverMolm> l;
		l = getMolmObservers();
		if (l != null && (!l.isEmpty())) {
			for (ObserverMolm obs : l)
				obs.update(me, extraArguments, molmManager);
		}
	}

}