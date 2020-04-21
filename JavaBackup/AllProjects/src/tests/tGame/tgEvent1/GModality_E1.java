package tests.tGame.tgEvent1;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GModel;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.theRisingAngel.GModalityTRAr;
import games.theRisingAngel.GameObjectsProvidersHolderTRAr;
import games.theRisingAngel.inventory.ArmProtectionShieldingDamageByMoney;
import games.theRisingAngel.inventory.NecklaceOfPainRinvigoring;
import games.theRisingAngel.misc.PlayerCharacterTypesHolder.PlayerCharacterTypes;
import tests.tGame.tgEvent1.oggettiDesempio.ObjDamageDeliverE1;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinterTO;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinter_EventDeliver;
import tests.tGame.tgEvent1.oggettiDesempio.ObserverPrinterEvent;
import tools.ObjectNamedID;

public class GModality_E1 extends GModalityTRAr {
	static final int STARTING_PLAYER_LIFE_MAX = 100;

	public GModality_E1(GController controller, String modalityName) {
		super(controller, modalityName);
	}

	public Player_E1 getPlayerRPG() {
		return (Player_E1) player;
	}

	@Override
	public void startGame() {
		super.startGame();
		getPlayerRPG().onEnteringInGame(this);
	}

	@Override
	public void onCreate() {
		GModel_E1 gmodel;
		Player_E1 p;
		ObjDamageDeliverE1 odd;
		ObserverPrinterEvent ope;
		NecklaceOfPainRinvigoring necklace_opr;
		ArmProtectionShieldingDamageByMoney armProtection_sdbm;
//		GC_E1 contr;
		GameObjectsProvidersHolderTRAr goph;

		super.onCreate();

		System.out.println("\n\n\n MY NAME: " + getModalityName() + "\n\n");

		//
//		contr = (GC_E1) controller;
		gmodel = (GModel_E1) this.getModel();
		goph = (GameObjectsProvidersHolderTRAr) this.getGameObjectsProvider();

		// TODO add all stuffs .. qui è il posto in cui dovrebbero stare gli oggetti
		// strani che inserisco
		p = (Player_E1) newPlayerInGame(null, PlayerCharacterTypes.Human); // new Player_E1(this);
		p.setName("Lonevetad");

		if (p.getAttributes().getBonusCalculator() == null)
			throw new IllegalStateException("WTF WHY IS NULL?");
		if (p.getAttributes().getBonusCalculator().getCreatureAttributesSet() == null)
			throw new IllegalStateException("WTF WHY IS NULL?");

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

		this.addGameObject(new ObjPrinter_EventDeliver(2000, "HAKINA MATATA") {

			@Override
			public String getText() {
//				return p.getAttributes().toString();
				StringBuilder sb;
				CreatureAttributes ca;
				sb = new StringBuilder(127);
				ca = p.getAttributes();
				sb.append("Player life ").append(p.getLife()).append(", current values: ");
				for (int i = 0; i < ca.getAttributesCount(); i++)
					sb.append(ca.getValue(i)).append(", ");
				return sb.toString();
			}
		});
		ope = new ObserverPrinterEvent();
//		this.addEventObserver(ope);
		this.addGameObject(ope);

		necklace_opr = (NecklaceOfPainRinvigoring) goph.getEquipmentsProvider()//
				.getNewObjByName(this, NecklaceOfPainRinvigoring.NAME);
//		necklace_opr.setCreatureReferred(p);
		p.equip(necklace_opr);
//		this.addGameObject(necklace_opr); // yet provided by equipping
//		this.addEventObserver(necklace_opr); // yet provided by equipping

		armProtection_sdbm = (ArmProtectionShieldingDamageByMoney) goph.getEquipmentsProvider().getNewObjByName(this,
				ArmProtectionShieldingDamageByMoney.NAME);
//		adrpcb = new ADamageReductionPhysicalCurrencyBased();
//		adrpcb.setOwner(p);
//		adrpcb.setEquipItem(equipmentItem);
		p.equip(armProtection_sdbm);

		// TODO aggiungere gli esempi pensati negli Appunti e esempio
		// first make the player, then the damager, the healer, the fairy, the
		// money-maker, etc

		odd = new ObjDamageDeliverE1(5000);
		odd.setTarget(p);
		odd.setDamageAmount(75);
		odd.setAccumulatedTimeElapsed(3000);
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
//		checkAndRebuildThreads();
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
	protected PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer, ObjectNamedID playerType) {
		Player_E1 p;
		p = new Player_E1(this, (PlayerCharacterTypes) playerType);

		if (p.getAttributes().getBonusCalculator() == null)
			throw new IllegalStateException("WTF WHY IS NULL?");
		if (p.getAttributes().getBonusCalculator().getCreatureAttributesSet() == null)
			throw new IllegalStateException("WTF WHY IS NULL?");

		setStartingBaseAttributes(p);

		if (p.getAttributes().getBonusCalculator() == null)
			throw new IllegalStateException("WTF WHY IS NULL?");
		if (p.getAttributes().getBonusCalculator().getCreatureAttributesSet() == null)
			throw new IllegalStateException("WTF WHY IS NULL?");

		p.setLifeMax(STARTING_PLAYER_LIFE_MAX);
		p.setLife(STARTING_PLAYER_LIFE_MAX);
		p.setCurrencies(newCurrencyHolder());
		return p;
	}

	@Override
	public CurrencySet newCurrencyHolder() {
		return new CurrencyHolder_E1(this, 1);
	}
}