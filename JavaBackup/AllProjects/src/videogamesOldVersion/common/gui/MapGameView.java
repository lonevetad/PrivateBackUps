package common.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

import javax.swing.JPanel;

import common.GameObjectInMap;
import common.MapGame;
import common.PainterMolm;
import common.PainterMolmPaintingOncePerRun;
import common.abstractCommon.AbstractMapGame;
import common.abstractCommon.GameModelGeneric;
import common.abstractCommon.MainController;
import common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager;
import common.abstractCommon.behaviouralObjectsAC.AbstractPainter;
import common.abstractCommon.behaviouralObjectsAC.AbstractPainterSimple;
import common.abstractCommon.behaviouralObjectsAC.ObjectGuiUpdatingOnFrame;
import common.abstractCommon.behaviouralObjectsAC.ThreadManagerTiny;
import common.abstractCommon.referenceHolderAC.FrameHolder;
import common.abstractCommon.shapedObject.AbstractObjectRectangleBoxed;
import common.mainTools.Comparators;
import common.mainTools.GraphicTools;
import common.mainTools.RunnableQueueReading;
import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.ObservableMolm.MolmEvent;
import common.mainTools.mOLM.abstractClassesMOLM.ObserverMolm;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification.SS_Rectangular;
import games.theRisingArmy.MainController_TheRisingArmy;
import tools.RedBlackTree;

/**
 * Only shows the map, that is usually the
 * {@link AbstractMatrixObjectLocationManager} View.<br>
 * Must be extended for Editor and for the real game
 * <p>
 * This class is and instance of {@link ObserverMolm} so it receives its
 * updates. For this purpose, this class store all {@link GameObjectInMapView}
 * added and removed from the map inside a {@link RedBlackTree}, obtainable
 * through {@link MapGameView#getObjectsInMap()}.
 * <p>
 * The painting mode could vary: see {@link MainGUI.DrawGameObjectMethod} for
 * the types.
 */
public abstract class MapGameView extends JPanel implements ObjectGuiUpdatingOnFrame, ObserverMolm, ThreadManagerTiny {
	private static final long serialVersionUID = 7847222734731L;

	// public MapGameView(FrameHolder frameHolder) {
	public MapGameView(MainGUI mainGui) {
		this(mainGui, true);
	}

	/**
	 * If the {@link MatrixObjectLocationManager} (a.k.a. MOLM) will hold few
	 * huge objects heavy and slow to be painted, then pass {@code true} to
	 * increase the visualizer's performance.<br>
	 * If the MOLM will hold several different instances of
	 * {@link ObjectWithID}, expecially if they are little and light to be
	 * drawn, then {@code false} is recommended. <br>
	 * By default, it's {@code true}.
	 */
	public MapGameView(MainGUI mainGui, boolean isNotRepaintingJetPaintedInstances) {
		this(mainGui, isNotRepaintingJetPaintedInstances ? PainterMolmPaintingOncePerRun.newInstance()
				: PainterMolm.newInstance());
	}

	public MapGameView(MainGUI mainGui, PainterMolm painter) {
		super();
		// super(view, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		// JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// this.frameHolder = frameHolder;
		// start stuffs decided at program-time and "final"
		repaintsOnScroll = false;
		paintingVisibleAreaOnly = true;
		// end stuffs decided at program-time and "final"
		//
		this.mainGui = mainGui;
		bufferToDrawOn = null;
		lastFrame = 0;
		setPainter(painter);
		// jpGlassView = view;
		// jpGlassView.mv = this;
		// setViewportView(view);
		canPaintMolms = true;
		// this.imageUpdater = new AnimationMOLMObjectsUpdater();
		objectsInMap = new RedBlackTree<>(Comparators.INTEGER_COMPARATOR);
		pixelEachMicropixelCustom = 1;

		//
		/*
		 * jpGlassView.addComponentListener(new ComponentAdapter() { public void
		 * componentResized(ComponentEvent e) {
		 * resizeBufferedImageToBePaintedOn(); } });
		 */
	}

