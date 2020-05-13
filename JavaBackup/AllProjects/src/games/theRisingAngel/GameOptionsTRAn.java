package games.theRisingAngel;

import games.generic.GameOptions;
import games.generic.controlModel.misc.LoaderGeneric.JSONFileConsumer;
import tools.minorTools.RandomWeightedIndexes;

public class GameOptionsTRAn extends GameOptions {
	protected RandomWeightedIndexes equipWeights;
	protected JSONFileConsumer jsonReader;
//	JSONLineReader lr;

	protected GameOptionsTRAn() {
		jsonReader = new JSONFileConsumer("", "configurationsTRAr.json") {

			@Override
			protected void readAllFileImpl(String line) {
				readAllFile_GOT(line);
			}
		};
	}

	protected void readAllFile_GOT(String line) {
		String splitted[];
		System.out.println("read line: " + line);
		if (line.contains("equipItemsRarities")) {
			int[] weights;
			// recycle "line"
			line = line.substring(line.indexOf(':') + 1).trim();
			// line should be of form "[1,28,-3]"
			splitted = line.substring(1, line.length() - 1).split(","); // remove square pranthesis and split
			weights = new int[splitted.length];
			for (int i = 0, n = splitted.length; i < n; i++) {
				weights[i] = Integer.parseInt(splitted[i].trim());
			}
			equipWeights = new RandomWeightedIndexes(weights);
		} // else .. other options
	}

	@Override
	public void loadConfig() {
		jsonReader.readAllFile();
	}
}