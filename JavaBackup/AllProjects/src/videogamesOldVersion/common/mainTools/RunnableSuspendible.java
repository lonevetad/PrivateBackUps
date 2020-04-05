package common.mainTools;

import common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;

public abstract class RunnableSuspendible implements Runnable {

	public RunnableSuspendible(LoggerMessagesHolder lmh) {
		this.lmh = lmh;
		shouldSleep = false;
		sleepLock = new Object();
	}

	protected boolean shouldSleep;
	protected LoggerMessagesHolder lmh;
	protected Object sleepLock;

	public abstract void doOnEachCycle();

	public abstract boolean canCycle();

	@Override
	public void run() {
		// log(this.getClass().getSimpleName() + " Runnable started");
		try {
			while (canCycle()) {
				checkAndSleep();
				doOnEachCycle();
			}
		} catch (Exception e) {
			if (!(e instanceof InterruptedException)) {
				e.printStackTrace();
				log(e.getMessage());
			}
		}
		// log(this.getClass().getSimpleName() + " Runnable closing");
	}

	public void suspendRunnable() {
		shouldSleep = true;
	}

	public void resumeRunnable() {
		shouldSleep = false;
		// log(this.getClass().getSimpleName() + " Runnable resuming");
		synchronized (sleepLock) {
			sleepLock.notifyAll();
			// log(this.getClass().getSimpleName() + " Runnable resumed");
		}
	}

	//

	protected void log(String t) {
		this.lmh.getLog().logAndPrint(t);
	}

	protected void log(Exception e) {
		this.lmh.getLog().logException(e);
	}

	protected void checkAndSleep() {
		if (shouldSleep) {
			shouldSleep = false;
			makeMeSleep();
		}
	}

	protected final void makeMeSleep() {
		try {
			synchronized (sleepLock) {
				// log(this.getClass().getSimpleName() + " Runnable sleeping");
				sleepLock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			log(e.toString());
		}
	}

}
