package videogamesOldVersion.common;

import videogamesOldVersion.common.abstractCommon.AbstractEnumElementGOTI;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.GameObjectInMapFactory;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.TileImageFactory;
import videogamesOldVersion.common.gui.TileImage;

/***/
public class EnumElementTileImageFactory implements AbstractEnumElementGOTI {
	private static final long serialVersionUID = -70320878892333L;

	public EnumElementTileImageFactory() {
		setTileImageFactory(null);
		isNotSolid = true;
	}

	boolean isNotSolid;
	Integer ID;
	String name;
	TileImageFactory tileImageFactory;
	TileImage tileImageCached;
	GameObjectInMapFactory gameObjectInMapFactory;
	ShapeSpecification shapeSpecification;

	//

	// TODO GETTER

	@Override
	public boolean isNotSolid() {
		return isNotSolid;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ShapeSpecification getShapeSpecification() {
		return shapeSpecification;
	}

	@Override
	public TileImageFactory getTileImageFactory() {
		return tileImageFactory;
	}

	@Override
	public TileImage getTileImageCached() {
		return tileImageCached;
	}

	@Override
	public GameObjectInMapFactory getGameObjectInMapFactory() {
		return gameObjectInMapFactory;
	}

	//

	// TODO SETTER

	@Override
	public AbstractEnumElementGOTI setNotSolid(boolean isNotSolid) {
		this.isNotSolid = isNotSolid;
		return this;
	}

	@Override
	public AbstractEnumElementGOTI setID(Integer iD) {
		ID = iD;
		return this;
	}

	@Override
	public AbstractEnumElementGOTI setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public AbstractEnumElementGOTI setShapeSpecification(ShapeSpecification shapeSpecification) {
		this.shapeSpecification = shapeSpecification;
		return this;
	}

	@Override
	public AbstractEnumElementGOTI setTileImageFactory(TileImageFactory tileImageFactory) {
		this.tileImageFactory = TileImageFactory.getOrDefault(tileImageFactory);
		return this;
	}

	@Override
	public AbstractEnumElementGOTI setTileImageCached(TileImage tileImageCached) {
		this.tileImageCached = tileImageCached;
		return this;
	}

	@Override
	public AbstractEnumElementGOTI setGameObjectInMapFactory(GameObjectInMapFactory gameObjectInMapFactory) {
		this.gameObjectInMapFactory = GameObjectInMapFactory.getOrDefault(gameObjectInMapFactory);
		return this;
	}

	// TODO to do
}