package shitter.testSilly;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;

public abstract class GeneralShittersSillyTest {

	public static FileAndAbsolutePath getAndCreateTestFolder() {
		FileAndAbsolutePath faap;
		faap = new FileAndAbsolutePath();
		faap.path = FileSystems.getDefault().getPath(".").toFile().getAbsolutePath();
		faap.path = faap.path.substring(0, faap.path.length() - 1)// remove the dot
				+ "test" + File.separatorChar;
		faap.folder = new File(faap.path);
		if (!faap.folder.exists())
			faap.folder.mkdirs();
		return faap;
	}

	public static void createDefaultTestFile(String pathFolder) {
		createDefaultTestFile(pathFolder, "txt");
	}

	public static void createDefaultTestFile(String pathFolder, String extension) {
		BufferedOutputStream bos;
		int len, minimumByteValue, amountBufferRepetition;
		byte[] buffer;
// create a simple file, having silly characters to output
// byte, i.e. chars, range from minimumByteValue to (minimumByteValue+len)
		minimumByteValue = 31;
		amountBufferRepetition = 5;// do not make the file too large
		try {
			len = 127 - minimumByteValue;
			bos = new BufferedOutputStream(new FileOutputStream(pathFolder + "temp." + extension), len);
			buffer = new byte[len];
			for (int i = 0; i < len; i++)
				buffer[i] = (byte) minimumByteValue++;
			while (amountBufferRepetition-- > 0) {
				bos.write(buffer);
				bos.flush();
			}
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//

	//

	static class FileAndAbsolutePath {
		String path;
		File folder;
	}

	//

	public static void main(String[] args) {
		FileAndAbsolutePath faap;
		faap = getAndCreateTestFolder();
		System.out.println(faap.path);
		System.out.println("create default test file");
		createDefaultTestFile(faap.path);
		System.out.println("End");
	}
}