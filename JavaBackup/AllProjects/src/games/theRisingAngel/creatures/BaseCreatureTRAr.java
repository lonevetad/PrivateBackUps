package games.theRisingAngel.creatures;

import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.theRisingAngel.AttributesTRAr;

public interface BaseCreatureTRAr extends CreatureOfRPGs {

	@Override
	public default int getLifeMax() {
		return this.getAttributes().getValue(AttributesTRAr.LifeMax.getIndex());
	}

	@Override
	public default int getLifeRegenation() {
		return this.getAttributes().getValue(AttributesTRAr.RigenLife.getIndex());
	}

	//

	@Override
	public default void setLifeMax(int lifeMax) {
		if (lifeMax > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAr.LifeMax.getIndex(), lifeMax);
			if (this.getLife() > lifeMax)
				this.setLife(lifeMax);
		}
	}

	@Override
	public default void setLifeRegenation(int lifeRegenation) {
		if (lifeRegenation > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAr.RigenLife.getIndex(), lifeRegenation);
		}
	}
}