package games.generic.view;

import games.generic.controlModel.GModality;

public class GuiComponent {

	public GuiComponent(GameView view) {
		super();
		this.view = view;
	}

	protected GameView view;

	//

	public GameView getView() { return view; }

	public GModality getGameModality() { return view.getCurrentModality(); }

	public void setView(GameView view) { this.view = view; }

}