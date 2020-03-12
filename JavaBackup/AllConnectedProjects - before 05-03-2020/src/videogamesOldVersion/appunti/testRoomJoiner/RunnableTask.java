package appunti.testRoomJoiner;

import java.io.Serializable;

import importedUtilities.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import importedUtilities.mainTools.LoggerMessages;

public abstract class RunnableTask implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;

	public RunnableTask() {
		lock = new Object();
		classname = this.getClass().getSimpleName();
	}

	public RunnableTask(LoggerMessagesHolder loggerHolder) {
		this();
		this.loggerHolder = loggerHolder;
	}

	Object lock;
	String classname;
	LoggerMessagesHolder loggerHolder;

	public LoggerMessages getLog() {
		return loggerHolder.getLog();
	}

	//

	public abstract void doEachCycle();

	public boolean canContinueCycle() {
		return true;
	}

	public void log(String ss) {
		loggerHolder.getLog().log(ss);
	}

	@Override
	public void run() {
		log(classname + " START");
		while (canContinueCycle()) {

			try {
				doEachCycle();
			} catch (Exception e) {
				e.printStackTrace();
				log(e.getMessage());
			}
			/*
			 * try { Thread.sleep(125); } catch (InterruptedException e) {
			 * e.printStackTrace(); log(e.getMessage()); }
			 */
		}
		log(classname + " END");
	}

	void suspendMyExecution() {
		synchronized (lock) {
			try {
				log("\n\r" + classname + " sleep");
				lock.wait();
				log("\n\r" + classname + " awakes");
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
	}

	void resumeThisRunnable() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}

}