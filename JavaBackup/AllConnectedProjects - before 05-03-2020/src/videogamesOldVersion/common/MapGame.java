package common;

import java.util.List;

import common.abstractCommon.AbstractMapGame;
import common.abstractCommon.MainController;
import common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager;
import common.abstractCommon.referenceHolderAC.FrameHolder;
import common.mainTools.LoggerMessages;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObservableMolm;
import common.mainTools.mOLM.abstractClassesMOLM.ObserverMolm;

/**
 * See {@link AbstractMapGame}.
 */
public class MapGame implements AbstractMapGame {
	private static final long serialVersionUID = 1025478717017063695L;

	//

	public static MapGameBuilder startBuilding(MainController main, String mapName) {
		return new MapGameBuilder(main, mapName);
	}

	public static class MapGameBuilder {
		boolean reallocMolmsNeeded;
		int widthMicropixel, heightMicropixel, gameTypeOrdinal;
		MOLMManagerObservable molmManagerObservable;
		MainController main;
		String mapName;

		public MapGameBuilder(MainController main, String mapName) {
			super();
			reallocMolmsNeeded = false;
			this.mapName = mapName;
			this.main = main;
			widthMicropixel = heightMicropixel = gameTypeOrdinal = 0;
		}

		public MapGameBuilder setReallocMolmsNeeded(boolean reallocMolmsNeeded) {
			this.reallocMolmsNeeded = reallocMolmsNeeded;
			return this;
		}

		public MapGameBuilder setSizeMicropixel(int widthMicropixel, int heightMicropixel) {
			if (widthMicropixel < 2 || heightMicropixel < 2)
				throw new IllegalArgumentException("Invalid micropixel dimensions: widthMicropixel: " + widthMicropixel
						+ ", heightMicropixel: " + heightMicropixel + ". At least 2 micropixel each.");
			this.widthMicropixel = widthMicropixel;
			this.heightMicropixel = heightMicropixel;
			return this;
		}

		public MapGameBuilder setGameTypeOrdinal(int gameTypeOrdinal) {
			if (gameTypeOrdinal < -1) gameTypeOrdinal = -1;
			this.gameTypeOrdinal = gameTypeOrdinal;
			return this;
		}

		public MapGameBuilder setMolmManagerDelegated(MOLMManagerObservable molmManagerDelegated) {
			this.molmManagerObservable = molmManagerDelegated;
			// checkMolmManagerObservable();
			return this;
		}

		/** BEWARE: No null-checks are performed to help debug. */
		public MapGameBuilder addMolmObserver(ObserverMolm observer) {
			// checkMolmManagerObservable();
			this.molmManagerObservable.addMolmObserver(observer);
			return this;
		}

		public MapGameBuilder addMolmObservers(Iterable<ObserverMolm> observers) {
			// if (observers instanceof List<?>)
			// this.molmManagerObservable.setMolmObservers((List<ObserverMolm>)
			// observers);
			// else
			this.molmManagerObservable.addMolmObservers(observers);
			return this;
		}

		/*
		 * protected void checkMolmManagerObservable() { if
		 * (this.molmManagerObservable == null) this.molmManagerObservable = new
		 * MOLMManagerObservable(); }
		 */

		public MapGame build() throws IllegalStateException {
			if (widthMicropixel < 2) widthMicropixel = MOLMManager.DEFAULT_MOLM_WIDTH_MICROPIXEL;
			if (heightMicropixel < 2) heightMicropixel = MOLMManager.DEFAULT_MOLM_HEIGHT_MICROPIXEL;
			if (gameTypeOrdinal < -1) gameTypeOrdinal = -1;
			// checkMolmManagerObservable();
			if (this.molmManagerObservable == null) throw new IllegalStateException(
					"Cannot build a MapGame without a non-null instance of MolmManagerObservable");
			return new MapGame(main, mapName, molmManagerObservable, widthMicropixel, heightMicropixel,
					reallocMolmsNeeded);
		}
	}

	//

	// do not invoke me
	private MapGame() {
		// this(FrameHolder.newDefaultFrameHolder());
		super();
	}

	protected MapGame(MainController main, String mapName, MOLMManagerObservable molmManagerDelegated,
			int widthMicropixel, int heightMicropixel, boolean reallocMolmsNeeded) {
		this();
		this.mapName = mapName;
		if (widthMicropixel < 1 || heightMicropixel < 1)
			throw new IllegalArgumentException("Invalid micropixel dimensions: widthMicropixel: " + widthMicropixel
					+ ", heightMicropixel: " + heightMicropixel);
		this.molmManagerDelegated = molmManagerDelegated;
		// != null ? molmManagerDelegated : new MOLMManagerObservable();
		// AbstractMOLMManager.newDefaultMOLMManager();
		this.main = main;
		if (reallocMolmsNeeded) reallocMolms(widthMicropixel, heightMicropixel);
	}

