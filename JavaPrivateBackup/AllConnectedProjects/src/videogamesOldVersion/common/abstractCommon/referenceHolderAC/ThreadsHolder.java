package common.abstractCommon.referenceHolderAC;

import java.util.Map;
import java.util.function.BiConsumer;

import common.abstractCommon.behaviouralObjectsAC.ThreadManagerTiny;
import common.mainTools.Comparators;
import common.mainTools.dataStruct.MapTreeAVL;

public interface ThreadsHolder extends ThreadManagerTiny {

	public static final BiConsumer<Long, Thread> THREAD_INTERRUPTER = (l, t) -> {
		if (t != null) {
			try {
				t.interrupt();
			} catch (Exception ex) {
				ex.getMessage();
			}
		}
	};

	//

	public Map<Long, Thread> getPoolThread();

	public ThreadsHolder setPoolThread(Map<Long, Thread> poolThread);

	public default Map<Long, Thread> newPoolThread() {
		return new MapTreeAVL<Long, Thread>(Comparators.LONG_COMPARATOR);
	}

	public default boolean addThreadToPool(Thread newThread) {
		return replaceThreadOnPool(null, newThread);
	}

	public default boolean removeThreadFromPool(Thread oldThread) {
		return replaceThreadOnPool(oldThread, null);
	}

	public default boolean replaceThreadOnPool(Thread oldThread, Thread newThread) {
		java.util.Map<Long, Thread> r;
		r = getPoolThread();
		if (r != null) {
			if (oldThread != null && (!r.isEmpty()))
				r.remove(oldThread.getId());
			if (newThread != null)
				r.put(newThread.getId(), newThread);
			return true;
		}
		return false;
	}

	public default ThreadsHolder resetPoolThread() {
		clearThreadPool();
		setPoolThread(newPoolThread());
		return this;
	}

	public default boolean interruptAllThreads() {
		Map<Long, Thread> t;
		t = getPoolThread();
		if (t != null) {
			t.forEach(THREAD_INTERRUPTER);
			return true;
		}
		return false;
	}

	public default boolean clearThreadPool() {
		Map<Long, Thread> t;
		t = getPoolThread();
		if (t != null) {
			t.clear();
			return true;
		}
		return false;
	}
}