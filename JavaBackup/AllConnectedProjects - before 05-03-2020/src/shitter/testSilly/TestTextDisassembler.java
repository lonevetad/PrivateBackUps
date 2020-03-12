package shitter.testSilly;

import java.io.File;

import shitter.TextDisassembler;

public class TestTextDisassembler extends GeneralShittersSillyTest {

	public static void main(String[] args) {

		File fileToRead;
		TextDisassembler td;

		FileAndAbsolutePath faap;
		faap = getAndCreateTestFolder();
		System.out.println(faap.path);
		System.out.println("create default test file");
		createDefaultTestFile(faap.path);

		System.out.println("START");
		td = new TextDisassembler();
		fileToRead = new File(faap.path + "temp.txt");
		System.out.println("file " + fileToRead + " can be handled? " + td.canHandle(fileToRead));
		System.out.println("file's name: " + fileToRead.getName());
		td.disassemble(fileToRead);
		System.out.println("now re-assemble");
		td.reAssemble(fileToRead.getAbsolutePath());
		System.out.println("END");
	}

}
