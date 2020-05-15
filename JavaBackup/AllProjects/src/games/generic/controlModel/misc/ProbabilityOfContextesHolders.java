package games.generic.controlModel.misc;

import java.util.Map;

import dataStructures.MapTreeAVL;
import tools.Comparators;
import tools.minorTools.RandomWeightedIndexes;

public class ProbabilityOfContextesHolders {

	public ProbabilityOfContextesHolders() {
		this.weightedIndexesContextes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				Comparators.STRING_COMPARATOR);
	}

	protected Map<String, RandomWeightedIndexes> weightedIndexesContextes;

	public Map<String, RandomWeightedIndexes> getWeightedIndexesContextes() { return weightedIndexesContextes; }

	public void addWeightedIndexesContext(String name, RandomWeightedIndexes weightedIndexes) {
		this.weightedIndexesContextes.put(name, weightedIndexes);
	}

	public RandomWeightedIndexes getWeightedIndexesContext(String name) { return weightedIndexesContextes.get(name); }
}