	protected boolean repaintsOnScroll, paintingVisibleAreaOnly, canPaintMolms, isPixelEachMicropixelCustom;
	protected int lastFrame, pixelEachMicropixelCustom;
	// protected transient FrameHolder frameHolder;
	protected transient MainGUI mainGui;
	// protected AbstractMapGame mapGame;
	protected transient RedBlackTree<
			// ? extends
			Integer, GameObjectInMapView> objectsInMap;
	protected RunnableMapGameViewUpdater runnableMapGameViewUpdater;
	protected Thread threadMapGameViewUpdater;
	protected transient BufferedImage bufferToDrawOn;
	protected BiConsumer<Integer, GameObjectInMapView> drawerAllGOIMV;
	protected SS_Rectangular areaMolmToBeDrawn;
	//
	protected AbstractPainterSimple somethingToDoAfterPaint;
	@Deprecated
	protected PainterMolm painter;
	@Deprecated
	protected AnimationMOLMObjectsUpdater imageUpdater;
	@Deprecated
	protected AbstractPainterSimple paintAllMolm, paintMolmVisibleAreaOnly;

	//

	// TODO GETTER

	@Override
	public int getLastFrame() {
		return lastFrame;
	}

	public boolean isPixelEachMicropixelCustom() {
		return isPixelEachMicropixelCustom;
	}

	public int getPixelEachMicropixelCustom() {
		return pixelEachMicropixelCustom;
	}

	public FrameHolder getFrameHolder() {
		return mainGui;
	}

	public int getMicropixelEachTile() {
		return mainGui.getMicropixelEachTile();
	}

	@Deprecated
	public AnimationMOLMObjectsUpdater getImageUpdater() {
		return imageUpdater;
	}

	/**
	 * Returns the one stored on
	 * {@link MainController_TheRisingArmy#getGameModel()}'s
	 * {@link GameModelGeneric}.
	 */
	public AbstractMapGame getMapGame() {
		return mainGui.getMapGame();
	}

	public MainGUI getMainGui() {
		return mainGui;
	}

	/**
	 * The {@link GameObjectInMapView} are stored through
	 * {@link GameObjectInMap#getGameObjectID()}!
	 */
	public RedBlackTree<Integer, GameObjectInMapView> getObjectsInMap() {
		return objectsInMap;
	}

	@Deprecated
	public PainterMolm getPainter() {
		return painter;
	}

	protected AbstractMatrixObjectLocationManager[] getMolms() {
		AbstractMapGame mapGame;
		mapGame = getMapGame();
		return mapGame == null ? null : mapGame.getMolms();
	}

	/** Returns the panel where the {@link MapGame} will be drawn. */
	public JPanel getGlassView() {
		return this;// jpGlassView;
	}

	//

	// TODO SETTER

	public RunnableMapGameViewUpdater getRunnableMapGameViewUpdater() {
		return runnableMapGameViewUpdater;
	}

	public SS_Rectangular getAreaMolmToBeDrawn() {
		return areaMolmToBeDrawn;
	}

	@Override
	public MapGameView setLastFrame(int frame) {
		lastFrame = frame;
		return this;
	}

	public MapGameView setPixelEachMicropixelCustom(boolean isPixelEachMicropixelCustom) {
		this.isPixelEachMicropixelCustom = isPixelEachMicropixelCustom;
		return this;
	}

	public MapGameView setPixelEachMicropixelCustom(int pixelEachMicropixelCustom) {
		if (pixelEachMicropixelCustom > 0) {
			this.pixelEachMicropixelCustom = pixelEachMicropixelCustom;
			setPixelEachMicropixelCustom(true);
		}
		return this;
	}

	/*
	 * public MapGameView setFrameHolder(FrameHolder frameHolder) {
	 * this.frameHolder = frameHolder; return this; }
	 */

	public MapGameView setMainGui(MainGUI mainGui) {
		this.mainGui = mainGui;
		return this;
	}

	@Deprecated
	public MapGameView setImageUpdater(AnimationMOLMObjectsUpdater imageUpdater) {
		this.imageUpdater = imageUpdater;
		return this;
	}

	public MapGameView observeMapGame(AbstractMapGame mapGame) {
		// this.mapGame = mapGame;
		if (mapGame != null) {
			setSizeJPanelGlass(mainGui.scaleToRealPixel(mapGame.getWidthMicropixel()),
					mainGui.scaleToRealPixel(mapGame.getHeightMicropixel()));
			mapGame.addMolmObserver(this);
		}
		return this;
	}

	@Deprecated
	public MapGameView setPainter(PainterMolm painter) {
		if (painter == null) throw new IllegalArgumentException("PainterMolm cannot be null !");
		this.painter = painter;
		return this;
	}

	public MapGameView setSomethingToDoAfterPaint(AbstractPainterSimple somethingToDoAfterPaint) {
		this.somethingToDoAfterPaint = AbstractPainterSimple.getOrDefault(somethingToDoAfterPaint);
		return this;
	}

