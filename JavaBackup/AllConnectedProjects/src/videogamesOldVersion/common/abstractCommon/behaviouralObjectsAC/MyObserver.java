package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

public interface MyObserver extends Serializable {
	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an {@link MyObservable} object's
	 * <code>notifyObservers</code> method to have all the object's observers
	 * notified of the change.
	 *
	 * @param o
	 *            the observable object.
	 * @param arg
	 *            an argument passed to the <code>notifyObservers</code> method.
	 */
	void update(MyObservable o, Object arg);

}
