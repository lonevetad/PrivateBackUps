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

	/**
	 * IMPORTANT notes for overrider:
	 * <ul>
	 * <li>Should invoke {@link GModality#removeGameObject(GameObjectGeneric)}, as
	 * the default implementation does</li>
	 * <li>It's not needed to call {@link #onRemovedFromGame(GModality)}: it's
	 * already called by {@link GModality#removeGameObject(GameObjectGeneric)}.</li>
	 * </ul>
	 */
	public default void removeMeToGame(GModality gm) { gm.removeGameObject(this); }
}