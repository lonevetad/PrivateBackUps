package common.abstractCommon;

import common.abstractCommon.behaviouralObjectsAC.GameObjectInMapFactory;
import common.abstractCommon.behaviouralObjectsAC.TileImageFactory;
import common.gui.TileImage;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

public interface AbstractEnumElementGameObjectTileImage_Delegating extends AbstractEnumElementGOTI {

	public AbstractEnumElementGOTI getDelegated();

	public AbstractEnumElementGameObjectTileImage_Delegating setDelegated(
			AbstractEnumElementGOTI delegated);

	public static AbstractEnumElementGOTI ndi() {
		return AbstractEnumElementGOTI.newDefaultInstance();
	}

	//

	@Override
	public default boolean isNotSolid() {
		return getDelegated().isNotSolid();
	}

	@Override
	public default Integer getID() {
		return getDelegated().getID();
	}

	@Override
	public default String getName() {
		return getDelegated().getName();
	}

	@Override
	public default TileImageFactory getTileImageFactory() {
		return getDelegated().getTileImageFactory();
	}

	@Override
	public default TileImage getTileImageCached() {
		return getDelegated().getTileImageCached();
	}

	@Override
	public default ShapeSpecification getShapeSpecification() {
		return getDelegated().getShapeSpecification();
	}

	@Override
	public default GameObjectInMapFactory getGameObjectInMapFactory() {
		return getDelegated().getGameObjectInMapFactory();
	}

	//

	@Override
	public default AbstractEnumElementGOTI setNotSolid(boolean isNotSolid) {
		getDelegated().setNotSolid(isNotSolid);
		return this;
	}

	@Override
	public default AbstractEnumElementGOTI setID(Integer iD) {
		getDelegated().setID(iD);
		return this;
	}

	@Override
	public default AbstractEnumElementGOTI setName(String name) {
		getDelegated().setName(name);
		return this;
	}

	@Override
	public default AbstractEnumElementGOTI setTileImageFactory(TileImageFactory tileImageFactory) {
		getDelegated().setTileImageFactory(tileImageFactory);
		return this;
	}

	@Override
	public default AbstractEnumElementGOTI setTileImageCached(TileImage tileImageCached) {
		getDelegated().setTileImageCached(tileImageCached);
		return this;
	}

	@Override
	public default TileImage newTileImage(MainController main) {
		return getDelegated().newTileImage(main);
	}

	@Override
	public default AbstractEnumElementGOTI setShapeSpecification(ShapeSpecification shapeSpecification) {
		getDelegated().setShapeSpecification(shapeSpecification);
		return this;
	}

	@Override
	public default AbstractEnumElementGOTI setGameObjectInMapFactory(
			GameObjectInMapFactory gameObjectInMapFactory) {
		getDelegated().setGameObjectInMapFactory(gameObjectInMapFactory);
		return this;
	}

}