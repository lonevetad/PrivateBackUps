package games.generic.controlModel;

import java.util.Map;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.misc.CurrencyHolder;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.PlayerInGame_Generic;
import games.generic.controlModel.player.PlayerOutside_Generic;
import games.generic.controlModel.subImpl.IGameModalityTimeBased;
import tools.Comparators;

/**
 * One of the Core classes.<br>
 * See differences with {@link GController}.<br>
 * Represents the real "game", how it works, its type, its modality. Implements
 * every dynamics, rules, win conditions, interactions, etc. Obviously, those
 * concepts could (and should) be defined in separated classes. <br>
 * This class (and its related components) should manage the event-firing
 * systems (i.e. {@link GEvent} and {@link GEventManager}), like "object moved",
 * "spawn creatures / projectiles", "stuffs dropped", "damage dealt", "someone
 * healed", etc through a set of specific methods (that could be wrapped in a
 * specific class).<br>
 * The following examples will provides an idea of what a "modality" is:
 * <ul>
 * <li>PvsIA, 1v1, Multi_VS_Multi, etc</li>
 * <li>chess like, usual RPG through maps, card game, real time strategy (RTS),
 * vehicle based, visual story, etc</li>
 * <li>Examples for RPG and/or RTS: dungeons, open world,
 * YouVsWawesOfEnemies</li>
 * </ul>
 */
public abstract class GModality {
	/**
	 * Used by {@link #runGameCycle()} and usually returned by
	 * {@link #getMinimumMillisecondsEachCycle()}.
	 */
	public static final long MIN_DELTA = 10L;

	//

	protected boolean isRunning;
	protected long lastElapsedDeltaTime = 0;
	protected GController controller;
	protected GModel model;
	protected String modalityName;
	/** Used to suspend threads */
	protected final Object pauseThreadsLock = new Object();
	protected Map<Long, GThread> threadsSleeping; // List<ThreadGame>
	protected PlayerInGame_Generic player;

