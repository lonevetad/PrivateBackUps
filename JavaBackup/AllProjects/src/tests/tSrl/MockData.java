package tests.tSrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.function.Supplier;

public class MockData {
	private static ISRLProgramTextSupplier[] SRL_PROGRAMS_TEST = null;

	public static ISRLProgramTextSupplier[] getProgramsExamples() {
		if (SRL_PROGRAMS_TEST == null) { SRL_PROGRAMS_TEST = srlProgEx(); }
		return SRL_PROGRAMS_TEST;
	}

	private static final ISRLProgramTextSupplier[] srlProgEx() {
		ArrayList<ISRLProgramTextSupplier> al;
		String fileText = null;
		int[] r0Vals = { 9, -8, 6, -13, 2, -2, 1, -1, 0, 3, -3 };
		String[] a = new String[] { //
				"init r0 17 init r1 4", //
				"init r0 7 init r1 4 inc r0", //
				"init r0 7 init r1 4 inc /*commento multilinea*/ r0", //
				"init r0 7 init r1 4 inc //commento singola \n r0", //
				"init r0 7 init r1 4 for r0 { inc r1}", //
				"init r0 7 init r1 4 for r0 { incr r1 } dec r0 decr r0;", //
				"init r0 7 init r1 4 init r2 300 for r0 { for (r1){ inc r2 } } ; dec r0", //
				"init rr -3 init r2 7 ; \n for rr { inc r2 }", //
				"init pippo 5 init pluto -88 swap(pippo, pluto)", //
				"init dividendo 18 init quoziente 0 init tp1 1 init tp2 0 ;\n"
						+ "for dividendo { swap(tp1 tp2) for(tp1){ inc quoziente; } }", //
				"init dividendo 7 init quoziente 0 init tp1 1 init tp2 0 ;\n"
						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
				"init dividendo 1 init quoziente 0 init tp1 1 init tp2 0 ;\n"
						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
				"init dividendo -9 init quoziente 0 init tp1 1 init tp2 0 ;\n"
						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
				"init dividendo -8 init quoziente 0 init tp1 1 init tp2 0 ;\n"
						+ "for dividendo { swap(tp1 tp2) for(tp1){ inc quoziente; } }", //
				"init dividendo -1 init quoziente 0 init tp1 1 init tp2 0 ;\n"
						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
				"init dividendo 0 init quoziente 0 init tp1 1 init tp2 0 ;\n"
						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
				"init a -10 init b 7 for a dec b", //
				"init a -3 init b 7 init c 100 for a { for b { inc c}}", // , //
				"init a -4 init b -16 init c 100 for a { for b { inc c}}",//
		};
		try {
			StringBuilder sb;
			sb = new StringBuilder(1024);
			FileReader fr;
			BufferedReader br;
			fr = new FileReader(new File("./lessThanOne.txt"));
			br = new BufferedReader(fr);
			br.lines().forEach(l -> sb.append('\n').append(l));
			fileText = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fileText == null) {
			int i;
			ISRLProgramTextSupplier[] aa;
			aa = new ISRLProgramTextSupplier[i = a.length];
			while (--i >= 0) {
				aa[i] = new SRLProgramTextSupplier(a[i]);
			}
			return aa;
		}
		al = new ArrayList<>(a.length + r0Vals.length);
		for (String simpleTest : a) {
			al.add(new SRLProgramTextSupplier(simpleTest));
		}
		for (int r0 : r0Vals) {
			al.add(new SRLProgramTextSupplier_ForLessThanOneFile(fileText, r0));
		}
		return al.toArray(new ISRLProgramTextSupplier[al.size()]);
	}

	public static interface ISRLProgramTextSupplier extends Supplier<String> {
	}

	public static class SRLProgramTextSupplier implements ISRLProgramTextSupplier {
		public final String program;

		public SRLProgramTextSupplier(String program) {
			super();
			this.program = program;
		}

		@Override
		public String get() { return program; }
	}

	public static class SRLProgramTextSupplier_ForLessThanOneFile implements ISRLProgramTextSupplier {
		public final int r0;
		public final String program;

		public SRLProgramTextSupplier_ForLessThanOneFile(String program, int r0) {
			super();
			this.program = program;
			this.r0 = r0;
		}

		@Override
		public String get() {
			return "init r0 " + r0 + //
					"init r1 1 init r2 0 init r3 0 init r4 0 \n" //
					+ program;
		}

	}
}