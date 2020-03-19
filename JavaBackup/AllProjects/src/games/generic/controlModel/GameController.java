package games.generic.controlModel;

import java.util.Map;

import dataStructures.MapTreeAVL;
import tools.Comparators;

/**
 * Layer between the View and the actual game: {@linkGameModality} instances.
 */
public abstract class GameController {

	protected boolean isAlive;
	protected Map<String, GameModalityFactory> gameModalitiesFactories;
	protected GameModality currentGameModality;

	protected GameController() {
		this.isAlive = false;
		this.gameModalitiesFactories = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				Comparators.STRING_COMPARATOR);
	}

	//

	public GameModality getCurrentGameModality() {
		return currentGameModality;
	}

	public Map<String, GameModalityFactory> getGameModalitiesFactories() {
		return gameModalitiesFactories;
	}

	//

	public void setCurrentGameModality(GameModality currentGameModality) {
		System.out.println("GameCOntroller#setCurrentGameModality : " + currentGameModality.getModalityName());
		this.currentGameModality = currentGameModality;
	}

	//

	//

	/**
	 * Add all {@link GameModalityFactory}} to the set of possible modalities,
	 * identified by {@link #getGameModalitiesFactories()}.
	 */
	protected abstract void defineGameModalitiesFactories();

	//

	public boolean isAlive() {
		return isAlive;
	}

	public boolean isPlaying() {
		GameModality gm;
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
	 * Call {@link #setCurrentGameModality(GameModality)}} before invoking me.
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
	public GameModality newModalityByName(String name) {
		GameModalityFactory gmf;
		gmf = this.getGameModalitiesFactories().get(name);
		return gmf == null ? null : gmf.newGameModality(this, name);
	}
}