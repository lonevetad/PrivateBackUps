package common.mainTools.mOLM.abstractClassesMOLM;

import common.mainTools.mOLM.NodeMatrix;

public interface DoSomethingWithNode extends DoSomethingWithItem {

	public default Object doOnNode(AbstractMatrixObjectLocationManager molm, NodeMatrix node, int x, int y) {
		return doOnItem(molm, node.getItem(), x, y);
	}
}