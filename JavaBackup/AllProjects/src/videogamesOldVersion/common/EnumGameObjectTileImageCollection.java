package videogamesOldVersion.common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import tools.Comparators;
import videogamesOldVersion.common.abstractCommon.AbstractEnumElementGOTI;
import videogamesOldVersion.common.abstractCommon.AbstractEnumGOTI;
import videogamesOldVersion.common.abstractCommon.MainController;
import videogamesOldVersion.common.gui.TileImage;

/**
 * Generic class that collects some instances of
 * {@link EnumGameObjectTileImage}.<br>
 * This class represents a huge collection of enumerations of {@link TileImage}
 * and {@link GameObjectInMap} (more precisely their allocators and wrappers:
 * {@link AbstractEnumElementGOTI}), divided in some sub-collection: a
 * sub-collection is implemented as {@link EnumGameObjectTileImage}.<br>
 * Those sub-collection could be, for example: "nature", "buildings", "plans",
 * "creatures", "magic", "machines", ecc.<br>
 * This class is meant to hold all of them in a single place and retrieve them.
 * <p>
 * NOTE: The preferred way to retrieve them is through a {@link String} key,
 * that could be obtained through {@link AbstractEnumElementGOTI#getName()},
 * because the String key ensure less collisions, but it's slower because of the
 * String comparison algorithm. <br>
 * The other way is through a {@link Integer} key, that could be obtained
 * through {@link AbstractEnumElementGOTI#getID()}, it's faster but could
 * introduce <i>non-determinism</i>: the Integer value could be the ordinal,
 * like the one returned by {@link Enum#ordinal()}, of the
 * {@link AbstractEnumGOTI} enumeration and in case of many instances of
 * {@link AbstractEnumGOTI} that ordinal could be replicated, so the fetch
 * operation could return a unpredictable value.
 * <p>
 * Edited: 07/12/2017
 */
public class EnumGameObjectTileImageCollection implements Serializable, Iterable<AbstractEnumGOTI> {
	private static final long serialVersionUID = 51609090958L;

	public EnumGameObjectTileImageCollection() {
		allEnumTileImage = MapTreeAVL.newMap(Comparators.STRING_COMPARATOR);
	}

	protected MapTreeAVL<String, AbstractEnumGOTI> allEnumTileImage;

	//

	//

	public boolean isEmpty() {
		return allEnumTileImage.isEmpty();
	}

	public boolean holdsSomeEnum() {
		return !allEnumTileImage.isEmpty();
	}

	public void addEnumTileImage(AbstractEnumGOTI tse) {
		if (tse != null && tse.getNameEnum() != null) {
			allEnumTileImage.put(tse.getNameEnum(), tse);
		}
	}

	// public TileImage getTileImageInstance(MainGenericController main, Integer
	// id) {

	/**
	 * Should be used if the key is a {@link String}.<br>
	 * WARNING: If is an ID, this method could return the first it found, without
	 * any guarantee of what {@link AbstractEnumGOTI} was picked from.
	 */
	public TileImage getTileImage(MainController main, Object key) {
		boolean isInt;
		TileImage t;
		AbstractEnumGOTI eti;
		Iterator<AbstractEnumGOTI> iter;
		// AbstractEnumElementTileImageFactory tmic;
		Integer i;
		String s;
		if (main == null || key == null)
			return null;
		// allTilemapSingletonEnums.forEachValues((k, v) -> {
		// })
		if (!((isInt = (key instanceof Integer)) || key instanceof String))
			throw new IllegalArgumentException(
					"Cannot instance a TileImage without its ID (Integer) or its image's filenme (String)");
		i = null;
		s = null;
		if (isInt)
			i = (Integer) key;
		else
			s = (String) key;
		// loader = main.getLoader();
		eti = null;
		t = null;
		iter = iterator();
		while(iter.hasNext() && t == null) {
			eti = iter.next();
			if (eti != null) {
				// tmic = isInt ? tse.fetchTileImageElement(i) :
				// tse.fetchTileImageElement(s);
				// t = tse.getTileImageInstance(main, id);
				// if (tmic != null) t = tmic.getTileImageInstance(main);
				t = eti.newTileImage(main, i, s);
			}
		}
		return t;
	}

	/**
	 * Fetch (and/or instantiate) a {@link TileImage} through its name (a equivalent
	 * {@link String} of {@link TileImage#getImageName()}), without any other
	 * information.<br>
	 * This method is slower than others because this collections iterate all over
	 * the enumerations (instances of {@link AbstractEnumGOTI}) looking for the one
	 * holding the given name. In fact, it simply calls
	 * {@link #getTileImage(MainController, Object)}.
	 */
	public TileImage getTileImageByImageName(MainController main, String key) {
		return getTileImage(main, key);
	}

	/**
	 * Like
	 * {@link #getTileImageByEnumnameImagename(MainController, String, String)}, but
	 * giving the ID (a {@link Integer}, same as {@link TileImage#getID()} and
	 * {@link GameObjectInMap#getIdEnumElement()}) as key instead of its name. Its
	 * faster than the String version.
	 */
	public TileImage getTileImageByEnumnameID(MainController main, String enumName, Integer id) {
		return getTileImageByEnumnameBothKeys(main, enumName, null, id);
	}

	/**
	 * Like {@link #getTileImageByImageName(MainController, String)}, but thanks to
	 * the second parameter ({@link String} enumName), the enumeration's name, the
	 * fetch process is faster.<br>
	 * This method calls
	 * {@link #getTileImageByEnumnameBothKeys(MainController, String, String, Integer)}
	 * putting <code>null</code> to the missing parameter.
	 */
	public TileImage getTileImageByEnumnameImagename(MainController main, String enumName, String imageName) {
		return getTileImageByEnumnameBothKeys(main, enumName, imageName, null);
	}

