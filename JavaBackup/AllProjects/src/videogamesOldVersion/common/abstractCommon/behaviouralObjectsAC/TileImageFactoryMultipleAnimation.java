package common.abstractCommon.behaviouralObjectsAC;

import java.io.IOException;

import common.abstractCommon.AnimationName;
import common.abstractCommon.LoaderGeneric;
import common.abstractCommon.MainController;
import common.abstractCommon.referenceHolderAC.ImageAnimationHolder;
import common.gui.TileImage;
import common.gui.TileImageMultipleAnimations;
import common.mainTools.LoggerMessages;

public class TileImageFactoryMultipleAnimation implements TileImageFactory {
	private static final long serialVersionUID = 1L;

	public TileImageFactoryMultipleAnimation(AnimationName[] animationsNames) {
		this.animationsNames = animationsNames;
	}

	AnimationName[] animationsNames;

	@Override
	public TileImage newEmptyTileImage(MainController m, String imageName) {
		TileImageMultipleAnimations tima;
		TileImage tiTemp;
		ImageAnimationHolder iah;
		LoaderGeneric l;
		String imageAnimationName;
		if (m == null || imageName == null || animationsNames == null || animationsNames.length == 0) return null;
		l = m.getLoader();
		tima = new TileImageMultipleAnimations();
		// set the image's name
		tima.setImageName(imageName);
		iah = null;
		try {
			// for each image/animation, load it
			for (AnimationName in : animationsNames) {
				// compute the name of the image/animation of this image, then load
				iah = l.loadImage(imageAnimationName = in.fileNameImage(tima));
				// add to the TIMA
				if (iah != null) {
					tiTemp = (iah instanceof TileImage) ? ((TileImage) iah) : new TileImage(iah);
					tima.addTileImageAnimation(imageAnimationName, tiTemp);
				}
			}
		} catch (IOException e) {
			LoggerMessages.loggerOrDefault(l.getLog()).logException(e);
		}
		return null;
	}

}
