package games.theRisingArmy.guiTRAr;

import common.gui.MainGUI;
import common.gui.MapEditor;
import common.gui.MapGameView;
import games.theRisingArmy.MainController_TheRisingArmy;

public class MainGUI_TRAr extends MainGUI {
	private static final long serialVersionUID = 56123087408L;

	public MainGUI_TRAr() {
		super();
		commonConstructor();
	}

	public MainGUI_TRAr(MainController_TheRisingArmy mgc) {
		super(mgc);
		commonConstructor();
	}

	@Override
	public void continueInitGUI() {
		// this.setAllTiles(new EnumTileMapListHolder_TRAr());
		// this.getMapEditor().refreshEnumsLists();
		// nothing else for now
	}

	public void commonConstructor() {
		this.setLog(null);
	}

	@Override
	public void updateGameAnimations(int milliseconds) {
		super.updateGameAnimations(milliseconds);
	}

	@Override
	public MapEditor newMapEditor(MainGUI mainGui) {
		MapEditor me;
		me = new MapEditorTRAr();
		me.setMainGUI(this);
		return me;
	}

	@Override
	public MapGameView newMapGameView_TheRealGame(MainGUI mainGui) {
		// TODO Auto-generated method stub
		return null;
	}
}