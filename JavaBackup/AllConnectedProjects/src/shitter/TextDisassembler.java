package shitter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import dataStructures.MapTreeAVL;
import shitter.Shitter.FileDisassemblerHandler;
import shitter.ShitterUtils.TornedApartFileCollector;

public class TextDisassembler extends FileDisassemblerHandler {
	@Override
	public boolean canHandle(File f) {
		return f != null && f.exists() && (!f.isDirectory())//
				&& f.getName().endsWith("txt");
	}

	@Override
	public void disassembleImpl(File f) {
		boolean canContinue;
		byte byteRead;
		int availableByte, piecesCount, counter;
		long seed;
		byte[] buffer;
		IntegerStream indexesStream;
		Iterator<Integer> integerIterator;
		Integer randomNumber;
		BufferedReader br;
//		BufferedInputStream bis;
		BufferedOutputStream bos;
		String nameNoExtension, extension;
		try {

			// use nameNoExtension as a temporary variable
			nameNoExtension = f.getAbsolutePath();
			// use piecesCount to find the index of the extension, if exists
			piecesCount = nameNoExtension.lastIndexOf('.');
			if (piecesCount < 0) {
				// no extension
				extension = "";
			} else {
				extension = nameNoExtension.substring(piecesCount);
				nameNoExtension = nameNoExtension.substring(0, piecesCount);
			}

//			br = new BufferedReader(new FileReader(f), Shitter.MAX_SIZE_BYTE);
			br = new BufferedReader(new FileReader(f), Shitter.MAX_SIZE_BYTE);
			// read all file
//			availableByte = br.lines().mapToInt(s -> s.length()).sum() * 2;// fis.available();
			availableByte = 0;
			while (br.read() != (-1))
				availableByte++;
			br.close();
			br = new BufferedReader(new FileReader(f), Shitter.MAX_SIZE_BYTE);

			piecesCount = ((availableByte % Shitter.MAX_SIZE_BYTE) == 0) ? (availableByte / Shitter.MAX_SIZE_BYTE)
					: ((availableByte / Shitter.MAX_SIZE_BYTE) + 1);

			buffer = new byte[Shitter.MAX_SIZE_BYTE];

			indexesStream = new IntegerStream(piecesCount);
			indexesStream.restart();
			integerIterator = indexesStream.iterator();
			seed = indexesStream.getSeed();

			canContinue = true;
			while (canContinue) {
				counter = Shitter.MAX_SIZE_BYTE;
				if (buffer.length != Shitter.MAX_SIZE_BYTE)
					buffer = new byte[Shitter.MAX_SIZE_BYTE];
				byteRead = -1;
				while ((counter > 0) && (byteRead = (byte) br.read()) != (-1))
					buffer[Shitter.MAX_SIZE_BYTE - counter--] = byteRead;
				canContinue = integerIterator.hasNext() && (counter == 0 || byteRead == (-1));
				randomNumber = integerIterator.next();
				if (counter > 0 && randomNumber != null) {
					// EOF reached: empty the buffer
//					int i = Shitter.MAX_SIZE_BYTE;
//					while (--i >= counter)
//						buffer[i] = 0;
					int i;
					byte[] newBuffer;
					i = -1;
					newBuffer = new byte[randomNumber != 0 ? counter : (counter + 8)];
					while (++i < counter)
						newBuffer[i] = buffer[i];
					buffer = newBuffer;
				}

				if (randomNumber != null) {
					bos = new BufferedOutputStream(new FileOutputStream(
							new File(nameNoExtension + SEPARATOR_NAME_INDEX + randomNumber + extension)));
					if (randomNumber.intValue() == 0) {
						// write the seed
						bos.write(ShitterUtils.splitLongInBytes(seed));
						bos.flush();
					}
					bos.write(buffer);
					bos.flush();
					bos.close();
				}

			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reAssembleImpl(String originalPathAndFilename, File f) {
		byte byteRead;
		int dotIndex, byteLeftToJump, counter;
		long seed;
		byte[] buffer;
		String pathToFile, nameNoExtension, extension;// , pathNameSeparetorNoExtension;
		File folderContaining, fileWithSeed;
		BufferedInputStream bis;
		IntegerStream indexesStream;
		Iterator<Integer> integerIterator;
		Integer randomNumber;
		BufferedOutputStream rebuildingFileStream;
		TornedApartFileCollector fc;
		final MapTreeAVL<Integer, File> pieces;
		pathToFile = f.getAbsolutePath();
		pathToFile = pathToFile.substring(0, pathToFile.lastIndexOf(File.separatorChar) + 1);
		folderContaining = new File(pathToFile);
		if (!(folderContaining.exists() && folderContaining.isDirectory()))
			return;
		{// calculate the file's name
			String tempName;
			dotIndex = (tempName = f.getName()).lastIndexOf('.');
			if (dotIndex >= 0) {
				extension = tempName.substring(dotIndex);// include the dot
				tempName = tempName.substring(0, dotIndex);
			} else {
				extension = ""; // none
			}
			nameNoExtension = tempName;
		}
//		pathNameSeparetorNoExtension = pathToFile + nameNoExtension + SEPARATOR_NAME_INDEX;
		fc = new ShitterUtils.TornedApartFileCollector(this, nameNoExtension, extension);
		folderContaining.listFiles(fc);
		pieces = fc.pieces;
		fileWithSeed = pieces.peekMinimum().getValue();

		if (fileWithSeed == null)
			return;
		rebuildingFileStream = null;
		try {
			buffer = new byte[8];
			bis = new BufferedInputStream(new FileInputStream(fileWithSeed));

			bis.read(buffer);
			bis.close();
			bis = null;
			seed = ShitterUtils.assembleBytesToLong(buffer);
			buffer = new byte[Shitter.MAX_SIZE_BYTE];
			indexesStream = new IntegerStream(pieces.size(), seed);
			indexesStream.restart();
			integerIterator = indexesStream.iterator();
			rebuildingFileStream = new BufferedOutputStream(
					new FileOutputStream(new File(pathToFile + nameNoExtension + " REBUILT" + extension)),
					Shitter.MAX_SIZE_BYTE);
			byteLeftToJump = 0;
			while (integerIterator.hasNext()) {
				randomNumber = integerIterator.next();
//				System.out.println("index: " + randomNumber);
				bis = new BufferedInputStream(new FileInputStream(fileWithSeed = pieces.remove(randomNumber)));
				counter = Shitter.MAX_SIZE_BYTE;
				if (randomNumber == 0) {
//					System.out.println("LOL");
					byteLeftToJump = 8;
				}

				byteRead = -1;
				// fill the buffer
				while ((counter > 0) && (byteRead = (byte) bis.read()) != (-1))
					if (byteLeftToJump == 0)
						buffer[Shitter.MAX_SIZE_BYTE - counter--] = byteRead;
					else
						byteLeftToJump--;
				bis.close();
				bis = null;
				if (counter > 0) {
					// EOF reached: copythe buffer
					int i;
					byte[] newBuffer;
					i = -1;
					newBuffer = new byte[counter];
					while (++i < counter)
						newBuffer[i] = buffer[i];
//					buffer = newBuffer;
//					rebuildingFileStream.
//					System.out.println("LOL con " + randomNumber);
					rebuildingFileStream.flush();
					rebuildingFileStream.write(newBuffer, 0, counter);
					rebuildingFileStream.flush();

				} else {
					rebuildingFileStream.write(buffer);
					rebuildingFileStream.flush();
				}
			}
			if (rebuildingFileStream != null) {
				rebuildingFileStream.close();
				rebuildingFileStream = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}