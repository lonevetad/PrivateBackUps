package common.abstractCommon;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import common.FullReloadEnvironment;
import common.GameObjectInMap;
import common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager;
import common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager_Delegating;
import common.abstractCommon.behaviouralObjectsAC.MementoPatternImplementor;
import common.abstractCommon.behaviouralObjectsAC.MyObservable;
import common.abstractCommon.behaviouralObjectsAC.MyObserver;
import common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import common.abstractCommon.referenceHolderAC.MainHolder;
import common.mainTools.Comparators;
import common.mainTools.LoggerMessages;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.ObservableMolm;
import common.mainTools.mOLM.abstractClassesMOLM.ObserverMolm;
import tools.RedBlackTree;

public abstract class GameModelGeneric implements Serializable, AbstractMOLMManager_Delegating, MyObservable,
		ObservableMolm, MementoPatternImplementor {
	private static final long serialVersionUID = 4306098807470070L;

	public GameModelGeneric() {
		// setMolmManagerDelegated();
		myObservers = new LinkedList<>();
		resetProgressiveIDGameObject();
		allGameObjectInMap = new RedBlackTree<>(Comparators.INTEGER_COMPARATOR);
	}

	public GameModelGeneric(MainController mainController) {
		this();
		this.mainController = mainController;
	}

	public GameModelGeneric(GameModelGeneric.MementoGameModelGeneric memento) {
		this();
		reloadState(memento);
	}

	int progressiveIDGameObject = 0;
	protected AbstractMapGame mapGameCurrent;
	AbstractPlayer player;
	RedBlackTree<Integer, GameObjectInMap> allGameObjectInMap;
	transient List<MyObserver> myObservers;
	transient MainController mainController;
	// AbstractMOLMManager molmManagerDelegated;

	//

	// TODO FIELDS

	public AbstractMapGame getMapGameCurrent() {
		return mapGameCurrent;
	}

	@Override
	public AbstractMOLMManager getMolmManagerDelegated() {
		return mapGameCurrent;
	}

	public MainController getMainController() {
		return mainController;
	}

	public ObservableMolm getObservableMolm() {
		return getMapGameCurrent();
	}

	public AbstractPlayer getPlayer() {
		return player;
	}

	@Override
	public List<MyObserver> getMyObservers() {
		return myObservers;
	}

	public int getProgressiveIDGameObject() {
		return progressiveIDGameObject;
	}

	/** Retrive the "progressiveIDGameObject" and increase it. */
	public int getAndNextIDGameObject() {
		return progressiveIDGameObject++;
	}

	public RedBlackTree<Integer, GameObjectInMap> getAllGameObjectInMap() {
		return allGameObjectInMap;
	}

	//

	// TODO SETTER

	public GameModelGeneric setMainController(MainController mainController) {
		this.mainController = mainController;
		return this;
	}

	public GameModelGeneric setMapGameCurrent(AbstractMapGame mapGameCurrent) {
		this.mapGameCurrent = mapGameCurrent;
		return this;
	}

	public GameModelGeneric setPlayer(AbstractPlayer player) {
		this.player = player;
		return this;
	}

	@Override
	public GameModelGeneric setMolmManagerDelegated(AbstractMOLMManager molmManager) {
		if (molmManager != null && molmManager instanceof AbstractMapGame)
			this.mapGameCurrent = (AbstractMapGame) molmManager;
		return this;
	}

	//

	// TODO OTHER

	// ABSTRACT

	/** Shall return <code>new AbstractSaveData_Subclass(this);</code>. */
	public abstract AbstractSaveData newSaves();

	/**
	 * Set the Model depending on the given save.<br>
	 * Should call functions like {@link #resetProgressiveIDGameObject()}.
	 */
	// public abstract boolean loadSave(AbstractSaveData save);

	//
	// public

	/**
	 * Reset all values to initial/default values. Usefull when preparing on
	 * loading a new game or a new save.
	 */
	public void resetAll() {
		String error;
		resetProgressiveIDGameObject();
		error = clearMOLMs();
		if (error != null) mainController.log(error);
	}

	public void resetProgressiveIDGameObject() {
		progressiveIDGameObject = 0;
	}

	/**
	 * Add the given object to a collection WITHOUT adding it to the
	 * MOLM(s).<br>
	 * It will be stored in {@link #getAllGameObjectInMap()}.
	 */
	public String addGameObjectNotInMolm(GameObjectInMap o) {
		String error;
		RedBlackTree<Integer, GameObjectInMap> all;
		Integer id;
		error = null;
		if (o != null) {
			all = getAllGameObjectInMap();
			if (all != null)
				if ((id = o.getGameObjectID()) != null)
					all.add(id, o);
				else
					error = "ERROR: on addGameObjectNotInMolm, the game object ID is null";
			else
				error = "ERROR: on addGameObjectNotInMolm, the set of game objects is null";
		} else
			error = "ERROR: on addGameObjectNotInMolm, the given object is null";
		return error;
	}

	/**
	 * Remove the given object to a collection WITHOUT removing it to the
	 * MOLM(s).<br>
	 * It will be removed ONLY from {@link #getAllGameObjectInMap()}.<br>
	 * DON'T rely only on this method, because it could create bugs.
	 */
	public String removeGameObjectNotFromMolm(GameObjectInMap o) {
		String error;
		RedBlackTree<Integer, GameObjectInMap> all;
		Integer id;
		error = null;
		if (o != null) {
			all = getAllGameObjectInMap();
			if (all != null)
				if ((id = o.getGameObjectID()) != null)
					all.delete(id);
				else
					error = "ERROR: on removeGameObjectNotFromMolm, the game object ID is null";
			else
				error = "ERROR: on removeGameObjectNotFromMolm, the set of game objects is null";
		} else
			error = "ERROR: on removeGameObjectNotFromMolm, the given object is null";
		return error;
	}

	//

	// TODO DELEGATED

	// molm observer

	@Override
	public boolean addMolmObserver(ObserverMolm o) {
		return mapGameCurrent.addMolmObserver(o);
	}

	@Override
	public List<ObserverMolm> getMolmObservers() {
		return mapGameCurrent.getMolmObservers();
	}

	@Override
	public boolean addMolmObservers(Iterable<ObserverMolm> os) {
		return mapGameCurrent.addMolmObservers(os);
	}

	@Override
	public ObservableMolm setMolmObservers(List<ObserverMolm> observers) {
		return mapGameCurrent.setMolmObservers(observers);
	}

	@Override
	public void removeMolmObservers() {
		mapGameCurrent.removeMolmObservers();
	}

	@Override
	public void removeMolmObserver(ObserverMolm o) {
		mapGameCurrent.removeMolmObserver(o);
	}

	@Override
	public void notifyMolmObservers() {
		mapGameCurrent.notifyMolmObservers();
	}

	@Override
	public void notifyMolmObservers(MolmEvent me, AbstractMOLMManager molmManager) {
		mapGameCurrent.notifyMolmObservers(me, molmManager);
	}

	@Override
	public void notifyMolmObservers(MolmEvent me, Object extraArguments, AbstractMOLMManager molmManager) {
		mapGameCurrent.notifyMolmObservers(me, extraArguments, molmManager);
	}

	// molm manager

	@Override
	public AbstractMatrixObjectLocationManager[] getMolms() {
		return mapGameCurrent == null ? null : mapGameCurrent.getMolms();
	}

	public MainController getMain() {
		return mapGameCurrent.getMain();
	}

	public LoggerMessages getLog() {
		return mainController.getLog();
	}

	public List<ObserverMolm> getObserversMolm() {
		return mapGameCurrent.getMolmObservers();
	}

	public MainHolder setMain(MainController main) {
		return mapGameCurrent.setMain(main);
	}

	public LoggerMessagesHolder setLog(LoggerMessages log) {
		return mapGameCurrent.setLog(log);
	}

	public boolean addObserverMolm(ObserverMolm o) {
		return mapGameCurrent.addMolmObserver(o);
	}

	public void removeObservers() {
		mapGameCurrent.removeMolmObservers();
	}

	public void removeObserver(ObserverMolm o) {
		mapGameCurrent.removeMolmObserver(o);
	}

	@Override
	public AbstractMOLMManager_Delegating setDefaultMolmManagerDelegated() {
		return mapGameCurrent.setDefaultMolmManagerDelegated();
	}

	@Override
	public int getMolmsLength() {
		return mapGameCurrent.getMolmsLength();
	}

	public String getMapName() {
		return mapGameCurrent.getMapName();
	}

	@Override
	public int getHeightMicropixel() {
		return mapGameCurrent.getHeightMicropixel();
	}

	public AbstractMapGame setMapName(String mapName) {
		return mapGameCurrent.setMapName(mapName);
	}

	@Override
	public AbstractMOLMManager setWidthMicropixel(int widthMicropixel) {
		return mapGameCurrent.setWidthMicropixel(widthMicropixel);
	}

	@Override
	public AbstractMatrixObjectLocationManager[] resizeMolms(int widthMicropixel, int heightMicropixel) {
		return mapGameCurrent.resizeMolms(widthMicropixel, heightMicropixel);
	}

	@Override
	public boolean reloadState(Memento m, FullReloadEnvironment fre) {
		return mapGameCurrent.reloadState(m, fre);
	}

	@Override
	public String addToMOLM(ObjectWithID owid) {
		String error;
		error = getMolmManagerDelegated().addToMOLM(owid);
		if (error == null) {
			if (owid instanceof GameObjectInMap)
				error = addGameObjectNotInMolm((GameObjectInMap) owid);
			else
				error = "ERROR on addToMOLM: the given ObjectWithID is not a instance of GameObjectInMap";
		}
		return error;
	}

	@Override
	public String addToMOLM(ObjectWithID owid, boolean removePrec) {
		String error;
		error = getMolmManagerDelegated().addToMOLM(owid, removePrec);
		if (error == null) {
			if (owid instanceof GameObjectInMap)
				error = addGameObjectNotInMolm((GameObjectInMap) owid);
			else
				error = "ERROR on addToMOLM: the given ObjectWithID is not a instance of GameObjectInMap";
		}
		return error;

	}

	@Override
	public String removeFromMOLM(ObjectWithID owid) {
		String error;
		error = getMolmManagerDelegated().removeFromMOLM(owid);
		if (error == null) {
			if (owid instanceof GameObjectInMap)
				error = removeGameObjectNotFromMolm((GameObjectInMap) owid);
			else
				error = "ERROR on removeFromMOLM: the given ObjectWithID is not a instance of GameObjectInMap";
		}
		return error;
	}

	/**
	 * Clear all molms AND the set {@link #getAllGameObjectInMap()}.<br>
	 * SO BEWARE: remember manually of what {@link GameObjectInMap()} are not
	 * added to the molm(s) and keep them in another set, if necessary.
	 * {@inheritDoc}
	 */
	@Override
	public String clearMOLMs() {
		String error;
		RedBlackTree<Integer, GameObjectInMap> all;
		error = getMolmManagerDelegated().clearMOLM(null);
		if (error == null) {
			all = getAllGameObjectInMap();
			if (all != null)
				all.clear();
			else
				error = "ERROR on clearMOLMs: the given set of GameObjectInMap is null";
		}
		return error;
	}

	/**
	 * Differently to {@link #clearMOLMs()}, this method does not clear the set
	 * {@link GameObjectInMap()}.
	 */
	@Override
	public String clearMOLM(AbstractMatrixObjectLocationManager preferredMolm) {
		String error;
		error = getMolmManagerDelegated().clearMOLM(preferredMolm);
		/*
		 * if (error == null) { RedBlackTree<Integer, GameObjectInMap> all; all
		 * = getAllGameObjectInMap(); if (all != null) all.clear(); else error =
		 * "ERROR on clearMOLMs: the given set of GameObjectInMap is null"; } /*
		 */
		return error;
	}

	//

	// TODO MEMENTO

	@Override
	public abstract MementoGameModelGeneric createMemento();

	/** Returns true if the reload operation was successfull. */
	@Override
	public boolean reloadState(Memento m) {
		boolean ok;
		MementoGameModelGeneric memento;
		ok = m != null && m instanceof MementoGameModelGeneric;
		if (ok) {
			memento = (MementoGameModelGeneric) m;
			this.progressiveIDGameObject = memento.progressiveIDGameObject;
			this.player = memento.player;
			this.mapGameCurrent = (AbstractMapGame) memento.mapGameCurrent_Memento.reinstanceFromMe();
			this.allGameObjectInMap = memento.allGameObjectInMap;
		}
		return ok;
	}

	public static abstract class MementoGameModelGeneric extends Memento {
		private static final long serialVersionUID = 515061518L;

		public MementoGameModelGeneric() {
			super();
		}

		public MementoGameModelGeneric(GameModelGeneric gmg) {
			super();
			this.progressiveIDGameObject = gmg.progressiveIDGameObject;
			this.mapGameCurrent_Memento = gmg.mapGameCurrent.createMemento();
			this.player = gmg.player;
			this.allGameObjectInMap = gmg.allGameObjectInMap;
		}

		int progressiveIDGameObject = 0;
		protected AbstractMapGame.AbstractMementoMapGame mapGameCurrent_Memento;
		AbstractPlayer player;
		RedBlackTree<Integer, GameObjectInMap> allGameObjectInMap;

		@Override
		public abstract GameModelGeneric reinstanceFromMe();
	}

}