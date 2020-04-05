package videogamesOldVersion.common;

import videogamesOldVersion.common.abstractCommon.MainController;
import videogamesOldVersion.common.abstractCommon.Memento;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectShaped;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectShaped.MementoOShaped;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

public class GameObjectInMap extends ObjectShaped
// implements ObjectActingOnPassingTime
{
	private static final long serialVersionUID = 26065500850988089L;
	/** See{@link #resetProgressiveID()}. */
	// public static int GAME_OBJECT_PROGRESSIVE_ID = 0;

	/** {@inheritDoc} */
	public GameObjectInMap() {
		super();
	}

	public GameObjectInMap(ShapeSpecification ss) {
		super(ss);
	}

	public GameObjectInMap(GameObjectInMap o) {
		super(o);
		this.name = o.name;
		// this.setShapeSpecification(o.getShapeSpecification().clone());
		this.idEnumElement = o.idEnumElement;
		this.gameObjectID = o.gameObjectID;
	}

	public GameObjectInMap(MementoGameObjectInMap o) {
		super(o);
		// throw new NotImplementedException();// "Too lazy for memento"
	}

	// { gameObjectID = GAME_OBJECT_PROGRESSIVE_ID++;}

	MainController main;
	Integer gameObjectID, idEnumElement;
	String name;
	ShapeSpecification shapeSpecification;

	//

	// TODO GETTER

	public String getName() {
		return name;
	}

	/**
	 * ID used for the Game to identify an object inside the Model.<br>
	 * Could be set any time and should start from 0, especially during multiplayer
	 * match.
	 */
	public Integer getGameObjectID() {
		return gameObjectID;
	}

	/**
	 * Every instance of this object comes from a enumeration, as like as it
	 * graphical component (part of the View). <br>
	 * This ID must be used for this purpose.
	 */
	public Integer getIdEnumElement() {
		return idEnumElement;
	}

	@Override
	public ShapeSpecification getShapeSpecification() {
		return shapeSpecification;
	}

	public MainController getMain() {
		return main;
	}

	//

	// TODO SETTER

	/** See {@link #getGameObjectID()}. */
	public GameObjectInMap setGameObjectID(Integer gameObjectID) {
		this.gameObjectID = gameObjectID;
		return this;
	}

	/** See {@link #getIdEnumElement()}. */
	public GameObjectInMap setIdEnumElement(Integer idEnumElement) {
		this.idEnumElement = idEnumElement;
		return this;
	}

	public GameObjectInMap setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public GameObjectInMap setShapeSpecification(ShapeSpecification shapeSpecification) {
		this.shapeSpecification = shapeSpecification;
		return this;
	}

	public GameObjectInMap setMain(MainController main) {
		this.main = main;
		return this;
	}

	//

	// TODO OTHER

	/*
	 * public static void resetProgressiveID() { GAME_OBJECT_PROGRESSIVE_ID = 0; }
	 */

	//

	// TODO MEMENTO

	@Override
	public MementoGameObjectInMap createMemento() {
		return new MementoGameObjectInMap(this);
	}

	@Override
	public boolean reloadState(Memento m) {
		MementoGameObjectInMap mgoim;
		if (m != null && m instanceof MementoGameObjectInMap) {
			mgoim = (MementoGameObjectInMap) m;
			super.reloadState(mgoim);
			this.name = mgoim.name;
			this.idEnumElement = mgoim.idEnumElement;
			this.gameObjectID = mgoim.gameObjectID;
			return true;
		}
		return false;
	}

	//

	// TODO CLASS

	public static class MementoGameObjectInMap extends MementoOShaped {
		private static final long serialVersionUID = 90342310L;
		Integer gameObjectID, idEnumElement;
		String name;
		// ShapeSpecification shapeSpecification;

		public MementoGameObjectInMap() {
			super();
		}

		public MementoGameObjectInMap(GameObjectInMap o) {
			super(o);
			this.name = o.name;
			this.idEnumElement = o.idEnumElement;
			this.gameObjectID = o.gameObjectID;
		}

		@Override
		public Object reinstanceFromMe() {
			return new GameObjectInMap(this);
		}
	}
}