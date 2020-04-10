package tests.tGame.tgEvent1;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GModel;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.GThread;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.PlayerRPG_E1;
import games.theRisingAngel.inventory.ArmProtectionShieldingDamageByMoney;
import games.theRisingAngel.inventory.NecklaceOfPainRinvigoring;
import tests.tGame.tgEvent1.oggettiDesempio.ObjDamageDeliverE1;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinterTO;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinter_EventDeliver;
import tests.tGame.tgEvent1.oggettiDesempio.ObserverPrinterEvent;

public class GModality_E1 extends GModalityET {

	public GModality_E1(GController controller, String modalityName) {
		super(controller, modalityName);
	}

	GThread threadGame;

	public PlayerRPG_E1 getPlayerRPG() {
		return (PlayerRPG_E1) player;
	}

	@Override
	public void startGame() {
		checkAndRebuildThreads();
		this.threadGame.start();
		getPlayerRPG().onStartingGame(this);
	}

	@Override
	public void onCreate() {
		GModel_E1 gmodel;
		Player_E1 p;
		ObjDamageDeliverE1 odd;
		ObserverPrinterEvent ope;
		NecklaceOfPainRinvigoring necklace_opr;
		ArmProtectionShieldingDamageByMoney armProtection_sdbm;

		super.onCreate();
		//
		gmodel = (GModel_E1) this.getModel();

		// TODO add all stuffs .. qui è il posto in cui dovrebbero stare gli oggetti
		// strani che inserisco
//		GModel_E1 gModelE ;
//		addTimedObject(new ObjDamageDeliver());
		p = (Player_E1) newPlayerInGame(null); // new Player_E1(this);
		p.setName("Lonevetad");
		this.setPlayer(p);
		p.setGameModality(this);

		p.getCurrencies().setMoneyAmount(0, 100);

		this.addGameObject(p);
		this.addGameObject(new ObjPrinterTO(1250, "LongWaiting"));
		this.addGameObject(new ObjPrinterTO(333, "Short") {

			@Override
			public void executeAction(GModality modality) {
				System.out.println("player's life: " + p.getLife() + ", life regen: " + p.getLifeRegenation());
			}
		});
//		this.addGameObject(new ObjPrinter_EventDeliver(250, "Tiny"));
		// gModelE.addTimeProgressingObject(odd);

		this.addGameObject(new ObjPrinter_EventDeliver(2000, "HAKINA MATATA"));
		ope = new ObserverPrinterEvent();
//		this.addEventObserver(ope);
		this.addGameObject(ope);

		necklace_opr = new NecklaceOfPainRinvigoring();
//		necklace_opr.setCreatureReferred(p);
		p.equip(necklace_opr);
//		this.addGameObject(necklace_opr); // yet provided by equipping
//		this.addEventObserver(necklace_opr); // yet provided by equipping

		armProtection_sdbm = new ArmProtectionShieldingDamageByMoney();
//		adrpcb = new ADamageReductionPhysicalCurrencyBased();
//		adrpcb.setOwner(p);
//		adrpcb.setEquipItem(equipmentItem);
		p.equip(armProtection_sdbm);

		// TODO aggiungere gli esempi pensati negli Appunti e esempio
		// first make the player, then the damager, the healer, the fairy, the
		// money-maker, etc

		odd = new ObjDamageDeliverE1(5000);
		odd.setTarget(p);
		odd.setDamageAmount(24);
		this.addGameObject(odd);

		System.out.println("GModalit_E1#onCreate .. quanti oggetti ho?");
		this.forEachGameObject(o -> System.out.println(o));
		System.out.println("and timed objects?");
		gmodel.forEachTimedObject(o -> System.out.println(o));
		System.out.println("timed objects ended\n\n");
		gmodel.forEachObjHolder((gohname, goh) -> {
			System.out.println("GOH " + gohname + " has: ");
			goh.forEach(o -> System.out.println("\t - " + o));
		});
		System.out.println("and then");
		// then ...
		checkAndRebuildThreads();
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
	protected PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer) {
		Player_E1 p;
		p = new Player_E1(this);
		p.setCurrencies(newCurrencyHolder());
		return p;
	}

	@Override
	public CurrencySet newCurrencyHolder() {
		return new CurrencyHolder_E1(this, 1);
	}

	@Override
	public void closeAll() {
		super.closeAll();
		this.threadGame = null;
	}

	//

	protected void checkAndRebuildThreads() {
		if (this.threadGame == null)
			this.threadGame = new GThread(new RunGameInstance());
	}

	//

	// TODO CLASS
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