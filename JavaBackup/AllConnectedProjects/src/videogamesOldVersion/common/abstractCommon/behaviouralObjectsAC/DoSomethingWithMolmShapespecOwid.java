package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

/**
 * Just copying the signature of
 * {@link MatrixObjectLocationManager#addOnShape(ShapeSpecification, ObjectWithID)} (a.k.a. "molm")
 * and {@link MatrixObjectLocationManager#removeOnShape(ShapeSpecification, ObjectWithID)}, but
 * adding the molm's pointer.
 */
public interface DoSomethingWithMolmShapespecOwid extends Serializable {

	/**
	 * Just do something, like
	 * {@link AbstractMatrixObjectLocationManager#addOnShape(ShapeSpecification, ObjectWithID, boolean)}.
	 */
	public String doOnMolmShapespecOwid(AbstractMatrixObjectLocationManager molm, ShapeSpecification ss,
			ObjectWithID owid, boolean removePrevIfShapeSpecHolder);

	// DEFAULTs

	public default String doOnMolmShapespecOwid(AbstractMatrixObjectLocationManager molm, ShapeSpecification ss,
			ObjectWithID owid) {
		return doOnMolmShapespecOwid(molm, ss, owid, false);
	}

	public default DoSomethingWithMolmShapespecOwid andThen(DoSomethingWithMolmShapespecOwid d) {
		return (molm, ss, owid, removePrevIfShapeSpecHolder) -> {
			String s, s2;
			s = doOnMolmShapespecOwid(molm, ss, owid, removePrevIfShapeSpecHolder);
			s2 = d.doOnMolmShapespecOwid(molm, ss, owid, removePrevIfShapeSpecHolder);
			if (s == null)
				s = s2;
			else if (s2 != null) s = s + '\n' + s2;
			return s;
		};
	}

	public final static DoSomethingWithMolmShapespecOwid //
	ADDER = (molm, ss, owID, removePrevIfShapeSpecHolder) -> molm.addOnShape(ss, owID, removePrevIfShapeSpecHolder)//
			, REMOVER = (molm, ss, owID, removePrevIfShapeSpecHolder) -> molm.removeOnShape(ss, owID) //
			, ADDER_REMOVING_PREVIOUS = new DoSomethingWithMolmShapespecOwid() {
				private static final long serialVersionUID = -7840845321035024189L;

				@Override
				public String doOnMolmShapespecOwid(AbstractMatrixObjectLocationManager molm, ShapeSpecification ss,
						ObjectWithID owid) {
					return doOnMolmShapespecOwid(molm, ss, owid, true);
				}

				@Override
				public String doOnMolmShapespecOwid(AbstractMatrixObjectLocationManager molm, ShapeSpecification ss,
						ObjectWithID owid, boolean removePrevIfShapeSpecHolder) {
					return ADDER.doOnMolmShapespecOwid(molm, ss, owid, removePrevIfShapeSpecHolder);
				}
			}//
			, CLEANER = (molm, ss, owID, removePrevIfShapeSpecHolder) -> {
				molm.clearMatrix();
				return null;
			};
}