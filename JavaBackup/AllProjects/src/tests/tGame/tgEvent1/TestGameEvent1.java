package tests.tGame.tgEvent1;

import games.generic.controlModel.GameLauncher;

public class TestGameEvent1 {

	public static void main(String[] args) {
		GL_E1 gl;
//		GModality_E1 gModalityE;
//		GModel_E1 gModelE;
		gl = new GL_E1();
		GameLauncher.initGame(gl);
	}
}