package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

public interface ThreadManagerTiny extends Serializable {

	/** Return true if some new Runnables has been allocated. */
	public boolean allocThreads();

	public void destroyThreads();

	public void startThreads();

	public void pauseThreads();

	public void resumeThreads();
}