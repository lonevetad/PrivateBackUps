package tests.tGame.tgEvent1;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GModel;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.theRisingAngel.GModalityTRAn;
import games.theRisingAngel.GameObjectsProvidersHolderTRAn;
import games.theRisingAngel.abilities.ArmProtectionShieldingDamageByMoney;
import games.theRisingAngel.inventory.NecklaceOfPainRinvigoring;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.PlayerCharacterTypesHolder.PlayerCharacterTypes;
import tests.tGame.tgEvent1.oggettiDesempio.ObjDamageDeliverE1;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinterTO;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinter_EventDeliver;
import tests.tGame.tgEvent1.oggettiDesempio.ObserverPrinterEvent;
import tools.ObjectNamedID;

public class GModality_E1 extends GModalityTRAn {
	static final int STARTING_PLAYER_LIFE_MAX = 100;

	public GModality_E1(GController controller, String modalityName) { super(controller, modalityName); }

	public Player_E1 getPlayerRPG() { return (Player_E1) player; }

	@Override
	public void startGame() {
		super.startGame();
//		getPlayerRPG().onEnteringInGame(this);
	}

	@Override
	public void onCreate() {
		GModel_E1 gmodel;
		Player_E1 p;
		ObjDamageDeliverE1 odd;
		ObserverPrinterEvent ope;
		NecklaceOfPainRinvigoring necklace_opr;
		ArmProtectionShieldingDamageByMoney armProtection_sdbm;
		EquipmentItem equip;
//		GC_E1 contr;
		GameObjectsProvidersHolderTRAn goph;
		ObjPrinter_EventDeliver printerPlayer;
		String equipmentName;

		super.onCreate();
		super.setRandomSeed(0);

		System.out.println("\n\n\n MY NAME: " + getModalityName() + "\n\n");

		//
//		contr = (GC_E1) controller;
		gmodel = (GModel_E1) this.getModel();
		goph = (GameObjectsProvidersHolderTRAn) this.getGameObjectsProvider();

		// TODO add all stuffs .. qui è il posto in cui dovrebbero stare gli oggetti
		// strani che inserisco
		p = (Player_E1) newPlayerInGame(null, PlayerCharacterTypes.Human); // new Player_E1(this);
		p.setName("Lonevetad");
		this.setPlayer(p);

		p.setLife((int) (p.getLife() * 1.5));
		p.setGameModality(this);
//		p.getCurrencies().setMoneyAmount(0, 100);

		this.addGameObject(p);
		this.addGameObject(new ObjPrinterTO(1250, "LongWaiting"));
//		this.addGameObject(new ObjPrinterTO(333, "Short") {
//
//			@Override
//			public void executeAction(GModality modality) {
//				System.out.println("player's life: " + p.getLife() + ", life regen: " + p.getLifeRegenation());
//			}
//		});
//		this.addGameObject(new ObjPrinter_EventDeliver(250, "Tiny"));
		// gModelE.addTimeProgressingObject(odd);
		printerPlayer = new ObjPrinter_EventDeliver(2000, "HAKUNA MATATA") {
			private static final long serialVersionUID = 1L;

			@Override
			public String getText() {
//				return p.getAttributes().toString();
				StringBuilder sb;
				CreatureAttributes ca;
				sb = new StringBuilder(127);
				ca = p.getAttributes();
				sb.append("Player life ").append(p.getLife()).append(", current values: ");
				for (int i = 0; i < ca.getAttributesCount(); i++)
					sb.append(ca.getValue(AttributesTRAn.VALUES[i])).append(", ");
				return sb.toString();
			}
		};
//		this.addGameObject(printerPlayer);
		ope = new ObserverPrinterEvent();
//		this.addEventObserver(ope);
		this.addGameObject(ope);

		necklace_opr = (NecklaceOfPainRinvigoring) goph.getEquipmentsProvider()//
				.getNewObjByName(this, NecklaceOfPainRinvigoring.NAME);
		p.equip(necklace_opr);

		armProtection_sdbm = (ArmProtectionShieldingDamageByMoney) goph.getEquipmentsProvider().getNewObjByName(this,
				ArmProtectionShieldingDamageByMoney.NAME);
		p.equip(armProtection_sdbm);

		// TODO aggiungere gli esempi pensati negli Appunti e esempio
		// first make the player, then the damager, the healer, the fairy, the
		// money-maker, etc

		odd = new ObjDamageDeliverE1(6000);
		odd.setTarget(p);
		odd.setDamageAmount(125);
		odd.setAccumulatedTimeElapsed(5000);
		this.addGameObject(odd);

//		odd = new ObjDamageDeliverE1(4000);
//		odd.setTarget(p);
//		odd.setDamageAmount(75);
//		odd.setAccumulatedTimeElapsed(2500);
//		this.addGameObject(odd);

		//

		equipmentName = "Plated Armor of Stonefying Skin";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		System.out.println("\n\n equipping: " + equipmentName);
		System.out.println(equip.toString());
		System.out.println("\n\n");
		p.equip(equip);
		System.out.println("Plated Armor of Stonefying Skin has " + equip.getAbilities().size() + " abilities !!!");

		equipmentName = "Belt with Limph-made Walled";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		System.out.println("\n\n equipping: " + equipmentName);
		System.out.println(equip.toString());
		System.out.println("\n\n");
		p.equip(equip);

		equipmentName = "Cloth Hat";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		p.equip(equip);

		equipmentName = "Ring of rusted plate";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		p.equip(equip);

		equipmentName = "Sunstone Ring";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		p.equip(equip);

		equipmentName = "Triphane Ring";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		p.equip(equip);

		equipmentName = "Amazonite Ring";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		p.equip(equip);
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		p.equip(equip);
		// second slot or hand
		equipmentName = "Moonstone Ring";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		p.equip(equip);

		equipmentName = "Gloves of the mad hunter";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		p.equip(equip);

		//

		System.out.println("\n\n\nGModalit_E1#onCreate .. quanti oggetti ho?");
		this.forEachGameObject(o -> System.out.println(o));
		System.out.println("\n\n and timed objects?");
		gmodel.forEachTimedObject(o -> System.out.println(o));
		System.out.println("timed objects ended\n\n");

		gmodel.forEachObjHolder((gohname, goh) -> {
			System.out.println("\n\nGOH " + gohname + " has: ");
			goh.forEach(o -> System.out.println("\t - " + o));
		});
		System.out.println("and then\n\n\n equipment set:");
		p.getEquipmentSet().forEachEquipment((e, i) -> {
			if (e != null)
				System.out.println(i + "-> " + e);
		});
		System.out.println("\n\n at the end, the player looks like:");
		System.out.println(printerPlayer.getText());
		// then ...
//		checkAndRebuildThreads();
	}

	@Override
	public GModel newGameModel() { return new GModel_E1(); }

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
	public CurrencySet newCurrencyHolder() { return new CurrencyHolder_E1(this, 1); }
}