	//

	// TODO OTHER

	protected int getPixelEachMicropixel() {
		return (mainGui == null || isPixelEachMicropixelCustom) ? pixelEachMicropixelCustom
				: mainGui.getPixelEachMicropixel();
	}

	/** Return true if a new Runnable has been allocated. */
	@Override
	public boolean allocThreads() {
		if (runnableMapGameViewUpdater == null) {
			threadMapGameViewUpdater = new Thread(runnableMapGameViewUpdater = new RunnableMapGameViewUpdater(this));
			mainGui.addThreadToPool(threadMapGameViewUpdater);
			return true;
		}
		return false;
	}

	@Override
	public void destroyThreads() {
		runnableMapGameViewUpdater = null;
		threadMapGameViewUpdater = null;
	}

	@Override
	public void startThreads() {
		threadMapGameViewUpdater.start();
	}

	@Override
	public void pauseThreads() {
		runnableMapGameViewUpdater.suspendRunnable();
	}

	@Override
	public void resumeThreads() {
		runnableMapGameViewUpdater.resumeRunnable();
	}

	@Override
	public int getCurrentFrame() {
		return getFrameHolder().getCurrentFrame();
	}

	@Override
	public void setSize(Dimension d) {
		this.setSize(d.width, d.height);
	}

	@Override
	public void setSize(int width, int height) {
		setSizeJPanelGlass(width, height);
	}

	/**
	 * Edit 01/02/2018 : this class is a JPanel, so this method sets its size
	 */
	public void setSizeJPanelGlass(int width, int height) {
		if (mainGui.drawMethod == MainGUI.DrawGameObjectMethod.DoubleBufferWithBufferedImage) {
			// jpGlassView.
			super.setSize(width, height);
			if (bufferToDrawOn == null || bufferToDrawOn.getWidth() != width || bufferToDrawOn.getHeight() != height) {
				bufferToDrawOn = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			}
		}
	}

	@Override
	public void performAnimation(int milliseconds) {
		AbstractMatrixObjectLocationManager[] molms;
		AbstractMapGame mapGame;
		mapGame = getMapGame();
		if (mapGame != null) {
			switch (mainGui.drawMethod) {
			case BufferStrategy:
				if (!objectsInMap.isEmpty()) {
					Graphics g;
					BufferStrategy strategy;
					strategy = this.mainGui.getStrategy();
					// Prepare for rendering the next frame
					// ...
					// COPIED FROM DOCS, DON'T KNOW WHAT TO PUT ON THAT TREE
					// DOTS
					// Render single frame
					do {
						// The following loop ensures that the contents of the
						// drawing buffer
						// are consistent in case the underlying surface was
						// recreated
						do {
							// Get a new graphics context every time through the
							// loop
							// to make sure the strategy is validated
							g = strategy.getDrawGraphics();
							// Render to graphics
							// ...
							drawAllObjectGameOn(g, milliseconds, // jpGlassView.
									getWidth(), // jpGlassView.
									getHeight());
							g.dispose();
							// Repeat the rendering if the drawing buffer
							// contents
							// were restored
						} while (mainGui.getMainController().isWorking() && strategy.contentsRestored());
						// Display the buffer
						strategy.show();
						// Repeat the rendering if the drawing buffer was lost
					} while (mainGui.getMainController().isWorking() && strategy.contentsLost());
				}
				break;
			case DoubleBufferWithBufferedImage:
				/*
				 * Create a BufferedImage big as the screen section (JPanel,
				 * probably) to be drawn, draw on it all images got from
				 * GameObjectInMapView-s held by objectsInMap (only visible one,
				 * like performing a kind of culling), then draw that
				 * BufferedImage on this class Graphic instance
				 */
				if (!objectsInMap.isEmpty()) {
					drawAllObjectGameOn(this.bufferToDrawOn.getGraphics(), milliseconds, bufferToDrawOn.getWidth(),
							bufferToDrawOn.getHeight());
					// jpGlassView.
					getGraphics().drawImage(bufferToDrawOn, 0, 0, null);
					paintMapGrid(this); // jpGlassView);
				}
				break;
			case CycleOverAllMolmsMicropixel: {
				molms = mapGame.getMolms();
				if (molms != null && molms.length > 0) {
					imageUpdater.prepareForNewCycle(milliseconds);
					for (AbstractMatrixObjectLocationManager molm : molms)
						molm.forEach(imageUpdater);
				}
				//
				break;
			}
			default:
				break;
			}
			// jpGlassView.
			repaint();
		}
	}

