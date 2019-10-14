package common;

import java.util.Iterator;

import common.abstractCommon.AbstractEnumElementGOTI;
import common.abstractCommon.AbstractEnumGOTI;
import common.mainTools.Comparators;
import tools.RedBlackTree;

/** Implementation of {@link AbstractEnumGOTI}. */
public class EnumGameObjectTileImage implements AbstractEnumGOTI {
	private static final long serialVersionUID = -198012887835L;

	public <T extends Enum<T> & AbstractEnumElementGOTI> EnumGameObjectTileImage(T[] preexistingSet) {
		this((String) null);
		this.addAll(preexistingSet);
	}

	public EnumGameObjectTileImage(String nameEnum) {
		this.nameEnum = nameEnum != null ? nameEnum : this.getClass().getSimpleName();
		allTileImageByID = new RedBlackTree<>(Comparators.INTEGER_COMPARATOR);
		allTileImageByImageName = new RedBlackTree<>(Comparators.STRING_COMPARATOR);
	}

	protected String nameEnum;
	protected RedBlackTree<Integer, AbstractEnumElementGOTI> allTileImageByID;
	protected RedBlackTree<String, AbstractEnumElementGOTI> allTileImageByImageName;

	//

	@Override
	public String getNameEnum() {
		return nameEnum;
	}

	@Override
	public RedBlackTree<Integer, AbstractEnumElementGOTI> getAllElementsByID() {
		return allTileImageByID;
	}

	@Override
	public RedBlackTree<String, AbstractEnumElementGOTI> getAllElementsByImageName() {
		return allTileImageByImageName;
	}

	//

	@Override
	public Iterator<AbstractEnumElementGOTI> iterator() {
		return allTileImageByImageName.iteratorValue();
	}

}