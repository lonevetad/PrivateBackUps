package games.generic.controlModel;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.PlayerOutside_Generic;
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
 * 
 */
public abstract class GController {

	protected boolean isAlive;
	protected Map<String, GModalityFactory> gameModalitiesFactories;
	protected GModality currentGameModality;
	protected PlayerOutside_Generic player;

	protected GController() {
		this.isAlive = false;
		this.gameModalitiesFactories = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				Comparators.STRING_COMPARATOR);
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
		if (currentGameModality != null)
			System.out.println("GController#setCurrentGameModality : " + currentGameModality.getModalityName());
		this.currentGameModality = currentGameModality;
	}

	//

	//

	/**
	 * Add all {@link GModalityFactory}} to the set of possible modalities,
	 * identified by {@link #getGameModalitiesFactories()}.
	 */
	protected abstract void defineGameModalitiesFactories();

	/** See {@link PlayerGeneric} to see what is meant. */
	protected abstract PlayerOutside_Generic newPlayerOutside();

	//

	public boolean isAlive() {
		return isAlive;
	}

	public boolean isPlaying() {
		GModality gm;
		gm = this.getCurrentGameModality();
		return gm != null && gm.isRunning();
	}

	/** Override designed BUT call <code>super.</code>{@link #init()}}. */
	public void init() {
		defineGameModalitiesFactories();
	}

	/**
	 * Override designed. BUT call <code>super.</code>{@link #init()}}.
	 * <p>
	 * Call {@link #setCurrentGameModality(GModality)}} before invoking me.
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
}