	// TODO drawAllObjectGameOn
	protected void drawAllObjectGameOn(final Graphics g, int milliseconds, int widthGraphics, int heightGraphics) {
		final int p;
		p = getPixelEachMicropixel();
		if (drawerAllGOIMV == null) drawerAllGOIMV = (id, goimv) -> {
			ShapeSpecification ss;
			BufferedImage bi;
			g.setColor(Color.BLACK);
			g.clearRect(0, 0, widthGraphics, heightGraphics);
			if (bufferToDrawOn == null) return;
			ss = goimv.getGameObject().getShapeSpecification();
			if (ss instanceof AbstractObjectRectangleBoxed) {
				if (areaMolmToBeDrawn.intersects((AbstractObjectRectangleBoxed) ss)) {
					// adjust the image size
					goimv.scaleImagesToShapeSpecAndSizeSquare();
					// update any possible animation and get the image
					bi = goimv.getNextImage(milliseconds);

					// knowing how mainGui.scaleToRealPixel(x) works, simplify
					// the whole procedure
					if (bi != null)
						g.drawImage(bi, ss.getXLeftBottom() * p, ss.getYLeftBottom() * p, null);
					else
						AbstractPainter.DEFAULT_PAINTER_BLACK_RECTANGULAR.paintOn(g, ss.getXLeftBottom() * p,
								ss.getYLeftBottom() * p, p, p);
				}
			}
		};
		this.objectsInMap.forEach(drawerAllGOIMV);
	}

	protected void paintMapGrid() {
		paintMapGrid(this // jpGlassView
		);
	}

	protected void paintMapGrid(Component onToDraw) {
		paintMapGrid(onToDraw.getGraphics(), Math.abs(onToDraw.getX()), Math.abs(onToDraw.getY()), onToDraw.getWidth(),
				onToDraw.getHeight());
	}

	protected void paintMapGrid(BufferedImage onToDraw) {
		paintMapGrid(onToDraw.getGraphics(), 0, 0, onToDraw.getWidth(), onToDraw.getHeight());
	}

	protected void paintMapGrid(Graphics g, int xVisibleRectangle, int yVisibleRectangle, int widthVisibleRectangle,
			int heightVisibleRectangle) {
		int tileSizePixel, p;
		p = getPixelEachMicropixel();
		Color c;

		c = g.getColor();
		g.setColor(Color.BLACK);
		tileSizePixel = mainGui.getPixelEachMicropixel() * p;
		GraphicTools.paintGrid(g, xVisibleRectangle - (xVisibleRectangle % tileSizePixel),
				yVisibleRectangle - (yVisibleRectangle % tileSizePixel), widthVisibleRectangle, heightVisibleRectangle,
				tileSizePixel);
		// every each MainGenericController.MICROPIXEL_EACH_TILE line, make a
		// wider one
		tileSizePixel = MainController.MICROPIXEL_EACH_TILE * p;
		g.setColor(Color.GRAY);
		xVisibleRectangle = -(xVisibleRectangle % tileSizePixel);
		yVisibleRectangle = -(yVisibleRectangle % tileSizePixel);
		GraphicTools.paintGrid(g, xVisibleRectangle - 1, yVisibleRectangle - 1, widthVisibleRectangle,
				heightVisibleRectangle, tileSizePixel);
		GraphicTools.paintGrid(g, xVisibleRectangle + 1, yVisibleRectangle + 1, widthVisibleRectangle,
				heightVisibleRectangle, tileSizePixel);

		g.setColor(c);
	}

	protected void drawAllObjectGameOn(final BufferedImage biToPaintOn, int milliseconds, int widthGraphics,
			int heightGraphics) {
		drawAllObjectGameOn(biToPaintOn.getGraphics(), milliseconds, widthGraphics, heightGraphics);
	}

	@Override
	public void update(MolmEvent me, Object o, AbstractMOLMManager molmManager
	// , int indexMolm
	) {
		UpdateMGVInfo info;
		if (runnableMapGameViewUpdater != null) {
			info = new UpdateMGVInfo(me, o, molmManager// , indexMolm
			);
			runnableMapGameViewUpdater.addObjectToQueue(info);
		}
	}

	/** Override-designed */
	protected void manageUndefinedMolmEventOnUpdate(UpdateMGVInfo info) {
		// to do something if needed
	}

