package shitter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import dataStructures.MapTreeAVL;

public class ShitterUtils {
	/**
	 * Bits more significant will be put in lower indexes, so the sign's bit will be
	 * put at index zero.
	 */
	public static byte[] splitLongInBytes(long num) {
		byte[] r;
		r = new byte[8];
		for (int i = 0, x = 7; x >= 0; x--) {
			r[i++] = (byte) (num >>>
			// number of bits to shift
			(x << 3));
		}
		return r;
	}

	/** Bytes in lower indexes will be places in "higher" bits */
	public static long assembleBytesToLong(byte[] a) {
//		byte c;
		int i = -1;
		long num;
		if (a == null || a.length < 8)
			return 0;
		num = 0;
		while (++i < 8)
			num = (num << 8) + (a[i] & 0xff);
		return num;
	}

	public static void main(String args) {
		long l;
		byte[] a, r;
		a = new byte[] { 0, 0, 1, 119, -82, 5, -7, 10 };
		System.out.println(Arrays.toString(a));
		l = assembleBytesToLong(a);
		System.out.println("l: " + l);
		System.out.println("\n\n\n\n ###############################à \n\n\n");
		r = splitLongInBytes(l);
		System.out.println(Arrays.toString(r));
		l = assembleBytesToLong(r);
		System.out.println("l_2.0: " + l);
	}

	public static File findFileWithSeed(File[] shittedFiles) {
		int dotIndex;
		File f;
		String fileName;
		f = shittedFiles[0];
		fileName = f.getName();
		dotIndex = fileName.lastIndexOf('.');
		return findFileWithSeed((dotIndex < 0 ? "" : fileName.substring(dotIndex)), shittedFiles);
	}

	/**
	 * Find and return the file having the seed to re-assemble the torned apart
	 * files, providing the file's extension for performance reason
	 */
	public static File findFileWithSeed(String extension, File[] shittedFiles) {
		int len;
		File f;
		String targetEnding;
		len = shittedFiles.length;
		targetEnding = Shitter.FileDisassemblerHandler.SEPARATOR_NAME_INDEX + extension;
		while (--len >= 0) {
			f = shittedFiles[len];
			if (f != null && f.getName().endsWith(targetEnding))
				return f;
		}
		return null;
	}

	//

	public static class TornedApartFileCollector implements FilenameFilter {
		final public MapTreeAVL<Integer, File> pieces;
		final public TextDisassembler td;
		final String nameSeparator, extension;

		public TornedApartFileCollector(TextDisassembler td, String nameNoExtension, String extension) {
			this.td = td;
			this.nameSeparator = nameNoExtension + Shitter.FileDisassemblerHandler.SEPARATOR_NAME_INDEX;
			this.extension = extension;
			pieces = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Integer::compare);
//			System.out.println("nameNoExtension: " + nameNoExtension + ", extension: " + extension);
		}

		@Override
		public boolean accept(File containedFile, String thatFileName) {
			boolean b;
			File thisFile;
			thisFile = new File(containedFile + File.separator + thatFileName);
			b = td.canHandle(thisFile) && thatFileName.endsWith(extension) && thatFileName.contains(nameSeparator);
			if (b) {
				int i, numAssociated;
				i = thatFileName.lastIndexOf(Shitter.FileDisassemblerHandler.SEPARATOR_NAME_INDEX)
						+ Shitter.FileDisassemblerHandler.SEPARATOR_NAME_INDEX.length();
				numAssociated = Integer.parseInt(thatFileName.substring(i, thatFileName.lastIndexOf(extension)));
				pieces.put(numAssociated, thisFile);
			}
			return b;
		}

		public File getFileWithSeed() {
			return this.pieces.peekMinimum().getValue();
		}
	}
}