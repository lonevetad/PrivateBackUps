package javaMechanism;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import shared.ImageUtils;

public class TestRobot {

	static final String pathTestFolder = File.separatorChar + "resources" + File.separatorChar + "img"
			+ File.separatorChar + "RobotTests" + File.separatorChar//
			, pathCurrent, pathTotal;

	static {
		File folderTotal;
		pathCurrent = System.getProperty("user.dir");
		pathTotal = pathCurrent + pathTestFolder;
		System.out.println("current dir = " + pathCurrent);
		System.out.println("pathTotal : " + pathTotal);

		folderTotal = new File(pathTotal);
		if (!folderTotal.exists()) {
			System.out.println(//
					"Folder total " + //
							(folderTotal.mkdirs() ? "created" : " not created due to some error"));
		}
	}

	public interface TestInstance {
		public void performTest(Robot r);
	}

	public TestRobot() {
	}

	public static Robot newRobot() {
		Robot r;
		r = null;
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return r;
	}

	public static void main(String[] args) {
		Robot r;
		TestInstance[] tests;

		System.out.println("START");
		r = newRobot();
		if (r != null) {
			tests = new TestInstance[] { //
					TestRobot::testScreenshoot//
					, TestRobot::testMouseMove //
			};
			// perform
			for (TestInstance aTest : tests) {
				aTest.performTest(r);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("END");
	}

	public static void testMouseMove(Robot r) {
		int i, maxIterations, deltax, deltay;
		Point mousePoint;
		mousePoint = MouseInfo.getPointerInfo().getLocation();
		r.mouseMove(100, 100);
//
		System.out.println("start MOVING MOUSE");
		deltay = 5;
		deltax = 8;
		maxIterations = 100;
		i = -1;

		while (++i < maxIterations) {
			mousePoint.x += deltax;
			mousePoint.y += deltay;
			r.mouseMove(mousePoint.x, mousePoint.y);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("end MOVING MOUSE");
	}

	public static void testScreenshoot(Robot r) {
		Dimension screenDimension;
		File f;
		f = new File(pathTotal + "aScreenshoot.png");
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		try {
			System.out.println("getting screenshoot to");
			System.out.println(f);
			ImageUtils.writeImageToPNG(f, r.createScreenCapture(new Rectangle(screenDimension)));
			System.out.println("got");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}