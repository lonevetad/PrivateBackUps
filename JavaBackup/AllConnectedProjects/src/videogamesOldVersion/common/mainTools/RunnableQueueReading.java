package common.mainTools;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;

public abstract class RunnableQueueReading<T> extends RunnableSuspendible {

	public RunnableQueueReading(LoggerMessagesHolder lmh) {
		super(lmh);
		queue = new LinkedList<>();
		// super.sleepLock = queue;// don't waste too much memory
	}

	protected Queue<T> queue;

	public abstract void manage(T object);

	@Override
	public void doOnEachCycle() {
		T t;
		try {
			/*
			 * if (queue.isEmpty()) log(this.getClass().getSimpleName() + "'s QUEUE empty"); else
			 */
			while (!queue.isEmpty()) {
				checkAndSleep();
				t = queue.poll();
				// log(this.getClass().getSimpleName() + " polled a object: " +
				// String.valueOf(t));
				if (t != null) manage(t);
			}
			doOnEmptyingQueue();
		} catch (Exception e) {
			if (!(e instanceof InterruptedException)) {
				// e.printStackTrace();
				log(e);
			}
		}
	}

	public abstract void doOnEmptyingQueue();

	public void addObjectToQueue(T t) {
		if (t != null) {
			queue.add(t);
			// log(this.getClass().getSimpleName() + " adding a object: " +
			// String.valueOf(t));
			resumeRunnable();
		}
	}

	public void addObjectsToQueue(Collection<T> coll) {
		boolean resumable;
		resumable = false;
		if (coll != null && (!coll.isEmpty())) {
			for (T t : coll) {
				if (t != null) {
					resumable = true;
					queue.add(t);
				}
			}
			if (resumable) resumeRunnable();
		}
	}

	//

	@Override
	protected void checkAndSleep() {
		if (shouldSleep || queue.isEmpty()) {
			shouldSleep = false;
			// log(this.getClass().getSimpleName() + " sleeping");
			super.makeMeSleep();
		}
	}
}
