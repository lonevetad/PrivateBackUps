package games.theRisingArmy.main;

import java.awt.image.BufferedImage;

import common.abstractCommon.LoaderGeneric;
import common.mainTools.FileUtilities;

/** TRAr : The Rising Army */
public class Loader_TRAr extends LoaderGeneric {
	private static final long serialVersionUID = -6266306320809463L;
	public static final String myPath_TRAr = superPath + "The Rising Army" + LoaderGeneric.sc//
			, pathImages_TRAr = myPath_TRAr + "img" + sc//
			, pathIcons_TRAr = pathImages_TRAr + "icons" + sc//
			, pathMaps_TRAr = myPath_TRAr + "maps" + sc//
			, extensionMap_TRAr = ".trarmap"//
			, extensionSaves_TRAr = ".trarsave"
	//
	;
	private static Loader_TRAr instance;

	public static Loader_TRAr getInstance() {
		if (instance == null) instance = new Loader_TRAr();
		return instance;
	}

	private Loader_TRAr() {
	}

	@Override
	public String getPath() {
		return myPath_TRAr;
	}

	@Override
	public String getPathImage() {
		return pathImages_TRAr;
	}

	@Override
	public String getPathMap() {
		return pathMaps_TRAr;
	}

	@Override
	public String getPathSaves() {
		return null;
	}

	@Override
	public String getPathSound() {
		return null;
	}

	@Override
	public String getMapExtension() {
		return extensionMap_TRAr;
	}

	@Override
	public String getSavesExtension() {
		return extensionSaves_TRAr;
	}

	public static BufferedImage readManaSymbol(int numberUncoloded) {
		if (numberUncoloded < 0 || numberUncoloded > 9) return null;
		return readManaSymbol(SharedStuffs_TRAr.ManaTypes.Undefined.name() + ' ' + numberUncoloded);
	}

	/***/
	public static BufferedImage readManaSymbol(SharedStuffs_TRAr.ManaTypes mana) {
		return readManaSymbol(mana.name());
	}

	/**
	 * Return the image associated with the given name.<br>
	 * N.B.: performss some I/O operations without cache, so it could be slow.
	 * Cache the results in order to optimize future calls.
	 */
	public static BufferedImage readManaSymbol(String s) {
		Object ret;
		BufferedImage bi;
		bi = null;
		ret = FileUtilities.getBufferedImageScanningFoldersAndSubFolders(s, pathIcons_TRAr, null);
		if (ret != null && ret instanceof BufferedImage) bi = (BufferedImage) ret;
		return bi;
	}
}