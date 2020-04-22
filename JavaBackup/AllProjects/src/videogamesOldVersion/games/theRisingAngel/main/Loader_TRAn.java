package videogamesOldVersion.games.theRisingAngel.main;

import videogamesOldVersion.common.abstractCommon.LoaderGeneric;

public class Loader_TRAn extends LoaderGeneric {
	private static final long serialVersionUID = 858044051L;
	public static final String myPath_TRAn = superPath + "The Rising Angel" + LoaderGeneric.sc//
			, pathImages_TRAn = myPath_TRAn + "img" + sc//
			, pathIcons_TRAn = pathImages_TRAn + "icons" + sc//
			, pathMaps_TRAn = myPath_TRAn + "maps" + sc//
			, extensionMap_TRAn = ".tranmap"//
			, extensionSaves_TRAn = ".transave"
	//
	;
	private static Loader_TRAn instance;

	public static Loader_TRAn getInstance() {
		if (instance == null)
			instance = new Loader_TRAn();
		return instance;
	}

	public Loader_TRAn() {
	}

	@Override
	public String getPath() {
		return myPath_TRAn;
	}

	@Override
	public String getPathImage() {
		return pathImages_TRAn;
	}

	@Override
	public String getPathMap() {
		return pathMaps_TRAn;
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
		return extensionMap_TRAn;
	}

	@Override
	public String getSavesExtension() {
		return extensionSaves_TRAn;
	}
}