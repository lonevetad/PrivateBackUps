package games.generic.controlModel;

import java.util.function.Consumer;

import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.GThread;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GEvent;
import games.generic.controlModel.subimpl.GameThreadsManager;
import games.generic.controlModel.subimpl.IGameModalityTimeBased;
import tools.ObjectWithID;

/**
 * One of the core classes.
 * <p>
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
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link GameObjectsManager}</li>
 * <li>{@link GameObjectsProvidersHolder}</li>
 * <li>{@link GThread}</li>
 * <li>{@link GameObjectsManager}</li>
 * <li>{@link GameObjectsManager}</li>
 * </ul>
 */
public abstract class GModality {
	/**
	 * Used by {@link #runGameCycle()} and usually returned by
	 * {@link #getMinimumMillisecondsEachCycle()}.
	 */
	public static final int MIN_DELTA = 10, MAX_DELTA = 100;

	//

	protected boolean isRunning;
	protected int lastElapsedDeltaTime = 0;
	protected final GController controller;
	protected GModel model;
	protected String modalityName;
	/** Used to suspend threads */
	protected PlayerGeneric player;
	protected final GameObjectsProvidersHolder gameObjectsProviderHolder;
	protected final GameObjectsManager gomDelegated;
	protected final GameThreadsManager gameThreadsManager;

	public GModality(GController controller, String modalityName) {
		this.controller = controller;
		this.modalityName = modalityName;
		this.model = newGameModel();
		this.lastElapsedDeltaTime = this.getMinimumMillisecondsEachCycle();
		this.gameObjectsProviderHolder = controller.getGObjProvidersHolderForGModality(this);
		this.gomDelegated = newGameObjectsManager(); // ((GControllerRPG) controller).get; //
		this.gameThreadsManager = newGameThreadsManager();
		onCreate();
		// il game model deve avere anche l'holder dovuto dal "Misom"
		assert this.getModel()
				.containsObjHolder(this.getGameObjectsManager().getGObjectInSpaceManager().getNameGObjHolder()) : //
		"The model does not have a \"GObjHolder\" instance for ObjectLocated (which is an instance of GObjectInSpaceManager)";
	}

	//
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

	public PlayerGeneric getPlayer() {
		return player;
	}

	public String getModalityName() {
		return modalityName;
	}

	public GameObjectsProvidersHolder getGameObjectsProvider() {
		return gameObjectsProviderHolder;
	}

	/** Get the HUGE delegate of almost everything. */
	public GameObjectsManager getGameObjectsManager() {
		return gomDelegated;
	}

	/**
	 * Returns the minimum amount of milliseconds that is forced to be elapsed
	 * between each cycle. In fact, it's used inside {@link #runGameCycle()}
	 * original implementation.<br>
	 * Set it as <code>0 (zero)</code> to remove each limit, especially FPS limits.
	 */
	public int getMinimumMillisecondsEachCycle() {
		return MIN_DELTA;
	}

	//

	// setter

	public void setModel(GModel model) {
		this.model = model;
	}

	public void setPlayer(PlayerGeneric player) {
		this.player = player;
		if (player != null)
			player.setGameModality(this);
	}

	//

	// TODO ABSTRACT

	/** Override designed BUT call <code>super.</code>{@link #onCreate()}}. */
	public void onCreate() {
		GObjectsInSpaceManager goism;
		/*
		 * Upon setting everything, "gom" and its "GOISM" included, add the goism to the
		 * model as an "objects holder" because that's what it is: an holder of
		 * ObkectLocated
		 */
		goism = this.getGameObjectsManager().getGObjectInSpaceManager();
		this.getModel().addObjHolder(goism.getNameGObjHolder(), goism);
	}

	public abstract GModel newGameModel();

	public abstract CurrencySet newCurrencyHolder();

	/** See {@link PlayerGeneric} to see what is meant. */
	protected abstract PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer);

	/**
	 * Defines and returns the instance of {@link GameObjectsManager} that will be
	 * used in this game modality and supports it in defining the game.<br>
	 * Requires an {@link GEventInterface} as a parameter but it's optional, if the
	 * game modality does not use the events system.
	 */
	protected abstract GameObjectsManager newGameObjectsManager();

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
	 * simple invocation of {@link IGameModalityTimeBased#progressElapsedTime(int)}
	 */
	public abstract void doOnEachCycle(int millisecToElapse);

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

	//

	// TODO threads

	//

	/**
	 * Override designed, by default simply calls
	 * {@link GameThreadsManager#instantiateAllThreads()}.
	 */
	protected void checkAndRebuildThreads() {
		this.gameThreadsManager.instantiateAllThreads();
	}

	/**
	 * Start all kinds of threads
	 */
	protected void startAllThreads() {
		this.gameThreadsManager.startGThreads();
	}

	/**
	 * Override designed - call this at the end on overrides.<br>
	 * Differs from {@link #resume()} because resume just change the state of the
	 * current match from stopped to running, in some way, while "start" create all
	 * instances, maps, handlers, threads, sockets, etc etc.
	 */
	public void startGame() {
		checkAndRebuildThreads();
		System.out.println("\n\n STARTING THREADS, SOON \n\n\n");
		startAllThreads();
	}

	public GameThreadsManager newGameThreadsManager() {
		return new GameThreadsManagerBase(this);
	}

	/**
	 * Used by other objects and threads (like GUI, sound, animation, the game's
	 * ones, etc) to check if the game is running or: If NOT (i.e. the game is
	 * paused), make that thread sleep <br>
	 * Differs from {@link #isRunning()}, see it for differences.
	 * <p>
	 * Delegate the implementation to
	 * {@link GameThreadsManager#isGModalityRunningOrSleep()}.
	 */
	public boolean isRunningOrSleep() {
		return this.gameThreadsManager.isGModalityRunningOrSleep();
	}

	public void pause() {
		this.isRunning = false;
	}

	public void resume() {
		this.isRunning = true;
		this.gameThreadsManager.resumeThreads();
	}

	/**
	 * <i/>WARNING</i>: <b>NOT</b> override designed,<br>
	 * but allowed (in case of tuning or enhancement): just redefine
	 * {@link #doOnEachCycle(int)} instead.
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
		long start;
		int timeToSleep, minDelta;
		minDelta = this.getMinimumMillisecondsEachCycle();
		if (isAlive()) {
			while(isRunningOrSleep()) {
				start = System.currentTimeMillis();
				doOnEachCycle(lastElapsedDeltaTime);
				timeToSleep = (minDelta - ((int) (System.currentTimeMillis() - start)) + 1);

				// "+1" as a rounding factor for nanoseconds
				if (timeToSleep > 0) {
					if (timeToSleep > MAX_DELTA)
						timeToSleep = MAX_DELTA; // do not exceed
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
		this.gameThreadsManager.stopThreads();
	}

	//

	//

	// TODO class

	//

	public static class GameThreadsManagerBase extends GameThreadsManager {
		public GameThreadsManagerBase(GModality gmodality) {
			super(gmodality);
		}

		@Override
		public void instantiateAllThreads() {
			this.addGThread(new GThread(new RunGameInstance(gmodality)));
		}
	}

	// previously was ThreadGame_GameRunner_E1
	protected static class RunGameInstance implements GThread.GTRunnable {
		protected boolean isWorking = true; // when the
		protected GModality gmodality;

		public RunGameInstance(GModality gmodality) {
			super();
			this.gmodality = gmodality;
		}

		@Override
		public void run() {
			while(isWorking) {
				this.gmodality.runGameCycle();
			}
		}

		@Override
		public void stopAndDie() {
			this.isWorking = false;
		}
	}
}