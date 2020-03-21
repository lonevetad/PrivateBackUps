package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.UniqueIDProvider;
import games.generic.controlModel.GModality;
import games.generic.controlModel.subImpl.TimedObjectSimpleImpl;

public class PrinterTO implements TimedObjectSimpleImpl {
	long timeThreshold, accumulatedTimeElapsed;
	String text;
	Integer ID;

	public PrinterTO(long timeThreshold, String text) {
		super();
		this.ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID();
		this.timeThreshold = timeThreshold;
		this.text = text;
		this.accumulatedTimeElapsed = 0;
		System.out.println("timeThreshold: " + timeThreshold + " for " + text);
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public long getAccumulatedTimeElapsed() {
		return accumulatedTimeElapsed;
	}

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) {
		this.accumulatedTimeElapsed = newAccumulated;
	}

	@Override
	public long getTimeThreshold() {
		return timeThreshold;
	}

	@Override
	public void executeAction(GModality modality) {
		System.out.println(text);
	}
}