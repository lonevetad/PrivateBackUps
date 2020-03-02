package videogamesOldVersion.common;

import videogamesOldVersion.common.abstractCommon.AbstractSaveData;
import videogamesOldVersion.common.abstractCommon.GameModelGeneric;
import videogamesOldVersion.common.abstractCommon.LoaderGeneric;
import videogamesOldVersion.common.abstractCommon.MainController;
import videogamesOldVersion.common.gui.MainGUI;

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