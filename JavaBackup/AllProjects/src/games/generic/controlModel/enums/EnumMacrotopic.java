package games.generic.controlModel.enums;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.subimpl.GObjectsHolderImpl;
import tools.Comparators;

/**
 * Cluster / collection of {@link GEnumeration} following the same "theme" (for
 * instance, all creatures in game, which may be divided depending on the in-map
 * regions).
 * 
 * @author ottin
 *
 */
public class EnumMacrotopic extends GObjectsHolderImpl<Long, GEnumeration> {

	protected final MapTreeAVL<String, GEnumeration> subclusterByName;

	public EnumMacrotopic() {
		super(Comparators.LONG_COMPARATOR, // ObjectWithID.KEY_EXTRACTOR
				GEnumeration::getID);
		subclusterByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
	}

}