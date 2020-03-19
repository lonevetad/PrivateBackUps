package games.generic.controlModel;

import java.util.Map;

import dataStructures.MapTreeAVL;
import tools.Comparators;

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
	protected Map<Long, ThreadGame> threadsSleeping; // List<ThreadGame>

	public GameModality(GameController controller, String modalityName) {
		this.controller = controller;
		this.modalityName = modalityName;
		this.model = newGameModel();
		threadsSleeping = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR);
//				new LinkedList<>();
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

	public GameController getController() {
		return controller;
	}

	public GameModel getModel() {
		return model;
	}

	public String getModalityName() {
		return modalityName;
	}

	//

	public void setModel(GameModel model) {
		this.model = model;
	}

	// setter

	//

	// TODO ABSTRACT

	/** Override designed BUT call <code>super.</code>{@link #onCreate()}}. */
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
	 * if the game is running or, if paused, make that thread sleep <br>
	 * Differs from {@link #isRunning()}, see it for differences.
	 */
	public boolean checkIsRunningElseSleep() {
		if (!(this.isRunning() && this.isAlive())) {
			try {
				synchronized (pauseThreadsLock) {
					ThreadGame t;
					t = (ThreadGame) Thread.currentThread();
					threadsSleeping.put(t.getId(), t);
//					threadsSleeping.add(t);
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
		synchronized (pauseThreadsLock) {
			threadsSleeping.clear();
			this.pauseThreadsLock.notifyAll();
		}
	}

	/**
	 * Perform a SINGLE game cycle/step.<br>
	 * Should be called by the game's thread inside a cycle.<br>
	 * It's designed as a single cycle to allow step-by-step execution.
	 * <p>
	 * No exactly "override designed", redefine {@link #doOnEachCycle(long)}
	 * instead.
	 */
	public void runGameCycle() {
		long start, lastDeltaElapsed;
		if (isAlive()) {
			lastDeltaElapsed = MIN_DELTA;
			while(checkIsRunningElseSleep()) {
				start = System.currentTimeMillis();
				doOnEachCycle(lastDeltaElapsed);
				lastDeltaElapsed = Math.min(MIN_DELTA, System.currentTimeMillis() - start);
			}
		} else {
			System.out.println("died runGame");
			try {
				Thread.sleep(MIN_DELTA);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/** Override AND call the super implementation. */
	public void closeAll() {
		this.pause();
		synchronized (pauseThreadsLock) {
			this.pauseThreadsLock.notifyAll();
			threadsSleeping.forEach(//
					(id, t) -> { // t
						/*
						 * try { t.interrupt(); } catch (SecurityException e) { e.printStackTrace(); }
						 */
						t.stopAndDie();
					});
		}
	}
}