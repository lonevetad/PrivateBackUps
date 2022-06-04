package games.generic.controlModel.enums;

import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.subimpl.GObjectsHolderImpl;
import tools.Comparators;

public class EnumsManager extends GObjectsHolderImpl<EnumMacrotopic> {

	public EnumsManager() {
		super();
		this.enumsByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
	}

	protected MapTreeAVL<String, EnumMacrotopic> enumsByName;

	//

	public boolean containsByEnumName(String nameEnum) { return this.enumsByName.containsKey(nameEnum); }

	public EnumMacrotopic getByEnumName(String nameEnum) { return this.enumsByName.get(nameEnum); }

	//

	@Override
	public boolean add(EnumMacrotopic o) {
		boolean added;
		added = super.add(o);
		if (added) { this.enumsByName.put(o.getName(), o); }
		return added;
	}

	@Override
	public boolean remove(EnumMacrotopic o) {
		boolean removed;
		removed = super.remove(o);
		if (removed) { this.enumsByName.remove(o.getName()); }
		return removed;
	}

	@Override
	public boolean removeAll() {
		this.enumsByName.clear();
		return super.removeAll();
	}

	@Override
	public void forEach(Consumer<EnumMacrotopic> action) { this.enumsByName.forEach((id, to) -> action.accept(to)); }
}