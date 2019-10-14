package common.tests.testsLittle;

import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.ShapeRunners;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.removed.mOLM_Deprecated.ShapeRunners_OLD_METHODS;

public class TestEfficience_MolmMethods {

	public static interface Tester {
		public void test(MatrixObjectLocationManager molm, DoSomethingWithNode doswn, int rip);
	}

	private TestEfficience_MolmMethods() {
	}

	public static void main(String[] args) {
		MatrixObjectLocationManager molm;
		DoSomethingWithNode doswn;
		Tester test;

		System.out.println("MAIN START");

		molm = MatrixObjectLocationManager.newDefaultInstance(//
				// 16, 12);
				1200, 900);
		doswn = (AbstractMatrixObjectLocationManager m, ObjectWithID item, int x, int y) -> {
			return null;
		};

		test = TestEfficience_MolmMethods::test_ShapeRunners_fillRectangle_Unsafe;

		test.test(molm, doswn, 500000);

		System.out.println("MAIN END");
	}

	static void test_ShapeRunners_fillRectangle_Unsafe(MatrixObjectLocationManager molm, DoSomethingWithNode doswn,
			int rip) {
		int i, xx, yy, w, h;
		long wstart, wend;
		double ang;

		xx = molm.getWidth() >> 1;
		yy = molm.getHeight() >> 1;
		w = xx >> 1;
		h = yy >> 1;
		ang = 222.0;

		i = -1;
		wstart = System.currentTimeMillis();
		while (++i < rip) {
			ShapeRunners.fillRectangle_Unsafe(molm, xx, yy, w, h, doswn, ang);
		}
		wend = System.currentTimeMillis();
		System.out.println("Actual fillRectangle_Unsafe tooks " + (wend - wstart) + " millis");

		i = -1;
		wstart = System.currentTimeMillis();
		while (++i < rip) {
			ShapeRunners_OLD_METHODS.fillRectangle_Unsafe_ORIGINAL(molm, xx, yy, w, h, doswn, ang);
		}
		wend = System.currentTimeMillis();
		System.out.println("original fillRectangle_Unsafe tooks " + (wend - wstart) + " millis");
	}

}
