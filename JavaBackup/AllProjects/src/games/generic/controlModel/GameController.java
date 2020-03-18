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
		this.currentGameModality = currentGameModality;
	}

	//

	//

	/**
	 * Add all {@link GameModalityFactory}} to the set of possible modalities,
	 * identified by {@link #getGameModalitiesFactories()}.
	 */
	protected abstract void defineGameModalitiesFactories();

	public abstract void init();

	/**
	 * DESTRY EVERYTHING WITH DOUBLE OF THANOS'S EFFICEINCY.
	 * <p>
	 * Remember to set the flag {@link #isAlive} to <code>false</code>.
	 */
	public abstract void closeAll();

	//

	public boolean isAlive() {
		return isAlive;
	}

	public boolean isPlaying() {
		GameModality gm;
		gm = this.getCurrentGameModality();
		return gm != null && gm.isRunning();
	}

	/** Override designed */
	public void startGame() {
		this.getCurrentGameModality().startGame();
		this.isAlive = true;
	}

	/** Override designed */
	public void pauseGame() {
		this.getCurrentGameModality().pause();
	}

	/** Override designed */
	public void resumeGame() {
		this.getCurrentGameModality().resume();
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