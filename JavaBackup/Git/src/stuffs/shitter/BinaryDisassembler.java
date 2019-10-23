package stuffs.shitter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import stuffs.shitter.Shitter.FileDisassemblerHandler;

public class BinaryDisassembler extends FileDisassemblerHandler {
	@Override
	public boolean canHandle(File f) {
		return f != null && f.exists() && (!f.isDirectory());
	}

	@Override
	public void disassembleImpl(File f) {
		int availableByte, piecesCount;
		long seed;
		byte[] buffer;
		IntegerStream indexesStream;
		Iterator<Integer> integerIterator;
		Integer randomNumber;
//		BufferedInputStream bis;
		BufferedOutputStream bos;
		String nameNoExtention, extention;
		try {
			FileInputStream fis;

			// use nameNoExtention as a temporary variable
			nameNoExtention = f.getAbsolutePath();
			// use piecesCount to find the index of the extention, if exists
			piecesCount = nameNoExtention.lastIndexOf('.');
			if (piecesCount < 0) {
				// no extention
				extention = "";
			} else {
				extention = nameNoExtention.substring(piecesCount);
				nameNoExtention = nameNoExtention.substring(0, piecesCount);
			}

			fis = new FileInputStream(f);
			availableByte = fis.available();
			piecesCount = ((availableByte % Shitter.MAX_SIZE_BYTE) == 0) ? (availableByte / Shitter.MAX_SIZE_BYTE)
					: ((availableByte / Shitter.MAX_SIZE_BYTE) + 1);
			buffer = new byte[Shitter.MAX_SIZE_BYTE];

			indexesStream = new IntegerStream(piecesCount);
			indexesStream.restart();
			integerIterator = indexesStream.iterator();
			seed = indexesStream.getSeed();
			while (fis.read(buffer) > 0) {
				randomNumber = integerIterator.next();
				bos = new BufferedOutputStream(
						new FileOutputStream(new File(nameNoExtention + " - " + randomNumber + extention)));
				if (randomNumber.intValue() == 0)// write the seed
					bos.write(ShitterUtils.splitLongInBytes(seed));
				bos.write(buffer);
				bos.flush();
				bos.close();
			}

			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reAssembleImpl(String originalPathAndFilename, File f) {

	}
}