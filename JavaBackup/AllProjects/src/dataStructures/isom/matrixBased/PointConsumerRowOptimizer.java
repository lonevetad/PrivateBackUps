package dataStructures.isom.matrixBased;

import java.awt.geom.Point2D;

import dataStructures.isom.NodeIsom;
import geometry.pointTools.impl.PointConsumerRestartable;

public abstract class PointConsumerRowOptimizer implements PointConsumerRestartable {
	private static final long serialVersionUID = 1L;

	public PointConsumerRowOptimizer(MatrixInSpaceObjectsManager misom) {
		this.misom = misom;
		y = 0;
		rowCache = null;
	}

	protected int y;
	protected NodeIsom[] rowCache;
	protected MatrixInSpaceObjectsManager misom;

//

//

	public int getY() {
		return y;
	}

	public NodeIsom[] getRowCache() {
		return rowCache;
	}

	public MatrixInSpaceObjectsManager getMisom() {
		return misom;
	}

	//

	public PointConsumerRowOptimizer setMisom(MatrixInSpaceObjectsManager misom) {
		this.misom = misom;
		return this;
	}

	//

	public abstract void acceptImpl(Point2D t);

	@Override
	public final void accept(Point2D p) {
		int yy;
		if (p == null)
			return;
		yy = (int) p.getY();
		if (rowCache == null || yy != y)
			rowCache = misom.getRow(y = yy);
		acceptImpl(p);
	}

	@Override
	public void restart() {
		y = 0;
		rowCache = null;
	}
}