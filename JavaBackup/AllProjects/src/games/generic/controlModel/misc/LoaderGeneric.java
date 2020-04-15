package games.generic.controlModel.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Iterator;

import games.generic.controlModel.GController;

public abstract class LoaderGeneric {

	public static final char sc = File.separatorChar;
	public static final String startPath;

	static {
		String s;
		s = FileSystems.getDefault().getPath(".").toAbsolutePath().toString();
		startPath = s.substring(0, s.length() - 2) + sc + "resources" + sc;
		System.out.println("starting path:");
		System.out.println(startPath);
	}

	//

	public abstract void loadInto(GController gc);

	//

	//

	//

	/** Just read lines by lines */
	protected static class JSONLineReader implements Iterator<String> {
		String path, filename, line;
		BufferedReader reader;

		public JSONLineReader(String path, String filename) throws IOException {
			super();
			this.path = path;
			this.filename = filename;
			reader = new BufferedReader(new FileReader(path + sc + filename));
			line = reader.readLine(); // read the '{' or '[' at the start
		}

		@Override
		public boolean hasNext() {
			return line != null && (!"}".equals(line)) && (!"]".equals(line));
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

	protected abstract static class JSONFileConsumer {
		protected JSONLineReader lr;

		/**
		 * The first string "subPath" must succeeds after {@link startPath} is setted
		 * and could be a blank string.
		 */
		protected JSONFileConsumer(String subPath, String filename) {
			try {
				lr = new JSONLineReader(startPath, subPath + filename); // "configurationsTRAr.json");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		protected void readAllFile() {
			String line;// , splitted[];
			while(lr.hasNext()) {
				line = lr.next();
				readAllFileImpl(line);
			}
		}

		protected abstract void readAllFileImpl(String line);
	}
}