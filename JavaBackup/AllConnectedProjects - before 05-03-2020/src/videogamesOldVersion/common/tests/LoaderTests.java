package common.tests;

import java.io.File;

import common.abstractCommon.LoaderGeneric;

public class LoaderTests extends LoaderGeneric {

	public static final String myPath = superPath + "Tests" + LoaderGeneric.sc//
			, pathImages = myPath + "img" + sc//
			, pathIcons = pathImages + "icons" + sc//
			, pathMaps = myPath + "maps" + sc//
			, extensionMap = ".testmap"//
			, extensionSaves = ".testsave"//
	;

	static {
		File f;
		f = new File(myPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		f = null;
	}

	public LoaderTests() {
		super();
	}

	private static LoaderTests instance = null;

	public static LoaderTests getInstance() {
		if (instance == null) instance = new LoaderTests();
		return instance;
	}

	@Override
	public String getPath() {
		return myPath;
	}

	@Override
	public String getPathImage() {
		return pathImages;
	}

	@Override
	public String getPathMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathSaves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathSound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMapExtension() {
		return extensionMap;
	}

	@Override
	public String getSavesExtension() {
		return extensionSaves;
	}

}