	public MapGame(MementoMapGame m, FullReloadEnvironment fre) {
		this();
		this.reloadState(m, fre);
	}

	int gameTypeOrdinal;
	// , widthMicropixel, heightMicropixel;
	protected transient LoggerMessages log;
	// protected AbstractMatrixObjectLocationManager molmNonSolid, molmSolid,
	// molms[];
	protected MOLMManagerObservable molmManagerDelegated;
	protected String mapName;
	protected transient MainController main;

	//

	// TODO GETTER

	@Override
	public int getGameTypeOrdinal() {
		return gameTypeOrdinal;
	}

	@Override
	public MainController getMain() {
		return main;
	}

	@Override
	public String getMapName() {
		return mapName;
	}

	@Override
	public LoggerMessages getLog() {
		return log;
	}

	@Override
	public AbstractMOLMManager getMolmManagerDelegated() {
		return molmManagerDelegated;
	}

	public AbstractMatrixObjectLocationManager getMolmNonSolid() {
		return getMolmFromNotSolidity(true);
		// molmNonSolid;
	}

	public AbstractMatrixObjectLocationManager getMolmSolid() {
		return getMolmFromNotSolidity(false);
		// molmSolid;
	}

	@Override
	public List<ObserverMolm> getMolmObservers() {
		return this.molmManagerDelegated == null ? null : molmManagerDelegated.observersMolm;
	}

	//

	// TODO SETTER

	@Override
	public MapGame setGameTypeOrdinal(int gameTypeOrdinal) {
		if (gameTypeOrdinal < -1) gameTypeOrdinal = -1;
		this.gameTypeOrdinal = gameTypeOrdinal;
		return this;
	}

	@Override
	public MapGame setMain(MainController main) {
		if (main != null && main != this.main) {
			this.main = main;
			if (this.molmManagerDelegated != null)
				for (AbstractMatrixObjectLocationManager molm : molmManagerDelegated) {
				if (molm != null) molm.forEach(main.getMainSetterForMOLM());
				}
		}
		return this;
	}

	@Override
	public MapGame setMapName(String mapName) {
		this.mapName = mapName;
		return this;
	}

	@Override
	public MapGame setLog(LoggerMessages log) {
		this.log = log = LoggerMessages.loggerOrDefault(log);
		if (this.getMolms() != null && this.getMolms().length > 0)
			for (AbstractMatrixObjectLocationManager molm : this.getMolms()) {
			molm.setLog(log);
			}
		return this;
	}

	@Override
	public ObservableMolm setMolmObservers(List<ObserverMolm> observers) {
		this.molmManagerDelegated.setMolmObservers(observers);
		return this;
	}

	@Override
	public MapGame setMolmManagerDelegated(AbstractMOLMManager molmManagerDelegated) {
		if (molmManagerDelegated != null)
			if (molmManagerDelegated instanceof MOLMManagerObservable)
				this.molmManagerDelegated = (MOLMManagerObservable) molmManagerDelegated;
			else
				throw new IllegalArgumentException(
						"The delegated AbstractMOLMManager must be an instance of MOLMManagerObservable");
		else
			throw new IllegalArgumentException("The delegated AbstractMOLMManager cannot be null");
		return this;
	}

	//

	@Override
	public String doAfterDeserialization(Object arguments) {
		AbstractMapGame.super.doAfterDeserialization(arguments);
		return null;
	}

	//

	// TODO OTHER

	/**
	 * This clears all instances of {@link AbstractMatrixObjectLocationManager}.
	 */

	//

	// MEMENTO

	@Override
	public MementoMapGame createMemento() {
		return new MementoMapGame(this);
	}

	public static class MementoMapGame extends AbstractMementoMapGame {
		private static final long serialVersionUID = 752099091942588L;

		public MementoMapGame() {
			super();
		}

		public MementoMapGame(MapGame mgg) {
			super(mgg);
		}

		/**
		 * Remember to call {@link MapGame#setFrameHolder(FrameHolder)}.
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public Object reinstanceFromMe(FullReloadEnvironment fre) {
			return new MapGame(this, fre);
		}
	}
}