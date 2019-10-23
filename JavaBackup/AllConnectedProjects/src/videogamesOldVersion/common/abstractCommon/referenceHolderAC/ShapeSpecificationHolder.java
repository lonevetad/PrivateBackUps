package common.abstractCommon.referenceHolderAC;

import java.io.Serializable;

import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

public interface ShapeSpecificationHolder extends Serializable {

	public ShapeSpecification getShapeSpecification();

	public ShapeSpecificationHolder setShapeSpecification(ShapeSpecification shapeSpecification);
}