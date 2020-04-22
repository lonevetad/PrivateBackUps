package videogamesOldVersion.common;

import java.util.Iterator;

import tools.Comparators;
import videogamesOldVersion.common.abstractCommon.AbstractEnumElementGOTI;
import videogamesOldVersion.common.abstractCommon.AbstractEnumGOTI;

/** Implementation of {@link AbstractEnumGOTI}. */
public class EnumGameObjectTileImage implements AbstractEnumGOTI {
	private static final long serialVersionUID = -198012887835L;

	public <T extends Enum<T> & AbstractEnumElementGOTI> EnumGameObjectTileImage(T[] preexistingSet) {
		this((String) null);
		this.addAll(preexistingSet);
	}

	public EnumGameObjectTileImage(String nameEnum) {
		this.nameEnum = nameEnum != null ? nameEnum : this.getClass().getSimpleName();
		allTileImageByID = new Map<>(Comparators.INTEGER_COMPARATOR);
		allTileImageByImageName = new Map<>(Comparators.STRING_COMPARATOR);
	}

	protected String nameEnum;
	protected Map<Integer, AbstractEnumElementGOTI> allTileImageByID;
	protected Map<String, AbstractEnumElementGOTI> allTileImageByImageName;

	//

	@Override
	public String getNameEnum() {
		return nameEnum;
	}

	@Override
	public Map<Integer, AbstractEnumElementGOTI> getAllElementsByID() {
		return allTileImageByID;
	}

	@Override
	public Map<String, AbstractEnumElementGOTI> getAllElementsByImageName() {
		return allTileImageByImageName;
	}

	//

	@Override
	public Iterator<AbstractEnumElementGOTI> iterator() {
		return allTileImageByImageName.iteratorValue();
	}

}