	public GModality(GController controller, String modalityName) {
		this.controller = controller;
		this.modalityName = modalityName;
		this.model = newGameModel();
		this.threadsSleeping = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR);
//				new LinkedList<>();
		this.lastElapsedDeltaTime = this.getMinimumMillisecondsEachCycle();
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
	 * Simply return a flag. Used to check if the game is running or not (i.e.: the
	 * game is NOT running if {@link #pause()} has been invoked).
	 * 
	 * <br>
	 * Differs from {@link #isAlive()}, see it for differences.
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	public GController getController() {
		return controller;
	}

	public GModel getModel() {
		return model;
	}

	public PlayerInGame_Generic getPlayer() {
		return player;
	}

	public String getModalityName() {
		return modalityName;
	}

	/**
	 * Returns the minimum amount of milliseconds that is forced to be elapsed
	 * between each cycle. In fact, it's used inside {@link #runGameCycle()}
	 * original implementation.<br>
	 * Set it as <code>0 (zero)</code> to remove each limit, especially FPS limits.
	 */
	public long getMinimumMillisecondsEachCycle() {
		return MIN_DELTA;
	}

	//

	public void setModel(GModel model) {
		this.model = model;
	}

	// setter

	//

	// TODO ABSTRACT

	/** Override designed BUT call <code>super.</code>{@link #onCreate()}}. */
	public abstract void onCreate();

	public abstract GModel newGameModel();

	public abstract CurrencyHolder newCurrencyHolder();

	/** See {@link PlayerGeneric} to see what is meant. */
	protected abstract PlayerInGame_Generic newPlayerInGame(PlayerOutside_Generic superPlayer);

	/**
	 * Override designed-.<br>
	 * Differs from {@link #resume()} because resume just change the state of the
	 * current match from stopped to running, in some way, while "start" create all
	 * instances, maps, handlers, threads, sockets, etc etc.
	 */
	public abstract void startGame();

	/**
	 * Override designed.
	 * <p>
	 * Define THE REAL GAME cycle.<br Everything about a cycle-based game is run
	 * there.<br>
	 * Even card-games, chess like and other "not-real time" games (i.e.: turn-based
	 * or other kinds of games that I'm currently not able to list) could use this
	 * method: just pause the {@link Thread} waiting for the event to be fired. OR
	 * simply override both this method and {@link #runGameCycle()} to simply
	 * execute events and not time-based stuffs.
	 * <p>
	 * Invoked by {@link #runGameCycle()}.
	 * 
	 * <p>
	 * NOTE:<br>
	 * (This note should not be wrote, because it requires to know about subclasses
	 * even if this is a super-class, but it's useful, as You would see.)<br>
	 * {@link GModality} implementing "real time" games should implement the
	 * interface {@link IGameModalityTimeBased} and implement this method as a
	 * simple invocation of {@link IGameModalityTimeBased#progressElapsedTime(long)}
	 */
	public abstract void doOnEachCycle(long millisecToElapse);

	/**
	 * Publish and fire the event in some way, if and only if this current Game
	 * Modality supports events, otherwise leave and empty implementation.
	 */
//	public abstract void fireEvent(GEvent event);

	//

	// game object handler

	//

	// TODO CONCRETE METHODS

	/** Add a {@link ObjectWithID} to the {@link GModel}. */
	public boolean addGameObject(ObjectWithID o) {
		GModel gm;
		if (o == null)
			return false;
		gm = this.getModel();
		if (gm != null) {
			return gm.add(o);
		}
		return false;
	}

	public boolean removeGameObject(ObjectWithID o) {
		GModel gm;
		if (o == null)
			return false;
		gm = this.getModel();
		if (gm != null) {
			return gm.remove(o);
		}
		return false;
	}

	public boolean removeAllGameObjects() {
		GModel gm;
		gm = this.getModel();
		if (gm != null) {
			return gm.removeAll();
		}
		return false;
	}

	public boolean containsGameObject(ObjectWithID o) {
		GModel gm;
		if (o == null)
			return false;
		gm = this.getModel();
		if (gm != null) {
			return gm.contains(o);
		}
		return false;
	}

	public void forEachGameObject(Consumer<ObjectWithID> action) {
		GModel gm;
		if (action == null)
			return;
		gm = this.getModel();
		if (gm != null) {
			gm.forEach(action);
		}
	}

	/**
	 * Used by other objects and threads (like GUI, sound, animation, the game's
	 * ones, etc) to check if the game is running or: If NOT (i.e. the game is
	 * paused), make that thread sleep <br>
	 * Differs from {@link #isRunning()}, see it for differences.
	 */
	public boolean isRunningOrSleep() {
		if (!(this.isRunning() && this.isAlive())) {
			try {
				synchronized (pauseThreadsLock) {
					GThread t;
					t = (GThread) Thread.currentThread();
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
	 * <i/>WARNING</i>: <b>NOT</b> override designed,<br>
	 * but allowed: just redefine {@link #doOnEachCycle(long)} instead.
	 * <p>
	 * Perform a SINGLE game cycle/step.<br>
	 * Should be called by the game's thread inside a cycle.<br>
	 * It's designed as a single cycle also to allow an optional step-by-step
	 * execution (some game engines allow it).
	 * <p>
	 * IMPLEMENTATION NOTE:<br>
	 * Each cycle lasts at least the amount of milliseconds provided by
	 * {@link #getMinimumMillisecondsEachCycle()} (currently it returns
	 * {@link #MIN_DELTA}) and a <code>Thread.sleep(..)</code> is invoked if the
	 * elapsed time during the cycle is lower than this threshold, so the maximum
	 * FPS count is <code>1000/{@link #getMinimumMillisecondsEachCycle()}</code>.
	 */
	public void runGameCycle() {
		long start, timeToSleep, minDelta;
		minDelta = this.getMinimumMillisecondsEachCycle();
		if (isAlive()) {
			while(isRunningOrSleep()) {
				start = System.currentTimeMillis();
				doOnEachCycle(lastElapsedDeltaTime);
				timeToSleep = minDelta - ((System.currentTimeMillis() - start) + 1);
				// "+1" as a rounding factor for nanoseconds
				if (timeToSleep > 0) {
					try {
						Thread.sleep(timeToSleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("died runGame");
			try {
				Thread.sleep(Math.min(MIN_DELTA, minDelta));
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