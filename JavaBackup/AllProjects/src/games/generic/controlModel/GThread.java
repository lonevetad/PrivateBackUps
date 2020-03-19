package games.generic.controlModel;

public class GThread extends Thread {

	protected GTRunnable tgr;

	public GThread(GTRunnable target) {
		super(target);
		this.tgr = target;
	}

	// makes me stops
	public void stopAndDie() {
		this.tgr.stopAndDie();
	}

	public static interface GTRunnable extends Runnable {

		// makes me stops
		public void stopAndDie();
	}
}