	/**
	 * Fetch (and/or instantiate) a {@link TileImage} thanks to the second parameter
	 * ({@link String} enumName), the enumeration's name, and at least one of the
	 * key informations : the name (a {@link String}, as returned by
	 * {@link TileImage#getImageName()}) or the ID (a {@link Integer}, as returned
	 * by {@link TileImage#getID()}).
	 */
	public TileImage getTileImageByEnumnameBothKeys(MainController main, String enumName, String imageName,
			Integer id) {
		AbstractEnumGOTI eti;
		if (main == null || enumName == null || (imageName == null && id == null))
			return null;
		eti = this.allEnumTileImage.get(enumName);
		return (eti == null) ? null : eti.newTileImage(main, id, imageName);
	}

	/**
	 * Should be used if the key is a {@link String}.<br>
	 * WARNING: If is an ID, this method could return the first it found, without
	 * any guarantee of what {@link AbstractEnumGOTI} was picked from.
	 */
	public GameObjectInMap getGameObjectInMap(MainController main, Object key) {
		boolean isInt;
		GameObjectInMap goim;
		AbstractEnumGOTI eti;
		Iterator<AbstractEnumGOTI> iter;
		Integer i;
		String s;
		if (main == null || key == null)
			return null;
		if (!((isInt = (key instanceof Integer)) || key instanceof String))
			throw new IllegalArgumentException(
					"Cannot instance a TileImage without its ID (Integer) or its image's filenme (String)");
		i = null;
		s = null;
		if (isInt)
			i = (Integer) key;
		else
			s = (String) key;
		eti = null;
		goim = null;
		iter = iterator();
		while(iter.hasNext() && goim == null) {
			eti = iter.next();
			if (eti != null) {
				goim = eti.newGameObjectInMap(main, i, s);
			}
		}
		return goim;
	}

	/**
	 * Fetch (and/or instantiate) a {@link GameObjectInMap} through its name (a
	 * equivalent {@link String} of {@link GameObjectInMap#getName()}), without any
	 * other information.<br>
	 * This method is slower than others because this collections iterate all over
	 * the enumerations (instances of {@link AbstractEnumGOTI}) looking for the one
	 * holding the given name. In fact, it simply calls
	 * {@link #getGameObjectInMap(MainController, Object)}.
	 */
	public GameObjectInMap getGameObjectInMapByName(MainController main, String key) {
		return getGameObjectInMap(main, key);
	}

	/**
	 * Like
	 * {@link #getTileImageByEnumnameImagename(MainController, String, String)}, but
	 * giving the ID (a {@link Integer}, same as {@link TileImage#getID()} and
	 * {@link GameObjectInMap#getIdEnumElement()}) as key instead of its name. Its
	 * faster than the String version.
	 */
	public GameObjectInMap getGameObjectInMapByEnumnameID(MainController main, String enumName, Integer id) {
		return getGameObjectInMapByEnumnameBothKeys(main, enumName, null, id);
	}

	/**
	 * Like {@link #getGameObjectInMapByName(MainController, String)}, but thanks to
	 * the second parameter ({@link String} enumName), the enumeration's name, the
	 * fetch process is faster.<br>
	 * This method calls
	 * {@link #getGameObjectInMapByEnumnameBothKeys(MainController, String, String, Integer)}
	 * putting <code>null</code> to the missing parameter.
	 */
	public GameObjectInMap getGameObjectInMapByEnumnameImagename(MainController main, String enumName,
			String imageName) {
		return getGameObjectInMapByEnumnameBothKeys(main, enumName, imageName, null);
	}

	/**
	 * Fetch (and/or instantiate) a {@link GameObjectInMap} thanks to the second
	 * parameter ({@link String} enumName), the enumeration's name, and at least one
	 * of the key informations : the name (a {@link String}, as returned by
	 * {@link GameObjectInMap#getName()}) or the ID (a {@link Integer}, as returned
	 * by {@link GameObjectInMap#getIdEnumElement()}).
	 */
	public GameObjectInMap getGameObjectInMapByEnumnameBothKeys(MainController main, String enumName, String imageName,
			Integer id) {
		AbstractEnumGOTI eti;
		if (main == null || enumName == null || (imageName == null && id == null))
			return null;
		eti = this.allEnumTileImage.get(enumName);
		return (eti == null) ? null : eti.newGameObjectInMap(main, id, imageName);
	}

	//

	@Override
	public Iterator<AbstractEnumGOTI> iterator() {
		return new Iterator_ETIC(this);
	}

	public void forEachEnumElement(BiConsumer<String, AbstractEnumElementGOTI> toDo) {
		if (toDo != null && holdsSomeEnum()) {
			for (AbstractEnumGOTI egoti : this) {
				egoti.forEachEnumElement(toDo);
			}
		}
	}

	/* wrapping a real iterator */
	public static class Iterator_ETIC implements Iterator<AbstractEnumGOTI> {
		AbstractEnumGOTI prevkey;
		EnumGameObjectTileImageCollection lh;
		private Iterator<String> iter;

		Iterator_ETIC(EnumGameObjectTileImageCollection lh) {
			this.lh = lh;
			iter = lh.allEnumTileImage.iteratorKey();
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public AbstractEnumGOTI next() {
			return prevkey = lh.allEnumTileImage.get(iter.next());
		}

		@Override
		public void remove() {
			if (prevkey != null) {
				lh.allEnumTileImage.remove(prevkey.getNameEnum());
				prevkey = null;
			}
		}
	}
}