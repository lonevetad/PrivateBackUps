package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

import common.GameObjectInMap;
import common.abstractCommon.MainController;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

public interface GameObjectInMapFactory extends Serializable {
	public static final GameObjectInMapFactory DEFAULT_GAME_OBJECT_IN_MAP_FACTORY = //
			// Avoiding JAVA bugs
			(GameObjectInMapFactory & Serializable) //
			((m, name, ss, isNotSolid) -> {
				GameObjectInMap goim;
				if (m == null || name == null || ss == null) return null;
				goim = new GameObjectInMap(ss);
				goim.setNotSolid(isNotSolid);
				goim.setName(name);
				return goim;
			});

	//

	public GameObjectInMap newGameObjectInMap(MainController main, String name, ShapeSpecification ss,
			boolean isNotSolid);

	//

	public static GameObjectInMapFactory getOrDefault(GameObjectInMapFactory g) {
		return g != null ? g : DEFAULT_GAME_OBJECT_IN_MAP_FACTORY;
	}
}