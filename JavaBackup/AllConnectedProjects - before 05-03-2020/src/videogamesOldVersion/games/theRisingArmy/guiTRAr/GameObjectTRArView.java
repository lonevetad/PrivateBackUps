package games.theRisingArmy.guiTRAr;

import common.GameObjectInMap;
import common.gui.GameObjectInMapView;
import common.gui.MainGUI;
import games.theRisingArmy.abstractTRAr.GameObjectTRAr;

public class GameObjectTRArView extends GameObjectInMapView
// implements common.abstractCommon.behaviouralObjectsAC.ObjectActingOnPassingTime
{
	private static final long serialVersionUID = 5165019989L;

	public GameObjectTRArView(MainGUI mainGui, GameObjectTRAr gameObject) {
		super(mainGui, gameObject);
	}

	GameObjectTRAr trarObject;
	// MainGUI_TRAr mainGUI;

	//

	// TODO GETTER

	// public MainGUI_TRAr getMainGUI() { return mainGUI; }

	// public int getLastFrame() { return lastFrame; }

	public GameObjectTRAr getTrarObject() {
		return trarObject;
	}

	//

	// TODO SETTER

	/*
	 * public ViewTRArObject setMainGUI(MainGUI_TRAr mainGUI) { this.mainGUI = mainGUI; return this;
	 * }
	 */

	/*
	 * public ViewTRArObject setLastFrame(int lastFrame) { this.lastFrame = lastFrame; return this;
	 * }
	 */

	public GameObjectTRArView setTrarObject(GameObjectTRAr trarObject) {
		this.trarObject = trarObject;
		super.setGameObject(trarObject);
		return this;
	}

	@Override
	public GameObjectInMapView setGameObject(GameObjectInMap gameObject) {
		if (gameObject instanceof GameObjectTRAr) {
			setTrarObject((GameObjectTRAr) gameObject);
		}
		return this;
	}

	//

	//

}
