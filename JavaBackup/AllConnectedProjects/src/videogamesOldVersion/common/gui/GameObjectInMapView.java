package common.gui;

import java.awt.image.BufferedImage;

import common.GameObjectInMap;
import common.abstractCommon.behaviouralObjectsAC.ObjectGuiUpdatingOnFrame;
import common.abstractCommon.referenceHolderAC.FrameHolder;
import common.abstractCommon.referenceHolderAC.FrameHolderDelegating;
import common.abstractCommon.referenceHolderAC.ImageAnimationHolder;
import common.abstractCommon.shapedObject.AbstractObjectRectangleBoxed;
import common.mainTools.AnimatedImage;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

public class GameObjectInMapView implements ObjectGuiUpdatingOnFrame, FrameHolderDelegating, ImageAnimationHolder {
	private static final long serialVersionUID = -501804849132023L;

	public GameObjectInMapView(MainGUI mainGui, GameObjectInMap gameObject) {
		this(mainGui, gameObject, mainGui.getAllGameObjectTileImage()
				.getTileImageByImageName(mainGui.getMainController(), gameObject.getName()));
	}

	public GameObjectInMapView(MainGUI mainGui, GameObjectInMap gameObject, TileImage tileImage) {
		super();
		this.mainGui = mainGui;
		this.setGameObject(gameObject);
		setFrameHolder(mainGui);
		currentFrame = 0;
		setFrom(tileImage);
	}

	int currentFrame;
	// FrameHolder frameHolder;
	GameObjectInMap gameObject; // GameObjectInMap
	MainGUI mainGui;
	// protected transient BufferedImage image;
	// protected transient AnimatedImage animatedImage;
	transient TileImage tileImage;

	//

	// TODO GETTER

	@Override
	public int getCurrentFrame() {
		return currentFrame;
	}

	public GameObjectInMap getGameObject() {
		return gameObject;
	}

	@Override
	public FrameHolder getFrameHolder() {
		return mainGui;
	}

	public MainGUI getMainGui() {
		return mainGui;
	}

	public TileImage getTileImage() {
		return tileImage;
	}

	@Override
	public BufferedImage getImageOriginal() {
		return tileImage != null ? tileImage.getImageOriginal() : null;
	}

	@Override
	public AnimatedImage getAnimatedImage() {
		return tileImage != null ? tileImage.getAnimatedImage() : null;
	}

	@Override
	public int getLastFrame() {
		return mainGui.getLastFrame();
	}

	@Override
	public Integer getID() {
		return gameObject.getIdEnumElement();
	}

	@Override
	public String getImageName() {
		return gameObject.getName();
	}

	//

	// TODO SETTER

	public GameObjectInMapView setGameObject(GameObjectInMap gameObject) {
		this.gameObject = gameObject;
		return this;
	}

	public GameObjectInMapView setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
		return this;
	}

	@Override
	public GameObjectInMapView setFrameHolder(FrameHolder frameHolder) {
		if (frameHolder != null && frameHolder instanceof MainGUI) this.mainGui = (MainGUI) frameHolder;
		return this;
	}

	public GameObjectInMapView setMainGui(MainGUI mainGui) {
		this.mainGui = mainGui;
		return this;
	}

	public GameObjectInMapView setTileImage(TileImage tileImage) {
		return setFrom(tileImage);
	}

	@Override
	public GameObjectInMapView setAnimatedImage(AnimatedImage animatedImage) {
		if (tileImage != null) tileImage.setAnimatedImage(animatedImage);
		return this;
	}

	@Override
	public ImageAnimationHolder setID(Integer iD) {
		gameObject.setIdEnumElement(iD);
		return this;
	}

	@Override
	public ImageAnimationHolder setImageName(String imageName) {
		gameObject.setName(imageName);
		return this;
	}

	//

	// TODO OTHER

	@Override
	public void performAnimation(int milliseconds) {
		AnimatedImage ai;
		ai = getAnimatedImage();
		if (ai != null) {
			tileImage.passTime(milliseconds);
			// if (ai.passTime(milliseconds)) this.setImage(ai.getImageResized());
		}
	}

	public GameObjectInMapView setFrom(TileImage tileImage) {
		if (tileImage != null) {
			/*
			 * this.setID(tileImage.getID()); this.setAnimatedImage(tileImage.getAnimatedImage());
			 * this.setImage(tileImage.getImage()); this.setImageName(tileImage.getImageName());
			 */
			this.tileImage = tileImage;
			if (gameObject != null) {
				if (gameObject.getIdEnumElement() == null) gameObject.setIdEnumElement(tileImage.getID());
				if (gameObject.getName() == null) gameObject.setName(tileImage.getImageName());
			}
		}
		return this;
	}

	// delegates

	@Override
	public BufferedImage getNextImage(int millis) {
		return tileImage == null ? null : tileImage.getNextImage(millis);
	}

	@Override
	public BufferedImage getImageResized() {
		return tileImage.getImageResized();
	}

	@Override
	public int getWidth() {
		return tileImage.getWidth();
	}

	@Override
	public int getHeight() {
		return tileImage.getHeight();
	}

	@Override
	public ImageAnimationHolder setImageResized(BufferedImage image) {
		tileImage.setImageResized(image);
		return this;
	}

	@Override
	public ImageAnimationHolder setImageOriginal(BufferedImage image) {
		tileImage.setImageOriginal(image);
		return this;
	}

	public TileImage scaleImagesToShapeSpecAndSizeSquare() {
		ShapeSpecification ss;
		AbstractObjectRectangleBoxed orb;
		int w, h;
		if (tileImage != null) {
			ss = this.gameObject.getShapeSpecification();
			if (ss != null && ss instanceof AbstractObjectRectangleBoxed) {
				orb = (AbstractObjectRectangleBoxed) ss;
				w = this.mainGui.scaleToRealPixel(orb.getWidth());
				h = this.mainGui.scaleToRealPixel(orb.getHeight());
				scaleImages(w, h);
			}
		}
		return tileImage;
	}

	public TileImage scaleImages(int width, int height) {
		return tileImage == null ? null : tileImage.scaleImages(width, height);
	}

	@Override
	public boolean passTime(long millisPassed) {
		return tileImage.passTime(millisPassed);
	}

}