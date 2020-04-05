package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

public interface DoOnShutDown extends Serializable, Runnable {

	@Override
	public default void run() {
		doOnShutdown();
	}

	public void doOnShutdown();
}