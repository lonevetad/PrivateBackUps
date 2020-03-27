package tests.tGame.tgEvent1;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GModel;
import games.generic.controlModel.GThread;
import games.generic.controlModel.misc.CurrencyHolder;
import games.generic.controlModel.player.PlayerInGame_Generic;
import games.generic.controlModel.player.PlayerOutside_Generic;
import games.generic.controlModel.subImpl.GModalityET;
import games.generic.controlModel.subImpl.PlayerInGameGeneric_ExampleRPG1;
import games.theRisingAngel.abilities.ADamageReductionPhysicalCurrencyBased;
import tests.tGame.tgEvent1.oggettiDesempio.ObjDamageDeliver;
import tests.tGame.tgEvent1.oggettiDesempio.ObjLifeRegen_PrinterVsTime;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinterTO;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinter_EventDeliver;
import tests.tGame.tgEvent1.oggettiDesempio.ObserverPrinterEvent;

public class GModality_E1 extends GModalityET {

	public GModality_E1(GController controller, String modalityName) {
		super(controller, modalityName);
	}

	GThread threadGame;
	PlayerInGameGeneric_ExampleRPG1 playerRPG;

	@Override
	public void onCreate() {
		Player_E1 p;
		ObjDamageDeliver odd;
		ObjLifeRegen_PrinterVsTime olr_pvt;
		ADamageReductionPhysicalCurrencyBased adrpcb;

		super.onCreate();
		checkAndRebuildThreads();
		//

		// TODO add all stuffs .. qui è il posto in cui dovrebbero stare gli oggetti
		// strani che inserisco
//		GModel_E1 gModelE ;
//		addTimedObject(new ObjDamageDeliver());
		p = new Player_E1(this);
		this.addGameObject(new ObjPrinterTO(1250, "LongWaiting"));
		this.addGameObject(new ObjPrinterTO(333, "Short"));
//		this.addGameObject(new ObjPrinter_EventDeliver(250, "Tiny"));
		// gModelE.addTimeProgressingObject(odd);

		this.addGameObject(new ObjPrinter_EventDeliver(2000, "HAKINA MATATA"));
		this.getEventManager().addEventObserver(new ObserverPrinterEvent());

		odd = new ObjDamageDeliver();
		odd.setTarget(p);
		olr_pvt = new ObjLifeRegen_PrinterVsTime();
		olr_pvt.setCreatureReferred(p);
		this.addGameObject(odd);
		this.addGameObject(olr_pvt);
		this.getEventManager().addEventObserver(olr_pvt);

		// TODO REQUIRES AN EQUIPMENT SSET, SO ADD THIS ABILITY TO AN EQUIPMENT ITEM
		// adrpcb=new ADamageReductionPhysicalCurrencyBased();
////		adrpcb.setOwner(owner);
//		adrpcb.set

		// TODO aggiungere gli esempi pensati negli Appunti e esempio
		// first make the player, then the damager, the healer, the fairy, the
		// money-maker, etc

		/*
		 * aggiungere oggetto che da riduzione del danno pari al 10% dei soldi, ma ad
		 * ogni danno scala il 10% Quindi è TimedObject e EventListener per il danno
		 * 
		 * ADamageReductionPhysicalCurrencyBased
		 */
		/*
		 * E collana che da rigenerazione vitale pari al 25% del danno subito, ma ogni
		 * secondo tale ammontare cala fino a 0 (quindi ad ogni evento del danno,
		 * incrementa il contatore del totale, poi ogni secondo scala di es 4 e aggiorna
		 * le statistiche)
		 */
//		addTimedObject(new ObjDamageDeliver());

		// then ...
	}

	@Override
	public GModel newGameModel() {
		return new GModel_E1();
	}

	@Override
	public GEventInterface newEventInterface() {
		GEventInterface_E1 gei;
		gei = new GEventInterface_E1();
		gei.setNewGameEventManager(this);
		return gei;
	}

	@Override
	public void startGame() {
		checkAndRebuildThreads();
		this.threadGame.start();
	}

	@Override
	public void closeAll() {
		super.closeAll();
		this.threadGame = null;
	}

	@Override
	protected PlayerInGame_Generic newPlayerInGame(PlayerOutside_Generic superPlayer) {
		Player_E1 p;
		p = new Player_E1(this);
		p.setMoneys(newCurrencyHolder());
		return p;
	}

	@Override
	public CurrencyHolder newCurrencyHolder() {
		return new CurrencyHolder_E1(this, 1);
	}

	//

	protected void checkAndRebuildThreads() {
		if (this.threadGame == null)
			this.threadGame = new GThread(new RunGameInstance());
	}

	//

	// previously was ThreadGame_GameRunner_E1
	protected class RunGameInstance implements GThread.GTRunnable {
		boolean isWorking = true; // when the

		@Override
		public void run() {
			while(isWorking) {
				runGameCycle();
			}
		}

		@Override
		public void stopAndDie() {
			this.isWorking = false;
		}
	}
}