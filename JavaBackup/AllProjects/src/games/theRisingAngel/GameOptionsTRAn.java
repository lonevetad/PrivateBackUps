package games.theRisingAngel;

import games.generic.controlModel.holders.ProbabilityOfContextesHolders;
import games.generic.controlModel.subimpl.GameOptionsRPG;

public class GameOptionsTRAn extends GameOptionsRPG {
	protected GameOptionsTRAn(GControllerTRAn gameController) {
		super(gameController);
		this.probabilitiesContexes = gameController.getProbabilityOfContextesHolders();
	}

	protected final ProbabilityOfContextesHolders probabilitiesContexes;
	protected int pointsAttributeUpgradeOnLeveling, factorPriceEssenceExtracting;

	//

	public ProbabilityOfContextesHolders getProbabilitiesContexes() { return probabilitiesContexes; }

	public int getPointsAttributeUpgradeOnLeveling() { return pointsAttributeUpgradeOnLeveling; }

	public int getFactorPriceEssenceExtracting() { return factorPriceEssenceExtracting; }

	//

	public void setPointsAttributeUpgradeOnLeveling(int pointsAttributeUpgradeOnLeveling) {
		this.pointsAttributeUpgradeOnLeveling = pointsAttributeUpgradeOnLeveling;
	}

	public void setFactorPriceEssenceExtracting(int factorPriceEssenceExtracting) {
		this.factorPriceEssenceExtracting = factorPriceEssenceExtracting;
	}
}