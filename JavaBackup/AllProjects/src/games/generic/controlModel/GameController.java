package games.generic.controlModel;

import java.util.Map;

import dataStructures.MapTreeAVL;
import tools.Comparators;

/**
 * Layer between the View and the actual game: {@linkGameModality} instances.
 */
public abstract class GameController {

	protected Map<String, GameModalityFactory> gameModalitiesFactories;
	protected GameModality currentGameModality;

	protected GameController() {
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

	public abstract boolean isAlive();

	/**
	 * Add all {@link GameModalityFactory}} to the set of possible modalities,
	 * identified by {@link #getGameModalitiesFactories()}.
	 */
	protected abstract void defineGameModalitiesFactories();

	public abstract void init();

	/** DESTRY EVERYTHING WITH DOUBLE OF THANOS'S EFFICEINCY. */
	public abstract void closeAll();

	/** Override designed */
	public void startGame() {
		this.getCurrentGameModality().startGame();
	}

	/** Override designed */
	public void pauseGame() {
		this.getCurrentGameModality().pause();
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