package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.misc.ObjGModalityBasedProvider;

public class CreatureProvider extends ObjGModalityBasedProvider<BaseCreatureRPG> {
	// old:
//	ObjGModalityBasedProvider<CreatureSimple>

	/** Should be preferred over {@link #getObjIdentifiedByID(Integer)}. */
	public BaseCreatureRPG getCreatureByName(GModality gm, String name) {
		if (gm == null || name == null)
			return null;
		return getObjIdentifiedByName(name).newInstance(gm);
	}
}