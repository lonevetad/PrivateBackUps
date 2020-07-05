package games.generic.controlModel.gObj;

import games.generic.controlModel.GModality;
import tools.ObjectNamedID;

public interface GameObjectGeneric extends ObjectNamedID {

	/**
	 * Opposite of {@link #onRemovedFromGame(GModality)}, is an actions performed
	 * upon upon being added to the game, in some way (into the "map", usually).<br>
	 * Usually called by {@link GModality#addGameObject(GameObjectGeneric)}.
	 */
	public void onAddedToGame(GModality gm);

	public default void addMeToGame(GModality gm) { gm.addGameObject(this); }

	/**
	 * Opposite of {@link #onAddedToGame(GModality)}, performing clean-up actions
	 * and called by {@link GModality#removeGameObject(GameObjectGeneric)}.
	 */
	public void onRemovedFromGame(GModality gm);

	public default void removeMeToGame(GModality gm) { gm.removeGameObject(this); }
}