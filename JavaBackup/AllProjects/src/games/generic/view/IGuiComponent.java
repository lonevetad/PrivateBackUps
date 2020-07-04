package games.generic.view;

import games.generic.controlModel.GModality;

public interface IGuiComponent {

	public GameView getView();

	public void setView(GameView view);

	public default GModality getGameModality() { return getView().getCurrentModality(); }

	/** Perform an action upon adding this component on the given view. */
	public void onAddingOnView(GameView view);

	/** The reverse of {@link #onAddingOnView(GameView)}. */
	public void onRemovingOnView(GameView view);
}