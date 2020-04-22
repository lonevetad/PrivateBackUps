package common.abstractCommon.referenceHolderAC;

public interface FrameHolderDelegating extends FrameHolder {

	public FrameHolder getFrameHolder();

	public FrameHolderDelegating setFrameHolder(FrameHolder frameHolder);

	@Override
	public default int getLastFrame() {
		return getFrameHolder().getLastFrame();
	}

	@Override
	public default FrameHolderDelegating setLastFrame(int lastFrame) {
		getFrameHolder().setLastFrame(lastFrame);
		return this;
	}
}
