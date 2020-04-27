package games.theRisingAngel;

import games.generic.controlModel.GController;
import games.generic.controlModel.misc.LoaderGeneric;
import tools.minorTools.RandomWeightedIndexes;

/**
 * should use Maven:
 * 
 * <pre>
 * <code>
 * < !-- https://mvnrepository.com/artifact/org.json/json -- >
	< dependency>
	    < groupId>org.json< /groupId>
	    < artifactId>json< /artifactId>
	    < version>20190722< /version>
	< /dependency>
 * </code>
 * </pre>
 * 
 */
public class LoaderConfigurations extends LoaderGeneric {

	public LoaderConfigurations() {
	}

	@Override
	public void loadInto(GController gc) {
		GControllerTRAn gcTrar;
		GameObjectsProvidersHolderTRAn gophTrar;
		GameOptions go;
//		JSONParser jsonReader;
		gcTrar = (GControllerTRAn) gc;
		gophTrar = (GameObjectsProvidersHolderTRAn) gcTrar.getGameObjectsProvider();
//		jsonReader=new JSONParser(source, global, dualFields)
		go = new GameOptions();
		go.loadConfig();
		gophTrar.setEquipItemsWeights(go.equipWeights);
		System.out.println("Loader Configurations loaded: ");
		System.out.println(go.equipWeights);

	}

	protected static class GameOptions extends JSONFileConsumer {
		protected RandomWeightedIndexes equipWeights;
//		JSONLineReader lr;

		protected GameOptions() {
			super("", "configurationsTRAr.json");
		}

		@Override
		protected void readAllFileImpl(String line) {
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

		protected void loadConfig() {
			super.readAllFile();
		}
	}

	public static void main(String[] args) {
		GameOptions go;
		System.out.println("CIAO");
		go = new GameOptions();
		go.loadConfig();
		System.out.println("Loader Configurations loaded: ");
		System.out.println(go.equipWeights);
	}
}