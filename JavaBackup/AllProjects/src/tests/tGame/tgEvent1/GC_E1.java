package tests.tGame.tgEvent1;

import games.generic.controlModel.subImpl.GameControllerET;
import tests.tGame.tgEvent1.oggettiDesempio.ObjDamageDeliver;

public class GC_E1 extends GameControllerET {

	public static final String GM_NAME = "TEST";

	public GC_E1() {
		super();
	}

	@Override
	protected void defineGameModalitiesFactories() {
		this.getGameModalitiesFactories().put(GM_NAME, (name, gc) -> {
			return new GModality_E1(name, gc);
		});
	}

	@Override
	public void startGame() {
		super.setCurrentGameModality(this.newModalityByName(GM_NAME));

		//

		// TODO add all stuffs
		GModality_E1 gModalityE;
//		GModel_E1 gModelE ;
		ObjDamageDeliver odd;

		gModalityE = (GModality_E1) this.getCurrentGameModality();
		odd = new ObjDamageDeliver();
		gModalityE.addTimedObject(odd);
//			gModelE.addTimeProgressingObject(odd);

		// TODO aggiungere gli esempi pensati negli Appunti e esempio

		// first make the player, then the damager, the healer, the fairy, the
		// money-maker, etc

		// then
		super.startGame();
	}

	@Override
	public void closeAll() {
//		isAlive = false;
		super.closeAll();
//		this.getCurrentGameModality().closeAll(); // yet done in super
	}
}