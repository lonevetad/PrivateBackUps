package common.abstractCommon.shapedObject;

import java.io.Serializable;

public interface AbstractObjectOnCartesianPlan extends Serializable {

	// getter

	public int getXLeftBottom();

	public int getYLeftBottom();

	public default int getXCenter() {
		return getXLeftBottom();
	}

	public default int getYCenter() {
		return getYLeftBottom();
	}

	// setter

	public AbstractObjectOnCartesianPlan setXLeftBottom(int x);

	public AbstractObjectOnCartesianPlan setYLeftBottom(int y);

	public default AbstractObjectOnCartesianPlan setLeftBottomCorner(int x, int y) {
		setXLeftBottom(x);
		setYLeftBottom(y);
		return this;
	}

	public default AbstractObjectOnCartesianPlan setXCenter(int x) {
		return setXLeftBottom(x);
	}

	public default AbstractObjectOnCartesianPlan setYCenter(int y) {
		return setYLeftBottom(y);
	}

	public default AbstractObjectOnCartesianPlan setCenter(int x, int y) {
		setXCenter(x);
		setYCenter(y);
		return this;
	}
}