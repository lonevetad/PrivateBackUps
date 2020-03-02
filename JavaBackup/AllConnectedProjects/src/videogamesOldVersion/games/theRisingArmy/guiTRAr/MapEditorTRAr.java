package games.theRisingArmy.guiTRAr;

import common.abstractCommon.GameMechanismType;
import common.gui.MapEditor;
import games.theRisingArmy.main.SharedStuffs_TRAr;

public class MapEditorTRAr extends MapEditor {
	private static final long serialVersionUID = -6540902L;
	protected static final int MIN_WIDTH_JTABBEDPANE = 1000, MIN_HEIGHT_JTABBEDPANE = 600,
			MILLISECOND_BETWEN_EACH_REPAINT = 25;
	protected static final long MILLIS_BETWEEN_EACH_AUTOSAVE = 300000, MILLIS_TICK_AUTOSAVE_CONTROL = 1000;
	protected static final String TEXT_AUTOSAVE_TRUE = "Click to suspend autosave",
			TEXT_AUTOSAVE_FALSE = "Click to start autosave", EMPTY_TEXT = "",
			DEFAULT_MAP_NAME = "Insert your map's name";

	public MapEditorTRAr() {
		super();
	}

	// LoggerMessages log;
	//
	// //
	//
	// @Override
	// public LoggerMessages getLog() {
	// // if (mainGUI == null) return LoggerMessages.LOGGER_DEFAULT;
	// return log;// mainGUI.getLog();
	// }
	//
	// @Override
	// public LoggerMessagesHolder setLog(LoggerMessages log) {
	// this.log = log;
	// return this;
	// }

	@Override
	public GameMechanismType[] getGameMechanismTypes() {
		return SharedStuffs_TRAr.GAME_MECHANISMS_TYPES_VALUE;
	}

	//

	//

	// public static void main(String[] args) {
	// JFrame fin;
	// MapEditor me;
	//
	// System.out.println("start");
	// fin = new JFrame_Repaint("EDITOR TEST");
	// fin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// me = new MapEditorTRAr();
	// fin.add(me);
	// fin.addComponentListener(new ComponentListener_DoOnResize() {
	//
	// @Override
	// public void componentResized(ComponentEvent e) {
	// me.setSize(fin.getSize());
	// }
	//
	// });
	// me.setLog(new LoggerMessagesJScrollPane());
	// me.initGUI();
	// me.showSection(MapEditorSections.TheRealEditor);
	// //
	// fin.setSize(800, 500);
	// fin.setVisible(true);
	// System.out.println("end");
	// }

}