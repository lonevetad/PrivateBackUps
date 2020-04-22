package common.mainTools.mOLM.abstractClassesMOLM;

public interface DoNothingWithItem extends DoSomethingWithItem {

	@Override
	public default Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int x, int y) {
		return null;
	}
}