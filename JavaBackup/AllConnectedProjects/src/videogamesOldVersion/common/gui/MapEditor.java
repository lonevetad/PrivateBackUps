package common.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

import common.EnumGameObjectTileImageCollection;
import common.PainterMolm;
import common.abstractCommon.AbstractEnumElementGOTI;
import common.abstractCommon.AbstractEnumGOTI;
import common.abstractCommon.AbstractMapGame;
import common.abstractCommon.GameMechanismType;
import common.abstractCommon.LoaderGeneric;
import common.abstractCommon.MainController;
import common.abstractCommon.behaviouralObjectsAC.GraphComponentSizeAdaptive;
import common.abstractCommon.behaviouralObjectsAC.MyComparator;
import common.abstractCommon.behaviouralObjectsAC.ObjectGuiUpdatingOnTime;
import common.abstractCommon.behaviouralObjectsAC.ThreadManagerTiny;
import common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import common.abstractCommon.referenceHolderAC.MainHolder;
import common.gui.MapEditor.LBV_EE_TMSI.JPIV_EE_TMSI;
import common.mainTools.AnimatedImage;
import common.mainTools.Comparators;
import common.mainTools.JListModelTreeMap;
import common.mainTools.ListModel;
import common.mainTools.LoggerMessages;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager.CollectedObject;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

/**
 * TODO: list
 * <ul>
 * <li>permit copy, paste and cut from selection area</li>
 * <li>add right controls to set molms' sizes and selected objects' rotation
 * angle.. add also a JSlider with {ALL, SolidOnly, NonSolidOnly} to choose what
 * molm(s) modificatiyngon changing selected objects' rotation (eh??).</li>
 * <li>TODO ADD A BUTTON TO CREATE A NEW MAP_GAME {@link #setNewMapGame()}.</li>
 * </ul>
 * <p>
 * 10/12/2017 : va riprogettata la grafica : questo pannellone deve permettere
 * lo switch tra due pannelli :
 * <ul>
 * <li>carica o nuova</li>
 * <li>il vero editor, in cui si imposta tutto</li>
 * </ul>
 */
