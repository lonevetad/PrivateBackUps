package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.controlModel.GModality;
import games.generic.controlModel.subimpl.TimedObjectSimpleImpl;
import tools.UniqueIDProvider;

/*Simply prints some text*/
public class ObjPrinterTO implements TimedObjectSimpleImpl {
	private static final long serialVersionUID = 1L;
	long timeThreshold, accumulatedTimeElapsed;
	String text;
	Integer ID;

	public ObjPrinterTO(long timeThreshold, String text) {
		super();
		this.ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID();
		this.timeThreshold = timeThreshold;
		this.text = text;
		this.accumulatedTimeElapsed = 0;
	}

	@Override
	public Integer getID() { return ID; }

	public String getText() { return text; }

	public void setText(String text) { this.text = text; }

	@Override
	public long getAccumulatedTimeElapsed() { return accumulatedTimeElapsed; }

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) { this.accumulatedTimeElapsed = newAccumulated; }

	@Override
	public long getTimeThreshold() { return timeThreshold; }

	@Override
	public void executeAction(GModality modality) { System.out.println(text); }

	@Override
	public void onAddedToGame(GModality gm) {}

	@Override
	public void onRemovedFromGame(GModality gm) {}

	@Override
	public String getName() { return "PRINTEEEEEEEEEEEEEEEEEEER"; }
}