	protected void executeUpdatePending(UpdateMGVInfo info) {
		MolmEvent me;
		Object o;
		// AbstractMatrixObjectLocationManager[] molms;
		me = info.me;
		o = info.o;
		// molms = info.molms;
		switch (me) {
		case MapCleaned:
			objectsInMap.clear();
			break;
		case ObjectAdded:
			if (o != null && o instanceof GameObjectInMap) {
				GameObjectInMapView oimpw;
				oimpw = new GameObjectInMapView(this.mainGui, (GameObjectInMap) o);
				objectsInMap.add(oimpw.getGameObject().getGameObjectID(), oimpw);
			}
			break;
		case ObjectRemoved:
			if (o != null && o instanceof GameObjectInMap) objectsInMap.delete(((GameObjectInMap) o).getGameObjectID());
			break;
		case Undefined:
			manageUndefinedMolmEventOnUpdate(info);
			break;
		case MapResized:
			// AbstractMatrixObjectLocationManager molm;
			// molm = info.molms[0];
			// bufferToDrawOn = new BufferedImage(molm.getWidthMicropixel() *
			// mainGui.getPixelEachMicropixel(), molm.getHeightMicropixel() *
			// mainGui.getPixelEachMicropixel(), BufferedImage.TYPE_INT_ARGB);
			resizeBufferedImageToBePaintedOn();
			break;
		case MolmsInstancesChanged:
			objectsInMap.clear();
			resizeBufferedImageToBePaintedOn();
			break;
		default:
			break;
		}
	}

	protected void resizeBufferedImageToBePaintedOn() {
		int p;
		AbstractMOLMManager amm;
		Dimension d;
		p = getPixelEachMicropixel();
		amm = this.getMapGame();
		d = new Dimension(p * amm.getWidthMicropixel(), p * amm.getHeightMicropixel());
		// jpGlassView.
		setSize(d);
		// jpGlassView.
		setPreferredSize(d);
		// bufferToDrawOn = new BufferedImage(d.width, d.height,
		// BufferedImage.TYPE_INT_ARGB);
	}

	//

	// TODO FROM MOLM VISUALIZER

	//

	@Deprecated
	protected void paintMolms_CommonCode(Graphics g, AbstractPainterSimple differentCode) {
		int xx, yy, ss, ww, hh, xStartGrid, yStartGrid, ssHuge;
		AbstractMatrixObjectLocationManager[] am;
		Color c;
		AbstractMapGame mapGame;

		mapGame = getMapGame();
		if (
		/**
		 * avoid infinite-recursion and multiple threads painting molms
		 * asynchronously and in concurrence
		 */
		canPaintMolms //
				&& mapGame != null && g != null && painter != null && ((am = getMolms()) != null) && am.length > 0) {
			canPaintMolms = false;
			ss = getMicropixelEachTile();
			xx = Math.abs(// jpGlassView.
					getX());
			yy = Math.abs(// jpGlassView.
					getY());
			ww = // jpGlassView.
					getWidth();
			hh = // jpGlassView.
					getHeight();

			c = g.getColor();
			g.setColor(Color.LIGHT_GRAY);
			painter.setGraphics(g);
			painter.prepareForPaintingRun();

			// paint
			differentCode.paintOn(g);

			GraphicTools.paintGrid(g, xx - (xx % ss), yy - (yy % ss), ww, hh, ss);
			// every each MainGenericController.MICROPIXEL_EACH_TILE line, make
			// a wider one
			ssHuge = MainController.MICROPIXEL_EACH_TILE;
			g.setColor(Color.GRAY);
			xStartGrid = xx - (xx % ssHuge);
			yStartGrid = yy - (yy % ssHuge);
			GraphicTools.paintGrid(g, xStartGrid - 1, yStartGrid - 1, ww, hh, ssHuge);
			GraphicTools.paintGrid(g, xStartGrid + 1, yStartGrid + 1, ww, hh, ssHuge);

			g.setColor(c);

			somethingToDoAfterPaint.paintOn(g);
			// re-enablePainting
			canPaintMolms = true;
		}
	}

	@Deprecated
	public void paintMolms(Graphics g) {
		if (paintingVisibleAreaOnly)
			paintMolms_OnlyVisibleArea(g);
		else
			paintMolms_AllMolm(g);
	}

	@Deprecated
	public void paintMolms_AllMolm(Graphics g) {
		paintMolms_CommonCode(g, paintAllMolm);
	}

	@Deprecated
	public void paintMolms_OnlyVisibleArea(Graphics g) {
		paintMolms_CommonCode(g, paintMolmVisibleAreaOnly);
	}

