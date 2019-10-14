package common.abstractCommon;

import java.util.Comparator;

import common.FullReloadEnvironment;
import common.GameMechanism;
import common.GameObjectInMap;
import common.MOLMManagerObservable;
import common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager;
import common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager_Delegating;
import common.abstractCommon.behaviouralObjectsAC.DoSomethingWithMolmShapespecOwid;
import common.abstractCommon.behaviouralObjectsAC.MementoPatternImplementor;
import common.abstractCommon.behaviouralObjectsAC.ObjectSerializable;
import common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import common.abstractCommon.referenceHolderAC.MainHolder;
import common.mainTools.Comparators;
import common.mainTools.LoggerMessages;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.ObservableMolm;

/**
 * Part of the Model. Holds all it's needed to create a map : the grid-matrix of
 * instances of {@link GameObjectInMap}, others stuffs like "Doors" (object
 * letting the player to move from a map to another ones), "Spawns" (it's useful
 * to rapidly know where the player could spawn), ecc...
 * <p>
 * 06/04/2018: This class does not inherit from {@link MOLMManagerObservable}
 * but holds a reference (obtained through both
 * {@link #getMolmManagerDelegated()} or {@link #getMolmManagerObservable()})
 * because it's needed to define a game map. This class also does not have the
 * responsibility to create new instances of {@link MOLMManagerObservable}: it's
 * delegated to {@link GameMechanism}.
 */

public interface AbstractMapGame extends ObjectSerializable, LoggerMessagesHolder, AbstractMOLMManager_Delegating,
		ObservableMolm, MainHolder, MementoPatternImplementor {

	public static final Comparator<AbstractMapGame> COMPARATOR_ABSTRACT_MAP_GAME = (m1, m2) -> {
		if (m1 == m2) return 0;
		if (m1 == null) return -1;
		if (m2 == null) return 1;
		return Comparators.STRING_COMPARATOR.compare(m1.getMapName(), m2.getMapName());
	};

	//

	/*
	 * it's about silly to create an interface that is the holder of a frame
	 * holder .. so I implement that silly interface directly.
	 */
	public String getMapName();

	/**
	 * See {@link GameMechanism#getGameTypeOrdinal()}.
	 */
	public int getGameTypeOrdinal();

	public default MOLMManagerObservable getMolmManagerObservable() {
		return (MOLMManagerObservable) getMolmManagerDelegated();
	}

	//

	public AbstractMapGame setMapName(String mapName);

	/** See {@link #getGameTypeOrdinal()}. */
	public AbstractMapGame setGameTypeOrdinal(int gameTypeOrdinal);

	/**
	 * This class holds a reference but should not have the responsability to
	 * allocate instances.
	 */
	public default AbstractMapGame setMolmManagerObservable(MOLMManagerObservable mmo) {
		this.setMolmManagerDelegated(mmo);
		return this;
	}

	// other

	@Override
	public default String doAfterDeserialization(Object arguments) {
		this.setLogSafe(LoggerMessages.LOGGER_DEFAULT);
		for (AbstractMatrixObjectLocationManager molm : this)
			if (molm != null) molm.resetDefaultInstances();
		return null;
	}

	//

	// TODO OVERRIDE from molm manager delegated
	@Override
	public default String addToMOLM(ObjectWithID owid) {
		String error;
		error = getMolmManagerDelegated().addToMOLM(owid);
		if (error != null) notifyMolmObservers(MolmEvent.ObjectAdded, owid, null// getMolms()
		);
		return error;
	}

	@Override
	public default String addToMOLM(ObjectWithID owid, boolean removePrec) {
		String error;
		error = getMolmManagerDelegated().addToMOLM(owid, removePrec);
		if (error != null) notifyMolmObservers(MolmEvent.ObjectAdded, owid, null// getMolms()
		);
		return error;
	}

	@Override
	public default String removeFromMOLM(ObjectWithID owid) {
		String error;
		error = getMolmManagerDelegated().removeFromMOLM(owid);
		if (error != null) notifyMolmObservers(MolmEvent.ObjectRemoved, owid, null// getMolms()
		);
		return error;
	}

	@Override
	public default String doOnMOLM(ObjectWithID owid, DoSomethingWithMolmShapespecOwid doSome) {
		String error;
		error = getMolmManagerDelegated().doOnMOLM(owid, doSome);
		if (error != null) notifyMolmObservers(MolmEvent.CustomOperation, owid, this// getMolms()
		);
		return error;
	}

	@Override
	public default String clearMOLM(AbstractMatrixObjectLocationManager preferredMolm) {
		String error;
		error = getMolmManagerDelegated().doOnMOLM(null, DoSomethingWithMolmShapespecOwid.CLEANER, preferredMolm);
		if (error != null) notifyMolmObservers(MolmEvent.MapCleaned, preferredMolm, this //
		// if i had pointers I wouldn't have allocated this new array..
		// new AbstractMatrixObjectLocationManager[] { preferredMolm }
		// , indexOf(this.getMolms(), preferredMolm)
		);
		return error;
	}

	public static int indexOf(AbstractMatrixObjectLocationManager[] molms, AbstractMatrixObjectLocationManager m) {
		int i, l;
		i = -1;
		l = molms.length;
		while (++i < l)
			if (molms[i] == m) return i;
		return -1;
	}

	//

	// TODO MEMEMNTO

	@Override
	public default boolean reloadState(Memento m) {
		return reloadState(m, null);
	}

	@Override
	public default boolean reloadState(Memento m, FullReloadEnvironment fre) {
		AbstractMementoMapGame amgg;
		if (m != null && m instanceof AbstractMementoMapGame) {
			amgg = (AbstractMementoMapGame) m;
			setMapName(amgg.mapName);
			setMolmManagerDelegated((AbstractMOLMManager) amgg.mementoMolmManager.reinstanceFromMe(fre));
			return true;
		}
		return false;
	}

	@Override
	public AbstractMementoMapGame createMemento();

	public static abstract class AbstractMementoMapGame extends Memento {
		private static final long serialVersionUID = -9019949463377382820L;
		public static final Comparator<AbstractMementoMapGame> COMPARATOR_ABSTRACT_MAP_GAME = (m1, m2) -> {
			if (m1 == m2) return 0;
			if (m1 == null) return -1;
			if (m2 == null) return 1;
			return Comparators.STRING_COMPARATOR.compare(m1.getMapName(), m2.getMapName());
		};

		String mapName;
		AbstractMOLMManager.AbstractMementoMOLMManager mementoMolmManager;

		public AbstractMementoMapGame() {
			super();
		}

		public AbstractMementoMapGame(AbstractMapGame mgg) {
			super();
			mapName = mgg.getMapName();
			mementoMolmManager = (AbstractMementoMOLMManager) mgg.getMolmManagerDelegated().createMemento();
		}

		public String getMapName() {
			return mapName;
		}

		public AbstractMOLMManager.AbstractMementoMOLMManager getMementoMolmManager() {
			return mementoMolmManager;
		}

		/**
		 * WARNING: it did not call
		 * {@link AbstractMOLMManager#reallocMolms(int, int)}.<br>
		 * The caller must call it !!
		 */
		@Override
		public Object reinstanceFromMe() {
			return reinstanceFromMe(null);
		}

		/**
		 * WARNING: it did not call
		 * {@link AbstractMOLMManager#reallocMolms(int, int)}.<br>
		 * The caller must call it !!
		 */
		public abstract Object reinstanceFromMe(FullReloadEnvironment fre);
	}
}