package common;

import common.abstractCommon.AbstractSaveData;
import common.abstractCommon.GameModelGeneric;
import common.abstractCommon.LoaderGeneric;
import common.abstractCommon.MainController;
import common.gui.MainGUI;

public class MainControllerEmpty extends MainController {
	private static final long serialVersionUID = -899258539335L;

	public MainControllerEmpty(LoaderGeneric loaderGeneric, MainGUI mgg) {
		super(loaderGeneric, mgg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getGameName() {
		return "Empty";
	}

	@Override
	public void continueInitNONGUIfields() {
	}

	@Override
	public void act(int milliseconds) {
	}

	@Override
	public EnumGameObjectTileImageCollection newEnumGameObjectTileImageCollection(MainController mainController) {
		return null;
	}

	@Override
	public GameModelGeneric newGameModelGeneric(MainController mainController) {
		return null;
	}

	@Override
	public AbstractSaveData newSaveData(GameModelGeneric gameModel) {
		return null;
	}

	//

	// overridden

}