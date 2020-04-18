package games.generic.controlModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.misc.GModalityFactory;
import games.generic.controlModel.misc.LoaderGameObjects;
import games.generic.controlModel.misc.LoaderGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.theRisingAngel.LoaderConfigurations;
import tools.Comparators;

/**
 * One of the Core classes.<br>
 * Layer between the View and the actual game: {@link GModality} instances.
 * There lies ALL game's implementations (i.e.: when the player "plays", what
 * he/she can do depends on {@link GModality}s' implementations. Think about
 * playing cards, RPG or driving vehicles).<br>
 * The Controller manages:
 * <ul>
 * <li>Settings</li>
 * <li>Saves</li>
 * <li>Player's data</li>
 * <li>Chat and other media channels</li>
 * <li>{@link GModality}, creating and destroying them</li>
 * <li>Connections: Internet, DataBase, banks, etc</li>
 * <li>If available, markets, editors, etc</li>
 * <li>etc</li>
 * </ul>
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link }></li>
 * </ul>
 */
public abstract class GController {

	protected boolean isAlive;
	protected Map<String, GModalityFactory> gameModalitiesFactories;
	protected GModality currentGameModality;
	protected UserAccountGeneric user;
	protected final List<LoaderGameObjects<? extends ObjectNamed>> gameObjectsLoader;
	protected final LoaderGeneric loaderConfigurations;

	/** Create everything and loads everything as well. */
	protected GController() {
		this.isAlive = false;
		this.gameModalitiesFactories = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				Comparators.STRING_COMPARATOR);
		this.gameObjectsLoader = new LinkedList<>();
		this.loaderConfigurations = newLoaderConfigurations(this);
	}

	//

	public GModality getCurrentGameModality() {
		return currentGameModality;
	}

	public Map<String, GModalityFactory> getGameModalitiesFactories() {
		return gameModalitiesFactories;
	}

	//

	public void setCurrentGameModality(GModality currentGameModality) {
		this.currentGameModality = currentGameModality;
	}

	//

	//

	/**
	 * Add all {@link GModalityFactory}} to the set of possible modalities,
	 * identified by {@link #getGameModalitiesFactories()}.
	 */
	protected abstract void defineGameModalitiesFactories();

	/** See {@link UserAccountGeneric} to see what is meant. */
	protected abstract UserAccountGeneric newUserAccount();

	/**
	 * Defines and return a specific instance of {@link GameObjectsProvidersHolder}
	 * for a particular subclass of {@link GModality}: some games could define
	 * different sets of game objects, based on the modality they are destined to be
	 * used.<br>
	 * Usually, this method just return the value of a single variable.
	 * <p>
	 * For instance, the game "The Rising Army" will differs two game modalities:
	 * one having some degree of randomness, the other one that is totally
	 * deterministic and aspects like damage ranges or "something that scatters
	 * randomly, like grape bombs" are fixed (for instance, grapes of grape bombs
	 * expands in a symmetric and radial way). So, there would exists abilities
	 * designed in a parallel way so that they could have random intervals or fixed
	 * values. (In that case, it's advised to define once the ability and only one
	 * {@link GameObjectsProvidersHolder}, just passing to that ability's
	 * constructor the flag about if the game modality allows randomness or not).
	 */
	protected abstract GameObjectsProvidersHolder getGObjProvidersHolderForGModality(GModality gm);

//

	/** Override designed BUT call <code>super.</code>{@link #init()}}. */
	protected void initNonFinalStuffs() {
		defineGameModalitiesFactories();
		user = this.newUserAccount();
		this.loaderConfigurations.loadInto(this);
		loadAll();
	}

	protected <E extends ObjectNamed> void addGameObjectLoader(LoaderGameObjects<E> ol) {
		this.gameObjectsLoader.add(ol);
	}

	/** Load everything that needs to be loaded */
	protected void loadAll() {
		this.gameObjectsLoader.forEach(l -> l.loadInto(this));
	}

	protected LoaderGeneric newLoaderConfigurations(GController cgRPG) {
		return new LoaderConfigurations();
	}

	//

	//

	public boolean isAlive() {
		return isAlive;
	}

	public boolean isPlaying() {
		GModality gm;
		gm = this.getCurrentGameModality();
		return gm != null && gm.isRunning();
	}

	/**
	 * The name must be selected from the keys used during the call
	 * {@link #defineGameModalitiesFactories()} or, at least, contained by
	 * {@link #getGameModalitiesFactories()}.
	 */
	public GModality newModalityByName(String name) {
		GModalityFactory gmf;
		gmf = this.getGameModalitiesFactories().get(name);
		return gmf == null ? null : gmf.newGameModality(this, name);
	}

	/**
	 * Override designed. BUT call <code>super.</code>{@link #startGame()}}.<br>
	 * Starts the current {@link GModality}.
	 * <p>
	 * Call {@link #setCurrentGameModality(GModality)}} before invoking it.
	 */
	public void startGame() {
		this.getCurrentGameModality().startGame();
		this.isAlive = true;
		this.resumeGame(); // make it run, added on 19/03/2020
	}

	/** Override designed BUT call <code>super.</code>{@link #pauseGame()}}. */
	public void pauseGame() {
		this.getCurrentGameModality().pause();
	}

	/** Override designed BUT call <code>super.</code>{@link #resumeGame()}}. */
	public void resumeGame() {
		this.getCurrentGameModality().resume();
	}

	/**
	 * DESTRY EVERYTHING WITH DOUBLE OF THANOS'S EFFICEINCY.
	 * <p>
	 * Remember to set the flag {@link #isAlive} to <code>false</code>.
	 */
	public void closeAll() {
		this.isAlive = false;
		if (this.getCurrentGameModality() != null) {
			this.getCurrentGameModality().closeAll();
			this.setCurrentGameModality(null);
		}
	}

	//

	//

}