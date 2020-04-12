package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.misc.GameObjectsProvider;

public class CreaturesProvider<C extends CreatureSimple> extends GameObjectsProvider<C> {
	public static final String NAME = "CreatP";
	// old:
//	ObjGModalityBasedProvider<CreatureSimple>

	public C getCreatureByName(GModality gm, String name) {
		if (gm == null || name == null)
			return null;
//		return getObjByName(name).newInstance(gm);
		return getNewObjByName(gm, name);
	}
}