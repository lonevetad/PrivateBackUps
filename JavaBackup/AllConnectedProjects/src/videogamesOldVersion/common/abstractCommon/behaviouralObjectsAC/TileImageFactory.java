package common.abstractCommon.behaviouralObjectsAC;

import java.io.IOException;
import java.io.Serializable;

import common.abstractCommon.LoaderGeneric;
import common.abstractCommon.MainController;
import common.abstractCommon.referenceHolderAC.ImageAnimationHolder;
import common.gui.TileImage;
import common.mainTools.LoggerMessages;

public interface TileImageFactory extends Serializable {
	public static final TileImageFactory DEFAULT_TILE_IMAGE_FACTORY = //
			(TileImageFactory & Serializable) // <- cast to avoid JAVA bugs
			TileImageFactory::defaultTileImageLoader;

	//

	public TileImage newEmptyTileImage(MainController main, String imageName);

	//

	public static TileImageFactory getOrDefault(TileImageFactory tif) {
		return tif != null ? tif : DEFAULT_TILE_IMAGE_FACTORY;
	}

	//

	public static TileImage defaultTileImageLoader(MainController m, String imageName) {
		TileImage ti;
		ImageAnimationHolder iah;
		LoaderGeneric l;
		if (m == null || imageName == null) return null;
		l = m.getLoader();
		ti = null;
		iah = null;
		try {
			iah = l.loadImage(imageName);
		} catch (IOException e) {
			LoggerMessages.loggerOrDefault(l.getLog()).logException(e);
		}
		if (iah != null) {
			ti = (iah instanceof TileImage) ? ((TileImage) iah) : new TileImage(iah);
		}
		return ti;
	}
}