public abstract class MapEditor extends JPanel implements GraphComponentSizeAdaptive, MainHolder, LoggerMessagesHolder,
		ObjectGuiUpdatingOnTime, ThreadManagerTiny {
	private static final long serialVersionUID = 519801266519L;

	public static enum MapEditorSections {
		LoadOrNew, TheRealEditor;
	}

	public static final int MILLIS_EACH_REPAINT = 50, SIZE_JL_LIST_SINGLE_ENUM_TILEMAP = 35,
			EDITOR_COLUMNS_GRIDBAGLAYOUT = 4, EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT = 8, EDITOR_ROWS_GRIDBAGLAYOUT = 10,
			EDITOR_LAST_COLUMN_START = EDITOR_COLUMNS_GRIDBAGLAYOUT * (EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT - 1);
	// minSizeLateralSections
	protected static final Dimension MIN_SIZE_LATERAL_SECTIONS = new Dimension(200, 400);
	protected static final Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.BLACK, 1);
	public static final ListBoxView.DescriptionExtractor //
	listTilesSingleEnum_DescriptionExtractor = o -> ((AbstractEnumElementGOTI) o).getName()//
			, listTileEnums_DescriptionExtractor = o -> ((AbstractEnumGOTI) o).getNameEnum();
	public static final MyComparator<AbstractEnumGOTI> COMPARATOR_AbstractEnumGameObjectTileImage = (e1, e2) -> {
		if (e1 == e2) return 0;
		if (e1 == null) return -1;
		if (e2 == null) return 1;
		return Comparators.STRING_COMPARATOR.compare(e1.getNameEnum(), e2.getNameEnum());
	};
	public static final MyComparator<AbstractEnumElementGOTI> COMPARATOR_AbstractEnumElementGameObjectTileImage = (e1,
			e2) -> {
		if (e1 == e2) return 0;
		if (e1 == null) return -1;
		if (e2 == null) return 1;
		return Comparators.STRING_COMPARATOR.compare(e1.getName(), e2.getName());
	};

	public static enum AreaMeaningOnMolmVisualizer {
		Nothing, Selection(Color.RED), Paste(Color.BLUE);
		final Color colorArea;

		AreaMeaningOnMolmVisualizer() {
			this(null);
		}

		AreaMeaningOnMolmVisualizer(Color c) {
			this.colorArea = c;
		}

		public static Color getColorAssociated(AreaMeaningOnMolmVisualizer amomv) {
			return amomv == null ? null : amomv.colorArea;
		}
	}

	/*
	 * public MapEditor() { super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	 * JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	 * setViewportView(componentGeneral = getNewJPanelGeneral()); }
	 */
	public MapEditor() {
		this(new CardLayout());
	}

	protected MapEditor(LayoutManager l) {
		super(l);
		if (l instanceof CardLayout) this.cardLayout = (CardLayout) l;
		// this(new JPanel())
		// }
		// public MapEditor(Component view) {
		// NON gui init
		isAlive = true;
		/*
		 * molmNonSolid = MatrixObjectLocationManager.newDefaultInstance(
		 * INITIAL_MOLM_WIDTH_MICROPIXEL, INITIAL_MOLM_HEIGHT_MICROPIXEL);
		 * molmSolid = MatrixObjectLocationManager.newDefaultInstance(
		 * INITIAL_MOLM_WIDTH_MICROPIXEL, INITIAL_MOLM_HEIGHT_MICROPIXEL); molms
		 * = new AbstractMatrixObjectLocationManager[] { molmNonSolid, molmSolid
		 * };
		 */
		setLogSafe(null);
		// gui
		// initGUI();
	}

	//

	// fields
	protected boolean isAlive;
	// gui
	// protected boolean canPaintRectangleSelection;
	protected int counterMillisRepaint, xPixelRealAreaOnMolmStart, yPixelRealAreaOnMolmStart, xPixelRealAreaOnMolmEnd,
			yPixelRealAreaOnMolmEnd;
	protected CardLayout cardLayout;
	protected JPanel jpLoadOrNew_Section, jpTheRealEditor_Section//
	/*
	 * , jpListTilesSection, jpRightControlSection, jpLeftControlSection,
	 * jpLCS_ButtonsBottom, jpRCS_ListMapsNewSaveDelete,
	 * jpRCS_ListMapsNewSaveDelete_Buttons,
	 * jpRCS_ListMapsNewSaveDelete_TopSectionOverList,
	 * jpRCS_ListMapsNewSaveDelete_MapName, jpRCS_Controls,
	 * jpRCS_Controls_SizeMolm, jpRCS_Controls_TileManagerSection
	 */;
	protected JButton jbAllBackToStart, jbCloseEditor, jbClearMolms, jbNewMap, jbSaveMap, jbDeleteMap,
			jbLoadSelectedMap, jbClearSelectedArea, jbCopySelectedArea, jbResizeMolms;
	protected JTextField jtfMapName;
	protected JScrollPane jspMapViewer_Preview, jspMapViewer_Editor, jspListMapJetCreated;
	protected JSpinner jsAngleTile, jsWidthMolm, jsHeightMolm;
	// , jsXTile, jsYTile;
	protected JComboBox<? extends GameMechanismType> jcbGameMechanismTypes;
	/** A menu shown on performing right click over the map */
	protected JList<String> listMenuOnRightClickOnMap;
	protected DefaultListModel<String> modelMenuOnRightClickOnMap;
	// protected MolmVisualizer molmVisualizer;
	// protected ListBoxView<AbstractEnumGOTI> listTileEnums;
	// protected ListBoxView<AbstractEnumElementGOTI> listTilesSingleEnum;
	// protected ListBoxView<String> listMapJetCreated;
	protected JList<AbstractEnumGOTI> listTileEnums;
	protected JListModelTreeMap<AbstractEnumGOTI, AbstractEnumGOTI> modelTileEnums;
	protected JList<AbstractEnumElementGOTI> listTileSingleEnum;
	protected JListModelTreeMap<AbstractEnumElementGOTI, AbstractEnumElementGOTI> modelTileSingleEnum;
	protected // ArrayList<TileImage>
	TileImage[] enumElement_sTileImagesCached;
	protected JList<String> listMapJetCreated;
	protected ListModel<String> modelMapJetCreated;
	// protected ListBoxView<AbstractMapGameGeneric> listMapJetCreated;
	// protected ListBoxView<AbstractMementoMapGame> listMapJetCreated;

	protected AnimationListboxTileUpdater animationUpdaterOnListbox;
	protected MouseAdapter mouseAdapterMolmVisualizer;
	protected AreaMeaningOnMolmVisualizer areaMeaningOnMolmVisualizer;
	protected MapEditorSections sectionShown;
	/*
	 * e' compito del mapEditor far sospendere e ripartire questo affare
	 */
	// protected Runnable_ImageUpdater runnableImageUpdater;
	// other
	// protected MainController main;
	protected MainGUI mainGUI;
	protected CollectedObject tilesSolid, tilesNonSolid;
	protected ShapeSpecification.SS_Rectangular shapeSelection;
	protected MapGameView mapGameView;
	// AbstractObjectRectangleBoxed areaSelection;
	// AbstractObjectRectangleBoxed shapeSelection;
	//

	// TODO GETTER

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public MainController getMain() {
		return mainGUI.getMain();
	}

	@Override
	public LoggerMessages getLog() {
		// if (mainGUI == null) return LoggerMessages.LOGGER_DEFAULT;
		return mainGUI.log;
	}

	public int getPixelEachTile() {
		return mainGUI.getPixelEachTile();
	}

	public int getPixelEachMicropixel() {
		return mainGUI.getPixelEachMicropixel();
	}

	public AbstractMapGame getMapGame() {
		return mainGUI.getMapGame();
	}

	public EnumGameObjectTileImageCollection getAllGameObjectTileImage() {
		return mainGUI.getAllGameObjectTileImage();
	}

	/** The graphic component holding everything inside this map editor */
	/*
	 * public Component getComponentGeneral() { return componentGeneral; }
	 */

	//

	// TODO SETTER

	/*
	 * public void setJpGeneral(JPanel jpGenera) { this.jpGeneral = jpGenera; }
	 */
	@Override
	public MapEditor setMain(MainController main) {
		/*
		 * this.main = main; this.setMainGUI(main.getMainGenericGUI());
		 * setLogSafe(main.getLog());
		 */
		return this;
	}

	@Override
	public LoggerMessagesHolder setLog(LoggerMessages log) {
		// log belongs to mainGUI
		return this;
	}

	public MapEditor setMainGUI(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
		// if (mainGUI != null)
		// mainGUI.getMapGameView().setFrameHolder(mainGUI);
		return this;
	}

	public MainGUI setMapGame(AbstractMapGame mapGame) {
		return mainGUI.setMapGame(mapGame);
	}

	//

	// TODO OHTER

	public void initGUI() {
		allocPrincipalComponents();
		composeGuiComponents();
		addBehaviourToComponents();

		// then the rest
		xPixelRealAreaOnMolmStart = yPixelRealAreaOnMolmStart = xPixelRealAreaOnMolmEnd = yPixelRealAreaOnMolmEnd = 0;
		mouseAdapterMolmVisualizer = getMouseListenersOnMolmsVisualizer();
		animationUpdaterOnListbox = new AnimationListboxTileUpdater(this);
		// animationMOLMTileUpdater = new AnimationMOLMTileUpdater(this);
		counterMillisRepaint = 0;
		areaMeaningOnMolmVisualizer = null;
		shapeSelection = ShapeSpecification.newRectangle(true, 2, 2, 4, 4, 0);
	}

	/** Create all instances of gui components */
	protected void allocPrincipalComponents() {
		// first, the hug-gest question
		jpLoadOrNew_Section = new JPanel(null);
		jpTheRealEditor_Section = new JPanel(null);
		// then all sub parts

		/*
		 * cell renders are depending on how the GUI will be built (BorderLayout
		 * or GridBagLayout, ecc) so this question is shifted
		 */
		modelTileEnums = JListModelTreeMap.newInstance(COMPARATOR_AbstractEnumGameObjectTileImage);
		listTileEnums = new JList<AbstractEnumGOTI>(modelTileEnums);
		listTileEnums.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modelTileSingleEnum = JListModelTreeMap.newInstance(COMPARATOR_AbstractEnumElementGameObjectTileImage);
		listTileSingleEnum = new JList<AbstractEnumElementGOTI>(modelTileSingleEnum);
		listTileSingleEnum.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modelMapJetCreated = new ListModel<>();
		listMapJetCreated = new JList<String>(modelMapJetCreated);
		listMapJetCreated.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jspListMapJetCreated = new JScrollPane(listMapJetCreated);
		jspListMapJetCreated.setViewportView(listMapJetCreated);

		jtfMapName = new JTextField();
		jsWidthMolm = new JSpinner(new SpinnerNumberModel(0, 0, 1024, 1));
		jsHeightMolm = new JSpinner(new SpinnerNumberModel(0, 0, 1024, 1));
		// jsXTile = new JSpinner(new SpinnerNumberModel(0, 0, 1024, 1));
		// jsYTile = new JSpinner(new SpinnerNumberModel(0, 0, 1024, 1));
		jsAngleTile = new JSpinner(new SpinnerNumberModel(0, 0, 360.0, 0.125));

		modelMenuOnRightClickOnMap = new DefaultListModel<>();
		listMenuOnRightClickOnMap = new JList<>(modelMenuOnRightClickOnMap);
		mapGameView = newMapGameView_Editor();

		jbAllBackToStart = new JButton("Exit to start");
		jbCloseEditor = new JButton("Exit");
		jbClearMolms = new JButton("Clear map");
		jbNewMap = new JButton("New Map");
		jbSaveMap = new JButton("Save");
		jbDeleteMap = new JButton("Delete selected");
		jbLoadSelectedMap = new JButton("Load");
		jbClearSelectedArea = new JButton("Cancel selected area");
		jbCopySelectedArea = new JButton("Copy selected area");
		jbResizeMolms = new JButton("Resize");
		jcbGameMechanismTypes = new JComboBox<>(getGameMechanismTypes());
	}

	/**
	 * Assemble the gui putting the yet instanced gui compents all togethers in
	 * the preferred way, through some layout rules
	 */
	protected void composeGuiComponents() {
		GridBagConstraints c;
		JLabel jlTemp;
		JList<? extends Object>[] jll;
		JButton[] buttonsOnRow;

		int i;
		c = new GridBagConstraints();
		// first, the hug-gest question
		this.add(jpLoadOrNew_Section, MapEditorSections.LoadOrNew.name());
		this.add(jpTheRealEditor_Section, MapEditorSections.TheRealEditor.name());
		showSection(MapEditorSections.LoadOrNew);
		// then all sub parts

		listTileEnums.setCellRenderer(new DefaultListCellRenderer());
		listTileSingleEnum.setCellRenderer(new CellRender_TileSingleEnum(this));
		listMapJetCreated.setCellRenderer(new DefaultListCellRenderer());

		// LoadOrNew
		/*
		 * la sezione list-existing-map-to-modify ha 3 bottoni : modifica, nuova
		 * e elimina. Nuova ripetera' il codice del "nuova" del menu iniziale,
		 * mandandomi alla sezione
		 */
		/**
		 * E LA CASELLA DI TESTO PER IL NOME DELLA NUOVA MAPPA? e
		 * jcbGameMechanismTypes?<br>
		 * _____________________________________________________<br>
		 * | . . . . . . . . . . . . . | . . . . . . . . . . . . |<br>
		 * | | .btnBack. | .btnLoad. | | |btnNewMap| btnDelete | |<br>
		 * | ________________________| | _______________________ |<br>
		 * | |jtfMapName|gameMechType| | . . . . . . . . . . | |<br>
		 * | |__________|____________| | | . . . . . . . . . . | |<br>
		 * | | . . . . . . . . . .|/\| | | . . . . . . . . . . | |<br>
		 * | | such a big list of |--| | | . preview panel : . | |<br>
		 * | | yet existing maps. |__| | | . MapGameView micro | |<br>
		 * | |____________________|\/| | |_____________________| |<br>
		 * |___________________________|_________________________|
		 */
		jpLoadOrNew_Section.setLayout(new GridBagLayout());

		buttonsOnRow = new JButton[] { jbLoadSelectedMap, jbNewMap, jbDeleteMap, jbAllBackToStart };
		i = -1;
		while (++i < buttonsOnRow.length) {
			c.fill = GridBagConstraints.BOTH;
			c.gridx = i << 1; // eh eh
			c.gridy = 0;
			c.weightx = c.gridwidth = 2;
			c.weighty = c.gridheight = 1;
			// c.ipadx = c.ipady = 10;
			jpLoadOrNew_Section.add(buttonsOnRow[i], c);
		}
		buttonsOnRow = null;

		//

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = c.ipady = 0;
		c.weightx = c.gridwidth = 2;
		jtfMapName.setText("Map name");
		jpLoadOrNew_Section.add(jtfMapName, c);
		c.gridx = 2;
		// c.weightx = c.gridwidth = 1;
		// jlTemp = new JLabel("Game mechanism");
		// jlTemp.setHorizontalAlignment(SwingConstants.CENTER);
		// jpLoadOrNew_Section.add(jlTemp, c);
		// c.gridx = 3;
		jpLoadOrNew_Section.add(jcbGameMechanismTypes, c);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = c.gridwidth = 4;
		c.weighty = c.gridheight = 6;
		// c.ipadx = c.ipady = 10;
		jpLoadOrNew_Section.add(jspListMapJetCreated, c);

		jspMapViewer_Preview = new JScrollPane();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 4;
		c.gridy = 1;
		c.weightx = c.gridwidth = 4;
		c.weighty = c.gridheight = 7;
		// c.ipadx = c.ipady = 10;
		jpLoadOrNew_Section.add(jspMapViewer_Preview, c);

		//

		// TheRealEditor

		/**
		 * _______1________|____2____|_____3____|____4______<br>
		 * | |btnCloseEdit|| . . . . . . . . |/\| | . . . . |<br>
		 * | ------------- | . . . . . . . . |--| | . . . . |<br>
		 * | | . all. |/\| | . . . . . . . . | .| | . . . . |<br>
		 * | | enums. |--| | . . . . . . . . | .| | buttonsCache |<br>
		 * | |of GOIMP|__| | . MapGameView . | .| | . . . . |<br>
		 * | |________|\/| | . . . . . . . . |[]| |_________|<br>
		 * | ____________| | . . . . . . . . | .| | . . . . |<br>
		 * | | single |/\| | . . . . . . . . |__| |controls |<br>
		 * | | enum . |--| | . . . . . . . . |\/| | . . . . |<br>
		 * | |elements|__| |_________________|__| | _______ |<br>
		 * | |________|\/| |<|_____[]______|>|__| | . log ? |<br>
		 * |________________________________________________|
		 */
		// rows: 1 btn, 4 all enum, 5 single enum
		// rows: 4 buttonsCache, 3 log, 3 controls

		// left section

		jpTheRealEditor_Section.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = c.gridwidth = EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT;
		c.weighty = c.gridheight = 1;
		c.ipadx = c.ipady = 10;
		jpTheRealEditor_Section.add(jbCloseEditor, c);

		jll = new JList<?>[] { listTileEnums, listTileSingleEnum };
		/*
		 * enums deve essere alto 1 riga in meno dell'altro
		 */
		i = -1;
		while (++i < jll.length) {
			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy += c.gridheight;
			// (i * EDITOR_ROWS_GRIDBAGLAYOUT) + (1 - i);
			c.weightx = c.gridwidth = EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT;
			c.weighty = c.gridheight = (EDITOR_ROWS_GRIDBAGLAYOUT >> 2) + i;
			c.ipadx = c.ipady = 10;
			jpTheRealEditor_Section.add(jll[i], c);
		}
		jll = null;

		// map view

		jspMapViewer_Editor = new JScrollPane();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT;
		c.gridy = 0;
		c.weightx = c.gridwidth = EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT << 1;
		c.weighty = c.gridheight = EDITOR_ROWS_GRIDBAGLAYOUT;
		c.ipadx = c.ipady = 50;
		jpTheRealEditor_Section.add(jspMapViewer_Editor, c);

		//

		// inizo parte a destra del map view : pulsanti e controlli

		c.fill = GridBagConstraints.BOTH;
		c.gridx = EDITOR_LAST_COLUMN_START;
		c.gridy = 0;
		c.weightx = c.gridwidth = (EDITOR_ROWS_GRIDBAGLAYOUT >> 1) - 1;// 2
		c.weighty = c.gridheight = EDITOR_ROWS_GRIDBAGLAYOUT >> 2;
		c.ipadx = c.ipady = 10;
		jpTheRealEditor_Section.add(jbResizeMolms, c);

		c.gridx += c.gridwidth;
		c.weightx = c.gridwidth = 1;
		jlTemp = new JLabel("width:");
		jlTemp.setLabelFor(jsWidthMolm);
		jlTemp.setToolTipText("Width is expressed in TILE, corrispondig to " + MainController.MICROPIXEL_EACH_TILE
				+ " micropixel (a grid square).");
		jpTheRealEditor_Section.add(jlTemp, c);
		c.gridx++;
		jpTheRealEditor_Section.add(jsWidthMolm, c);

		c.gridx++;
		jlTemp = new JLabel("height:");
		jlTemp.setLabelFor(jsHeightMolm);
		jlTemp.setToolTipText("Height is expressed in TILE, corrispondig to " + MainController.MICROPIXEL_EACH_TILE
				+ " micropixel (a grid square).");
		jpTheRealEditor_Section.add(jlTemp, c);
		c.gridx++;
		jpTheRealEditor_Section.add(jsHeightMolm, c);

		// 3 bottoni sotto : copia, cancella selezione, pulisci mappa
		buttonsOnRow = new JButton[] { jbCopySelectedArea, jbClearSelectedArea, jbClearMolms };
		i = -1;
		c.gridx = EDITOR_LAST_COLUMN_START;
		c.gridwidth = 0;
		while (++i < buttonsOnRow.length) {
			c.fill = GridBagConstraints.BOTH;
			c.gridx += c.gridwidth;
			// EDITOR_LAST_COLUMN_START +
			// //(i << 1)
			// i * (EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT / buttonsOnRow.length);
			c.gridy = EDITOR_ROWS_GRIDBAGLAYOUT >> 1;
			// the first button must be wide half of the space, the other 2 just
			// a quarter
			c.weightx = c.gridwidth = (EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT >> (i == 0 ? 1 : 2));
			/*
			 * in place of this, to make the first button consume half of the
			 * space and then the others divide equally the rest, calculate:
			 * (i==0)? (EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT>>1):
			 * (EDITOR_SUBCOLUMNS_GRIDBAGLAYOUT/((buttonsOnRow.length-1)<<1))
			 */
			// c.weighty = c.gridheight = 1; // keep the same
			c.ipadx = c.ipady = 10;
			jpTheRealEditor_Section.add(buttonsOnRow[i], c);
		}
		buttonsOnRow = null;

		// in fondo il log
		reAddLog();

		//

		addMapGameViewToSection(false);
	}

	// TODO addBehaviourToComponents
	/** Add listeners to gui components */
	protected void addBehaviourToComponents() {
		listTileEnums.addListSelectionListener(l -> doOnMouseClick_ListTilesEnums());
		listTileSingleEnum.addListSelectionListener(l -> doOnMouseClick_OnTilemapSelectedFromList());
		mapGameView.addMouseListener(mouseAdapterMolmVisualizer);
		mapGameView.addMouseMotionListener(mouseAdapterMolmVisualizer);
		// add listeners to buttonsCache
		jbAllBackToStart.addActionListener(l -> hideAndCloseAll());
		jbCloseEditor.addActionListener(l -> closeMapEditorSection());
		jbClearSelectedArea.addActionListener(l -> {
			tilesSolid = tilesNonSolid = null;
			// listTilesSingleEnum.unselectAll();
		});
		jbNewMap.addActionListener(l -> setNewMapGame());
		jbLoadSelectedMap.addActionListener(l -> reloadSelectedMap());
		jbSaveMap.addActionListener(l -> askAndSaveMap());
		jbDeleteMap.addActionListener(l -> deleteSelectedMap());
		jbClearMolms.addActionListener(l -> {
			if (JOptionPane.showConfirmDialog(this, "Clear map?\nUnsaved changes will be lost !", "Clearing map",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				if (this.getMapGame() != null) getMapGame().clearMOLMs();
			}
		});
		jbResizeMolms.addActionListener(l -> {
			if (JOptionPane.showConfirmDialog(this, "Confirm resize map?", "Resizing map",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				if (getMapGame() != null) try {
					getMapGame().resizeMolms((Integer) jsWidthMolm.getValue(), (Integer) jsHeightMolm.getValue());
				} catch (Exception ex) {
					ex.printStackTrace();
					getLog().log("ERROR: on resizing molms, exception raised:\n\t" + ex);
				}
			}
		});
	}

	protected void addMapGameViewToSection(boolean isEditor) {
		if (mapGameView == null) mapGameView = newMapGameView_Editor();
		if (isEditor) {
			jspMapViewer_Preview.setViewportView(null);
			jspMapViewer_Editor.setViewportView(mapGameView);
			mapGameView.setPixelEachMicropixelCustom(false);
		} else {
			jspMapViewer_Preview.setViewportView(mapGameView);
			jspMapViewer_Editor.setViewportView(null);
			mapGameView.setPixelEachMicropixelCustom(4);
		}
	}

	protected void reAddLog() {
		LoggerMessages log;
		LoggerMessagesJScrollPane jsplog;
		log = getLog();
		if (log instanceof LoggerMessagesJScrollPane) {
			GridBagConstraints c;
			c = new GridBagConstraints();
			jsplog = (LoggerMessagesJScrollPane) log;
			c.fill = GridBagConstraints.BOTH;
			c.gridx = EDITOR_LAST_COLUMN_START;
			c.gridy = EDITOR_ROWS_GRIDBAGLAYOUT >> 1;
			c.weightx = c.gridwidth = c.gridy;
			// c.weighty = c.gridheight = 1; // keep the same
			c.ipadx = c.ipady = 10;
			jpTheRealEditor_Section.add(jsplog, c);
		}
	}

	public abstract GameMechanismType[] getGameMechanismTypes();

	@Override
	public void adaptSize(Dimension d) {
		setSize(d);
	}

	protected MapGameView newMapGameView_Editor() {
		return new MapGameView_Editor(this.mainGUI, true);
	}

	/**
	 * Add the {@link MapGameView} to the right component and prepare it to be
	 * shown
	 */
	protected void doOnShow() {
		showSection(MapEditorSections.LoadOrNew);
		addMapGameViewToSection(false);
		reloadMapsGameList();
	}

	protected void doOnHide() {
		mainGUI.onHideMapEditor();
	}

	@Override
	public boolean allocThreads() {
		return mapGameView.allocThreads();
	}

	@Override
	public void destroyThreads() {
		mapGameView.destroyThreads();
	}

	@Override
	public void startThreads() {
		mapGameView.startThreads();
	}

	@Override
	public void pauseThreads() {
		mapGameView.pauseThreads();
	}

	@Override
	public void resumeThreads() {
		mapGameView.resumeThreads();
	}

	//

	// TODO MOLM OPERATIONS

	protected void addToMOLM(String enumName, String imageName, int xMicropixelLocation, int yMicropixelLocation) {
		// if (getMapGame() != null) getMapGame().addToMOLM(t);
		this.getMain().addGameObject(enumName, imageName, xMicropixelLocation, yMicropixelLocation);
	}

	/**
	 * An area has been created, now find the instances inside the moms, collect
	 * them in ad-hoc lists, then prepare them for copy
	 */
	protected void setSelectionCopy() {
		AbstractMapGame mg;
		mg = getMapGame();
		if (mg != null) {
			shapeSelection.setCenter(Math.min(xPixelRealAreaOnMolmEnd, xPixelRealAreaOnMolmStart) / getPixelEachTile(),
					Math.min(yPixelRealAreaOnMolmEnd, yPixelRealAreaOnMolmStart) / getPixelEachTile());
			shapeSelection.setWidth(Math.abs(xPixelRealAreaOnMolmEnd - xPixelRealAreaOnMolmStart) / getPixelEachTile());
			shapeSelection
					.setHeight(Math.abs(yPixelRealAreaOnMolmEnd - yPixelRealAreaOnMolmStart) / getPixelEachTile());

			tilesSolid = mg.getMolmFromNotSolidity(false).collectOnShape(shapeSelection);
			tilesNonSolid = mg.getMolmFromNotSolidity(true).collectOnShape(shapeSelection);
		}
	}

	//

	// TODO other GUI methods

	@Override
	public void updateGui(int milliseconds) {
		/*
		 * if (isVisible() && milliseconds > 0) { if ((counterMillisRepaint +=
		 * milliseconds) > MILLIS_EACH_REPAINT) { counterMillisRepaint %=
		 * MILLIS_EACH_REPAINT; molmVisualizer.repaint(); }
		 * animationUpdaterOnListbox.milliseconds = milliseconds;
		 * listTilesSingleEnum.forEachJPanelItem(animationUpdaterOnListbox); }
		 */
		// TODO : update animations on listboxes
		// modelTileSingleEnum.forEach((k, enumElementGoti) -> {
		// enumElementGoti.newGameObjectInMap(this.main);
		// });
		if (enumElement_sTileImagesCached != null && enumElement_sTileImagesCached.length > 0)
			for (TileImage ti : this.enumElement_sTileImagesCached)
			ti.updateAnimation(milliseconds);
		mapGameView.updateGui(milliseconds);
	}

	protected void refreshEnumsLists() {
		// perform update
		// listTileEnumsremoveItems();
		// listTilesSingleEnum.removeItems();
		modelTileEnums.clear();
		modelTileSingleEnum.clear();
		// for (AbstractEnumGOTI aet : getAllGameObjectTileImage())
		// listTileEnums.addNewItem(aet);
		// modelTileEnums.add(aet);
		modelTileEnums.addElements(getAllGameObjectTileImage());
	}

	protected void closeMapEditorSection() {
		if (JOptionPane.showConfirmDialog(this, "Do you want to exit from map editor?", "Map editor closing",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			doOnMapEditorSectionClosing();
		}
	}

	protected void hideAndCloseAll() {
		this.setVisible(false);
		// this.mainGUI.onHideMapEditor();
	}

	public void showSection(MapEditorSections section) {
		this.sectionShown = section;
		cardLayout.show(this, section.name());
	}

	//

	// TODO OVERRIDE-DESIGNED METHODS

	protected void doOnMapEditorSectionClosing() {
		/*
		 * do stuffs like saving the current map if is not yet saved and clear
		 * molms
		 */
		askAndSaveMap();
		showSection(MapEditorSections.LoadOrNew);
	}

	protected void doOnMousePressReleased_MolmVis(MouseEvent me) {
		setSelectionCopy();
		areaMeaningOnMolmVisualizer = AreaMeaningOnMolmVisualizer.Nothing;
	}

	// TODO doOnMouseClicked_MolmVis
	/**
	 * Perform some actions:
	 * <ul>
	 * <li>If left click and there is a collection of selected GameObjectInMap,
	 * then paste them (and clear the selection)</li>
	 * <li>If left click and there is nothing selected, then add the selected
	 * "tile" (as it's jet implemented)</li>
	 * <li>If right click, open the menu</li>
	 * <li></li>
	 * <ul>
	 */
	protected void doOnMouseClicked_MolmVis(MouseEvent me) {
		int xMicropixel, yMicropixel;
		AbstractEnumElementGOTI atmic;
		AbstractEnumGOTI aet;
		// TileImage tm;

		xMicropixel = me.getX() / getPixelEachTile();
		yMicropixel = me.getY() / getPixelEachTile();
		atmic = listTileSingleEnum.getSelectedValue();
		aet = listTileEnums.getSelectedValue();
		if (aet != null && atmic != null) {
			/*
			 * tm = atmic.getTileMapInstance(getMain()); if (tm != null) {
			 * tm.setLog(getLog());
			 * tm.setScaleMicropixelToRealpixel(getPixelEachTile());
			 * tm.setLeftBottomCorner(xMicropixel, yMicropixel); addToMOLM(tm);
			 * }
			 */
			addToMOLM(aet.getNameEnum(), atmic.getName(), xMicropixel, yMicropixel);
		}
	}

	protected void doOnMouseClick_ListTilesEnums() {
		int i;
		AbstractEnumGOTI aegoti;
		MainController main;
		main = getMain();
		// Iterable<AbstractEnumElementGameObjectTileImage> it;
		// refreshEnumsLists();
		modelTileSingleEnum.clear();
		aegoti = listTileEnums.getSelectedValue();
		if (aegoti != null) {
			// it = aet;
			// if (it != null) {
			// for (AbstractEnumElementGOTI aeetmi :
			// aet)modelTilesSingleEnum.addElement(aeetmi);
			enumElement_sTileImagesCached = new // ArrayList<>
			TileImage[aegoti.size()];
			i = -1;
			// enumElement_sTileImages.ensureCapacity(size);
			for (AbstractEnumElementGOTI eegoti : aegoti) {
				enumElement_sTileImagesCached// .add(
				[++i] = eegoti.newTileImage(main);
			}

			modelTileSingleEnum.addElements(aegoti);
			// }
		}
	}

	protected void doOnMouseClick_OnTilemapSelectedFromList() {
		/*
		 * nothing : the selected item will be avaiable thanks to listboxview's
		 * public methods
		 */
	}

	// like the selection area
	protected void paintSomethingElse(Graphics g) {
		Color c;

		repaint();
		if (areaMeaningOnMolmVisualizer != null && areaMeaningOnMolmVisualizer != AreaMeaningOnMolmVisualizer.Nothing) {
			c = g.getColor();
			g.setColor(areaMeaningOnMolmVisualizer.colorArea);
			// paint area
			g.drawRect(Math.min(xPixelRealAreaOnMolmEnd, xPixelRealAreaOnMolmStart),
					Math.min(yPixelRealAreaOnMolmEnd, yPixelRealAreaOnMolmStart),
					Math.abs(xPixelRealAreaOnMolmEnd - xPixelRealAreaOnMolmStart),
					Math.abs(yPixelRealAreaOnMolmEnd - yPixelRealAreaOnMolmStart));
			g.setColor(c);
		}
	}

	protected MouseListenersOnMolmsVisualizer getMouseListenersOnMolmsVisualizer() {
		return new MouseListenersOnMolmsVisualizer(this);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		/*
		 * if (runnableImageUpdater != null) { runnableImageUpdater.canPaint =
		 * b; if (b) notifyAll(); }
		 */
		if (b)
			doOnShow();
		else
			doOnHide();
	}

	//

	// TODO OTHER MANAGEMENT METHODS

	protected void setMapName() {
		String s, old;
		s = jtfMapName.getText();
		old = mainGUI.getMainController().getMapGameName();
		if (old != null && mainGUI.getMainController().setMapName(s)) {
			this.modelMapJetCreated.removeElement(old);
			this.modelMapJetCreated.addElement(s);
		}
	}

	protected boolean saveCurrentMap() {
		return saveCurrentMap(true);
	}

	protected boolean saveCurrentMap(boolean updateName) {
		boolean ok;
		AbstractMapGame mg;
		ok = false;
		if (getMain().saveCurrentMap()) {
			mg = getMapGame();
			modelMapJetCreated.addElement(mg.getMapName());
			ok = true;
		}
		return ok;
	}

	protected void askAndSaveMap() {
		if (mainGUI.askConfirm("Save map",
				"Save current map?\nEverything you will not save will be lost forever.\n\n(Yes, it's a phylosophical, touching sentence.)")) {
			saveCurrentMap();
		}
	}

	protected void setNewMapGame() {
		String nameMap;
		AbstractMapGame mg;
		MainController main;

		main = getMain();
		if (JOptionPane.showConfirmDialog(this, "Create a new map?", "new map",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			mg = getMapGame();
			if (mg != null) askAndSaveMap();

			nameMap = jtfMapName.getText().trim();
			if (nameMap.equals("")) {
				JOptionPane.showMessageDialog(this, "Map name empty", "Map name empty", JOptionPane.ERROR_MESSAGE);
				return;
				// jtfMapName.setText(nameMap = "New Map");
			}

			/*
			 * if (main.getMolmManager() == null) {
			 * getLog().logAndPrintError("MAIN'S MOLM MANAGER NULL"); return; }
			 */
			addMapGameViewToSection(true);
			// error =
			main.startEditingNewMap(nameMap, (GameMechanismType) jcbGameMechanismTypes.getSelectedItem(), mapGameView);
			if (saveCurrentMap(true))//
				showSection(MapEditorSections.TheRealEditor);
			reloadMapsGameList();
		}
	}

	/*
	 * shouldn't be delegated to the controller? this method requires just the
	 * list of mapnames, but it sould be loaded by the controllers
	 */
	protected void reloadMapsGameList() {
		List<String> mapsList;
		this.modelMapJetCreated.clear();
		mapsList = this.getMain().getMapsNames();
		if (mapsList != null) modelMapJetCreated.setDelegate(mapsList);
	}

	protected void deleteSelectedMap() {
		// AbstractMementoMapGame map;
		String mapName;
		if (JOptionPane.showConfirmDialog(this, "Delete selected map?", "Delete map",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			mapName = listMapJetCreated.getSelectedValue();
			if (mapName != null) {
				if (deleteMap(mapName)) modelMapJetCreated.removeElement(mapName);
			}
		}
	}

	protected boolean deleteMap(String mapName) {
		File fileMap;
		LoaderGeneric load;
		MainController main;
		main = getMain();

		if (mapName != null) {
			// listMapJetCreated.removeItem(mapName);
			try {
				fileMap = new File((load = main.getLoader()).getPathMap() + mapName// .getMapName()
						+ load.getMapExtension());
				if (fileMap.exists()) {
					if (!fileMap.delete()) {
						getLog().logAndPrint(
								"Map " + mapName + " cannot be deleted on location: " + fileMap.getCanonicalPath());
					} else {
						reloadMapsGameList();
						return true;
					}
				} else
					getLog().logAndPrint("Map " + mapName + " don't exists on location: " + fileMap.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
				getLog().logAndPrint("ERROR: on deleteMap, exception raised:\n" + e);
			}
		}
		return false;
	}

	protected void reloadSelectedMap() {
		String mapName, pathMap;
		AbstractMapGame map;
		// Object o;
		// AbstractMementoMapGame mgg;
		File fileMap;
		LoaderGeneric load;
		MainController main;
		main = getMain();

		if (JOptionPane.showConfirmDialog(this, "Reload selected map?", "reloadmap",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			askAndSaveMap();
			mapName = listMapJetCreated.getSelectedValue();
			if (mapName != null) {

				/* shouldn't this operation been done on Controller-side? */
				fileMap = new File(pathMap = (load = main.getLoader()).getPathMap() + mapName// .getMapName()
						+ load.getMapExtension());
				if (fileMap.exists()) {
					try {
						getLog().logAndPrint(
								"MapEditor.reload selected Map: --" + mapName + "-- at path:\n\t" + pathMap);
						// TODO DELEGATE LOAD MAP ON CONTROLLER !!
						map = load.loadMap(mapName, getMain(), getAllGameObjectTileImage());

						if (map != null) {
							map.setLog(getLog());// <br>
							// map.reallocMolms();
							setMapGame(map);
							jtfMapName.setText(mapName);
							addMapGameViewToSection(true);
							showSection(MapEditorSections.TheRealEditor);
						}
					} catch (Exception e) {
						e.printStackTrace();
						getLog().logAndPrint(
								"ERROR: during reload map at:\n" + pathMap + "\n exception raised: " + e.getMessage());
					}
				} // <br>
			}
		}
	}

	//

	// TODO CLASSES

	public class MapGameView_Editor extends MapGameView {
		private static final long serialVersionUID = 510816562562L;

		public MapGameView_Editor(MainGUI mainGui, boolean isNotRepaintingJetPaintedInstances) {
			super(mainGui, isNotRepaintingJetPaintedInstances);
		}

		public MapGameView_Editor(MainGUI mainGui) {
			super(mainGui);
		}

		public MapGameView_Editor(MainGUI mainGui, PainterMolm painter) {
			super(mainGui, painter);
		}
	}

	public static class MouseAdapter_MapEditor_ListEnumTiles extends MouseAdapter {
		MapEditor me;

		MouseAdapter_MapEditor_ListEnumTiles(MapEditor me) {
			this.me = me;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			me.doOnMouseClick_ListTilesEnums();
		}
	}

	public static class MouseAdapter_MapEditor_ListSingleEnumTile extends MouseAdapter {
		MapEditor me;

		MouseAdapter_MapEditor_ListSingleEnumTile(MapEditor me) {
			this.me = me;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			me.doOnMouseClick_OnTilemapSelectedFromList();
		}
	}

	/** Usato nel metodo {@link MapEditor#updateGUIAnimation(int)}. */
	static class AnimationListboxTileUpdater
			implements Consumer<ListBoxView<AbstractEnumElementGOTI>.JPanelItemVisualizer> {
		int milliseconds;
		MapEditor me;

		AnimationListboxTileUpdater(MapEditor me) {
			this.me = me;
			milliseconds = 0;
		}

		public int getMilliseconds() {
			return milliseconds;
		}

		public AnimationListboxTileUpdater setMilliseconds(int milliseconds) {
			this.milliseconds = milliseconds;
			return this;
		}

		@Override
		public void accept(ListBoxView<AbstractEnumElementGOTI>.JPanelItemVisualizer t) {
			TileImage tm;
			AnimatedImage ai;
			ai = null;
			if (t != null) {
				if (t.getItem() != null) {
					/*
					 * tm = t.getItem().getTileMapInstance(me.getMain()); if (tm
					 * != null) { ai = tm.getAnimatedImage(); if (ai != null) {
					 * if (ai.passTime(milliseconds)) { t.updateItemShow(); } }
					 * }
					 */
					tm = ((JPIV_EE_TMSI) t).tmCache;
					if (tm != null) {
						ai = tm.getAnimatedImage();
						if (ai != null) {
							if (ai.passTime(milliseconds)) {
								t.updateItemShow();
							}
						}
					}
				} else {
					System.out.println("NULL ITEM");
				}
			} else
				System.out.println("NULL JPanelItemVis");
		}
	}

	@Deprecated
	static class Runnable_ImageUpdater implements Runnable {
		boolean canPaint;
		MapEditor me;

		Runnable_ImageUpdater(MapEditor me) {
			this.me = me;
			canPaint = false;
		}

		@Override
		public void run() {
			while (me.isAlive) {
				while (canPaint) {
					// some kind of update
					me.updateGui(-1);
				}
				try {
					// Thread.sleep(250);
					me.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static class LBV_EE_TMSI extends ListBoxView<AbstractEnumElementGOTI> {
		private static final long serialVersionUID = -96123012084L;

		public LBV_EE_TMSI(MapEditor me, MyComparator<AbstractEnumElementGOTI> comp) {
			super(comp);
			this.me = me;
		}

		MapEditor me;

		@Override
		public ListBoxView<AbstractEnumElementGOTI>.JPanelItemVisualizer newRow(AbstractEnumElementGOTI item) {
			return new JPIV_EE_TMSI(item);
		}

		public class JPIV_EE_TMSI extends JPanelItemVisualizer {
			private static final long serialVersionUID = 45105202087L;

			public JPIV_EE_TMSI(AbstractEnumElementGOTI item, LayoutManager layout) {
				super(item, layout);
				tmCache = null;
			}

			public JPIV_EE_TMSI(AbstractEnumElementGOTI item) {
				this(item, new BorderLayout());
			}

			private JLabel jl, jlIcon;
			ImageIcon ic = null;
			BufferedImage biPrev = null;
			TileImage tmCache;
			long i = 0;

			// jl con icona (che eventualmente si aggiorna con il passare
			// del
			// tempo) e con nome
			@Override
			public void updateItemShow() {
				int jlh;
				BufferedImage bi;
				Image im;

				// if (jl == null) initGUI();

				setSize(jlIcon.getWidth() + Math.max(jl.getWidth(), jl.getPreferredSize().width) + 50,
						// jlIcon.getHeight()
						SIZE_JL_LIST_SINGLE_ENUM_TILEMAP);

				if (tmCache == null) {
					tmCache = getItem().newTileImage(me.getMain());
					tmCache.scaleImages(me.getPixelEachTile(), me.getPixelEachTile());
				}
				if (tmCache != null) {
					bi = tmCache.getImageResized();
					if (bi != null && bi != biPrev) {
						jlh = jl.getHeight();
						im = bi;
						if (bi.getHeight() != jlh || bi.getWidth() != jlh || biPrev != bi) {
							im = bi.getScaledInstance(jlh, jlh, Image.SCALE_SMOOTH);
						}
						ic = new ImageIcon(im);
					}
					if (ic == null || biPrev != bi) {
						if (bi == null && biPrev != bi) ic = new ImageIcon(biPrev = bi);
						jlIcon.setIcon(ic);
					}
				}
				repaint();
			}

			@Override
			public void initGUI() {
				this.setLayout(new BorderLayout());
				setSize(SIZE_JL_LIST_SINGLE_ENUM_TILEMAP, SIZE_JL_LIST_SINGLE_ENUM_TILEMAP);
				jl = new JLabel();
				jl.addMouseListener(getOnMouseClickSelectMe());
				jl.setSize(SIZE_JL_LIST_SINGLE_ENUM_TILEMAP, SIZE_JL_LIST_SINGLE_ENUM_TILEMAP);
				this.add(jl, BorderLayout.CENTER);
				jlIcon = new JLabel();
				jlIcon.addMouseListener(getOnMouseClickSelectMe());
				jlIcon.setSize(SIZE_JL_LIST_SINGLE_ENUM_TILEMAP, SIZE_JL_LIST_SINGLE_ENUM_TILEMAP);
				this.add(jlIcon, BorderLayout.WEST);

				this.setSize(DEFAULT_DIMENSION);
				super.setVisible(true);
				jl.setText(getItemDescription());
			}

		}
	}

	public static class MouseListenersOnMolmsVisualizer extends MouseAdapter {
		MapEditor me;

		MouseListenersOnMolmsVisualizer(MapEditor me) {
			this.me = me;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			me.doOnMousePressReleased_MolmVis(e);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			mouseReleased(e);
			me.doOnMouseClicked_MolmVis(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (me.areaMeaningOnMolmVisualizer != AreaMeaningOnMolmVisualizer.Selection) {
				me.areaMeaningOnMolmVisualizer = AreaMeaningOnMolmVisualizer.Selection;
				me.xPixelRealAreaOnMolmEnd = 1 + (me.xPixelRealAreaOnMolmStart = e.getX());
				me.yPixelRealAreaOnMolmEnd = 1 + (me.yPixelRealAreaOnMolmStart = e.getY());
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			me.xPixelRealAreaOnMolmEnd = e.getX();
			me.yPixelRealAreaOnMolmEnd = e.getY();
		}
		// public void mouseReleased(MouseEvent e) {}
	}

	static class CellRender_TileSingleEnum extends JLabel implements ListCellRenderer<AbstractEnumElementGOTI> {
		private static final long serialVersionUID = 9204708170387466713L;
		static final Color BGS = new Color(128, 240, 255);
		static final Border BS = BorderFactory
				.createLineBorder(new Color(BGS.getRed() - 64, BGS.getGreen(), BGS.getBlue()), 2);
		MapEditor editor;
		ImageIcon ic;

		public CellRender_TileSingleEnum(MapEditor editor) {
			super();
			this.editor = editor;
			ic = new ImageIcon();
			this.setIcon(ic);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends AbstractEnumElementGOTI> list,
				AbstractEnumElementGOTI value, int index, boolean isSelected, boolean cellHasFocus) {
			TileImage ti;
			ti = editor.enumElement_sTileImagesCached[index];
			if (ti != null) {
				setText(ti.getImageName());
				ic.setImage(ti.getImageResized());
				setBackground(BGS);
				setBorder(BS);
			} else {
				setText("Undefined at index " + index);
				ic.setImage(null);
				setBackground(Color.WHITE);
				setBorder(null);
			}
			return this;
		}
	}
}