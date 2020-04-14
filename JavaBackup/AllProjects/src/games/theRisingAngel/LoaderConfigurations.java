package games.theRisingAngel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Iterator;

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
public class LoaderConfigurations implements LoaderGeneric {
	public static final char sc = File.separatorChar;
	public static final String startPath;

	static {
		String s;
		s = FileSystems.getDefault().getPath(".").toAbsolutePath().toString();
		startPath = s.substring(0, s.length() - 2) + sc + "resources" + sc;
		System.out.println("starting path:");
		System.out.println(startPath);
	}

	public LoaderConfigurations() {
	}

	@Override
	public void loadInto(GController gc) {
		GControllerTRAr gcTrar;
		GameObjectsProvidersHolderTRAr gophTrar;
		GameOptions go;
//		JSONParser jsonReader;
		gcTrar = (GControllerTRAr) gc;
		gophTrar = (GameObjectsProvidersHolderTRAr) gcTrar.getGameObjectsManagerProvider();
//		jsonReader=new JSONParser(source, global, dualFields)
		go = new GameOptions();
		go.loadConfig();
		gophTrar.setEquipItemsWeights(go.equipWeights);
		System.out.println("Loader Configurations loaded: ");
		System.out.println(go.equipWeights);

	}

	protected static class GameOptions {
		RandomWeightedIndexes equipWeights;
		JSONLineReader lr;

		protected GameOptions() {
			try {
				lr = new JSONLineReader(startPath, "configurationsTRAr.json");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		void loadConfig() {
			String line, splitted[];
			while(lr.hasNext()) {
				line = lr.next();
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
		}
	}

	/** Just read lines by lines */
	protected static class JSONLineReader implements Iterator<String> {
		String path, filename, line;
		BufferedReader reader;

		public JSONLineReader(String path, String filename) throws IOException {
			super();
			this.path = path;
			this.filename = filename;
			reader = new BufferedReader(new FileReader(path + sc + filename));
			line = reader.readLine(); // read the "{" at the start
		}

		@Override
		public boolean hasNext() {
			return line != null && !"}".equals(line);
		}

		@Override
		public String next() {
			try {
				return line = reader.readLine().trim();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
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