	/*
	 * void addRepainterOnScroll() { AdjustmentListener al; al = e -> { if
	 * (repaintsOnScroll) jpGlassView.repaint(); };
	 * this.getHorizontalScrollBar().addAdjustmentListener(al);
	 * this.getVerticalScrollBar().addAdjustmentListener(al); }
	 */

	@Deprecated
	protected void setMolmRepainterUncommonFunction() {
		paintAllMolm = g -> paintMolmAll(g);
		paintMolmVisibleAreaOnly = g -> paintMolmVisibleAreaOnly(g);
	}

	@Deprecated
	protected void paintMolmAll(Graphics g) {
		AbstractMatrixObjectLocationManager[] ms;
		ms = getMolms();
		if (ms != null && ms.length > 0) {
			for (AbstractMatrixObjectLocationManager m : ms)
				m.forEach(painter);
		}
	}

	@Deprecated
	protected void paintMolmVisibleAreaOnly(Graphics g) {
		AbstractMatrixObjectLocationManager[] ms;
		int xx, yy, ss, ww, hh;
		ms = getMolms();
		if (ms != null && ms.length > 0) {
			xx = Math.abs(// jpGlassView.
					getX());
			yy = Math.abs(// jpGlassView.
					getY());
			ss = getMicropixelEachTile();
			/*
			 * ww = jpMolmView.getWidth(); hh = jpMolmView.getHeight();
			 */
			ww = getWidth();
			hh = getHeight();
			this.areaMolmToBeDrawn.setLeftBottomCorner(xx / ss, yy / ss);
			// integer division
			// this.rectangleToBeDrawn.setWidth((ww + (ss - 1)) / ss);
			// this.rectangleToBeDrawn.setHeight((hh + (ss - 1)) / ss);
			this.areaMolmToBeDrawn.setWidth(ww / ss);
			this.areaMolmToBeDrawn.setHeight(hh / ss);
			for (AbstractMatrixObjectLocationManager m : ms)
				m.runOnShape(areaMolmToBeDrawn, painter);
		}
	}

	// public void addMouseListener(MouseListener l)
	// {jpGlassView.addMouseListener(l);}

	// public void addMouseMotionListener(MouseMotionListener l)
	// {jpGlassView.addMouseMotionListener(l);}

	//

	// TODO CLASS

	public static class UpdateMGVInfo {
		// int indexMolm;
		MolmEvent me;
		Object o;
		AbstractMOLMManager molmManager;

		public UpdateMGVInfo(MolmEvent me, Object o, AbstractMOLMManager molmManager
		// , int indexMolm
		) {
			super();
			// this.indexMolm = indexMolm;
			this.me = me;
			this.o = o;
			this.molmManager = molmManager;
		}
	}

	public static class RunnableMapGameViewUpdater extends RunnableQueueReading<UpdateMGVInfo> {
		public RunnableMapGameViewUpdater(MapGameView mgv) {
			super(mgv.mainGui);
			this.mgv = mgv;
		}

		long timeRepaintStart;
		MapGameView mgv;

		@Override
		public void doOnEmptyingQueue() {
			long delta;
			delta = (System.currentTimeMillis() - timeRepaintStart) - 15;
			if (delta > 0) try {
				Thread.sleep(delta);
			} catch (InterruptedException e) {
				log(e);
			}
		}

		@Override
		public void manage(UpdateMGVInfo object) {
			timeRepaintStart = System.currentTimeMillis();
			mgv.executeUpdatePending(object);
		}

		@Override
		public boolean canCycle() {
			MainController main;
			return (main = mgv.mainGui.getMain()) == null || main.isWorking();
		}
	}

	protected static class JPanelMolmView extends JPanel {
		private static final long serialVersionUID = 160970963343376L;

		public JPanelMolmView() {
			this(null);
		}

		public JPanelMolmView(MapGameView mv) {
			this(mv, null);
		}

		public JPanelMolmView(MapGameView mv, LayoutManager layout) {
			super(layout);
			this.mv = mv;
		}

		MapGameView mv;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			paintMolms(g);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			// super.paintComponents(g);
			paintMolms(g);
		}

		public void paintMolms(Graphics g) {
			// mv.paintMolms(g);
			if (mv.mainGui.drawMethod == MainGUI.DrawGameObjectMethod.DoubleBufferWithBufferedImage
					&& mv.bufferToDrawOn != null) {
				g.drawImage(mv.bufferToDrawOn, 0, 0, null);
			}
		}

	}
}