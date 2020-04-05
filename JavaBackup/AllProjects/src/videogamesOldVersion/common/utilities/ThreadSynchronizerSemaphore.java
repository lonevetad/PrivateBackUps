package common.utilities;

import java.io.Serializable;

public class ThreadSynchronizerSemaphore implements Serializable {
	private static final long serialVersionUID = -9878907879002221L;

	public ThreadSynchronizerSemaphore() {
	}

	public ThreadSynchronizerSemaphore(int totalAmountThreadsSynchronizing) {
		if (totalAmountThreadsSynchronizing < 2)
			throw new IllegalArgumentException("Cannot create a semaphore to synchronize less than 2 threads !");
		this.totalAmountThreadsSynchronizing = totalAmountThreadsSynchronizing;
	}

	int amountThreadsWaiting, totalAmountThreadsSynchronizing;

	public int getAmountThreadsWaiting() {
		return amountThreadsWaiting;
	}

	public int getTotalAmountThreadsSynchronizing() {
		return totalAmountThreadsSynchronizing;
	}

	@SuppressWarnings("unused")
	private void resetAmountThreadsWaiting() {
		amountThreadsWaiting = 0;
	}

	@SuppressWarnings("unused")
	private void increaseAmountThreadsWaiting() {
		amountThreadsWaiting++;
	}

	public synchronized void waitForOtherThreads() {
		if (amountThreadsWaiting >= totalAmountThreadsSynchronizing) {
			// amountThreadsWaiting();
			amountThreadsWaiting = 0;
			notifyAll();
		} else {
			// increaseAmountThreadsWaiting(); //optimize
			amountThreadsWaiting++;
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				amountThreadsWaiting--;
			}
		}
	}
}