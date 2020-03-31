package common.abstractCommon;

import java.io.Serializable;

/** The Object class holding all data to be saved. */
public abstract class AbstractSaveData implements Serializable {

	private static final long serialVersionUID = 1111111111111111101L;// lol

	public AbstractSaveData() {
		saveTime = System.currentTimeMillis();
	}

	public AbstractSaveData(GameModelGeneric gameModel) {
		this();
		mementoGameModelGeneric = gameModel.createMemento();
	}

	protected final long saveTime;
	String saveName;
	// for now, let's do this
	GameModelGeneric.MementoGameModelGeneric mementoGameModelGeneric;

	//

	// TODO GETTER

	public long getSaveTime() {
		return saveTime;
	}

	public String getSaveName() {
		return saveName;
	}

	//

	// TODO SETTER

	public AbstractSaveData setSaveName(String saveName) {
		this.saveName = saveName;
		return this;
	}

	///

	//

	public GameModelGeneric createGameModel() {
		return mementoGameModelGeneric.reinstanceFromMe();
	}
}