package games.generic.controlModel.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import games.generic.controlModel.GController;
import tools.LoggerMessages;

/**
 * Class that loads something, usually from file
 */
public abstract class LoaderGeneric {
//	implements ObjectWithID{
//	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @author ottin
	 *
	 */
	public static enum LoadStatusResult {
		Success, CriticalFail, MinorFail;

		public boolean isFailed() { return this.ordinal() != 0; }
	}

	public static final char sc = File.separatorChar;
	public static final String startPath = LoggerMessages.CONSOLE_BASE_PATH + sc + "resources" + sc;
//	public static UniqueIDProvider UIDP_LOADER = null;
//	public static final UIDProviderLoadedListener UIDP_LOAD_LISTENER_LoaderGeneric = uidp -> UIDP_LOADER = uidp;

	static {
//		UIDPCollector.registerForProvider(LoaderGeneric.class, );
		System.out.println("starting path:");
		System.out.println(startPath);
	}

	//

	public abstract LoadStatusResult loadInto(GController gc);

	//

	//

	//

	/**
	 * Reads lines by lines, providing each of them for each invocation of
	 * {@link #next()}.
	 */
	public static class JSONLineReader implements Iterator<String> {
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
		public boolean hasNext() { return line != null && (!"}".equals(line)) && (!"]".equals(line)); }

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

	public abstract static class JSONFileConsumer {
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

		public void readAllFile() {
			String line;// , splitted[];
			while (getLineReader().hasNext()) {
				line = getLineReader().next();
				readAllFileImpl(line);
			}
		}

		protected abstract void readAllFileImpl(String line);

		public JSONLineReader getLineReader() { return lr; }
	}

	//

	//

	// TODO MINOR UTILITIES

	public static String[] trimAll(String[] a) {
		int i;
		i = a.length;
		while (--i >= 0)
			a[i] = a[i].trim();
		return a;
	}

	public static String removeQuotes(String s) {
		int i;
		i = s.indexOf('\"');
		if (i < 0)
			return s;
		return s.substring(i + 1, s.lastIndexOf('\"'));
	}

	public static int extractIntValue(String s) {
		boolean isNeg;
		int i, pow, res, len;
		char c;
		len = s.length();
		pow = 1;
		res = 0;
		isNeg = (c = s.charAt(0)) == '-';
		if (isNeg || c == '+') {
			i = 0;
			while (++i < len && ((c = s.charAt(i)) >= '0') && c <= '9')
				;
			while (--i >= 1) {
				res += (s.charAt(i) - '0') * pow;
				pow *= 10;
			}
			if (isNeg)
				res = -res;
		} else {
			i = -1;
			while (++i < len && ((c = s.charAt(i)) >= '0') && c <= '9')
				;
			while (--i >= 0) {
				res += (s.charAt(i) - '0') * pow;
				pow *= 10;
			}
		}
		return res;
	}
}