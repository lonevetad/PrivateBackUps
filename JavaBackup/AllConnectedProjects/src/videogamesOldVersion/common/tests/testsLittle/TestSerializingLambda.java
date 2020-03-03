package common.tests.testsLittle;

import java.io.Serializable;
import java.util.Scanner;

import common.abstractCommon.LoaderGeneric;
import common.mainTools.FileUtilities;
import common.tests.LoaderTests;

public class TestSerializingLambda {

	public TestSerializingLambda() {
	}

	public static interface StringGetter extends Serializable {
		public String getString();
	}

	public static interface IntManipulator extends Serializable {
		public int manipulate(int i);
	}

	public static class StringGetterDelegator implements StringGetter {
		private static final long serialVersionUID = -777045565230891161L;
		StringGetter sg;
		double d;

		public StringGetterDelegator(double d, StringGetter sg) {
			this.sg = sg;
			this.d = d;
		}

		@Override
		public String getString() {
			return sg.getString() + " : " + d;
		}
	}

	public static StringGetter boundInt(int i) {
		int x;
		IntManipulator im;
		x = i;
		im = (v) -> 1000 + v;
		return () -> {
			return "value: " + im.manipulate(x);
		};
	}

	public static void writeStringGetterOnFile(StringGetter sg, String pathFolder, String filename, String extension) {
		String error, path;

		path = pathFolder + filename + extension;
		System.out.println("StringGetter got: " + sg.getString());
		error = FileUtilities.writeObject(sg, path);
		if (error != null)
			System.out.println(error);
		else
			System.out.println("wrote at: " + path + "\n the StringGetter giving " + sg.getString());
	}

	public static void readFile(String pathFolder, String filename, String extension) {
		StringGetter sg;
		String path;
		Object o;
		System.out.println("reading file " + filename);

		path = pathFolder + filename + extension;
		o = FileUtilities.readObject(path);
		if (o != null) {
			if (o instanceof StringGetter) {
				sg = (StringGetter) o;
				System.out.println("eh eh:  " + sg.getString());
			} else
				System.out.println("read object of strange class: " + o.getClass().getName());
		} else
			System.out.println("read object null");
	}

	public static void main(String[] args) {
		StringGetter sgd, sgManipInt;
		LoaderGeneric lg;
		String error, path;
		Scanner scan;

		scan = new Scanner(System.in);

		lg = new LoaderTests();
		path = lg.getPath();
		System.out.println("let's start test with path:\n\t " + path);
		System.out.println("Write \'y\' to create the file, anything else to read");

		error = scan.nextLine();

		if ("y".equals(error)) {

			System.out.println("writing file");
			sgd = new StringGetterDelegator(Math.random(), () -> "puzzi tanto ");
			writeStringGetterOnFile(sgd, path, StringGetterDelegator.class.getSimpleName(), lg.getSavesExtension());

			System.out.println("Insert an int:");
			sgManipInt = boundInt(scan.nextInt());
			writeStringGetterOnFile(sgManipInt, path, "Int bounded", lg.getSavesExtension());

		} else {

			readFile(path, StringGetterDelegator.class.getSimpleName(), lg.getSavesExtension());
			readFile(path, "Int bounded", lg.getSavesExtension());
		}
		scan.close();
	}

}
