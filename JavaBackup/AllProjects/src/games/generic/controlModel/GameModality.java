package games.generic.controlModel;

/**
 * One of the Core classes.<br>
 * Represents the real "game", how it works, its type, its modality. Implements
 * every dynamics, rules, win conditions, interactions, etc. Obviously, those
 * concepts could (and should) be defined in separated classes. <br>
 * This class (and its related components) should manage the event-firing
 * systems (i.e. {@link GameEvent} and {@link GameEventManager}), like "object
 * moved", "spawn creatures / projectiles", "stuffs dropped", "damage dealt",
 * "someone healed", etc through a set of specific methods (that could be
 * wrapped in a specific class).<br>
 * The following examples will provides an idea of what a "modality" is:
 * <ul>
 * <li>PvsIA, 1v1, Multi_VS_Multi, etc</li>
 * <li>chess like, usual RPG through maps, card game, real time strategy (RTS),
 * vehicle based, visual story, etc</li>
 * <li>Examples for RPG and/or RTS: dungeons, open world,
 * YouVsWawesOfEnemies</li>
 * </ul>
 */
public abstract class GameModality {
	static final long MIN_DELTA = 10L;

	//

	boolean isRunning;
	protected GameController controller;
	protected GameModel model;
	protected String modalityName;
	/** Used to suspend threads */
	protected final Object pauseThreadsLock = new Object();

	public GameModality(GameController controller, String modalityName) {
		this.controller = controller;
		this.modalityName = modalityName;
		this.model = newGameModel();
		onCreate();
	}

	// getter and setter

	// getter

	/**
	 * Used to check if the game is SHUTTED-OFF. <br>
	 * Differs from {@link #isRunning()}, see it for differences.
	 */
	public boolean isAlive() {
		return this.controller.isAlive();
	}

	/**
	 * Simply return a flag, to check if the game is running or not (i.e.
	 * {@link #pause()} has been invoked or not).
	 * 
	 * <br>
	 * Differs from {@link #isAlive()}, see it for differences.
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	// setter

	//

	// TODO ABSTRACT

	/** Override designed */
	public abstract void onCreate();

	public abstract GameModel newGameModel();

	/**
	 * Override designed-.<br>
	 * Differs from {@link #resume()} because resume just change the state of the
	 * current match from stopped to running, in some way, while "start" create all
	 * instances, maps, handlers, threads, sockets, etc etc.
	 */
	public abstract void startGame();

	/** Designed to be overrided */
	public abstract void doOnEachCycle(long millisecToElapse);

	//

	// game object handler

	//

	// TODO CONCRETE METHODS

	/**
	 * Used by other objects and threads (like GUI, sound, animation, etc) to check
	 * if the game is running or paused AND making that tread sleep <br>
	 * Differs from {@link #isRunning()}, see it for differences.
	 */
	public boolean checkAndSleepIsRunning() {
		if (!this.isRunning()) {
			try {
				synchronized (pauseThreadsLock) {
					this.pauseThreadsLock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // make me sleep
		}
		return true;
	}

	public void pause() {
		this.isRunning = false;
	}

	public void resume() {
		this.isRunning = true;
		this.pauseThreadsLock.notifyAll();
	}

	public void runGame() {
		long start, lastDeltaElapsed;
		while(isAlive()) {
			lastDeltaElapsed = MIN_DELTA;
			while(isRunning()) {
				start = System.currentTimeMillis();
				doOnEachCycle(lastDeltaElapsed);
				lastDeltaElapsed = Math.min(MIN_DELTA, System.currentTimeMillis() - start);
			}
		}
	}
}