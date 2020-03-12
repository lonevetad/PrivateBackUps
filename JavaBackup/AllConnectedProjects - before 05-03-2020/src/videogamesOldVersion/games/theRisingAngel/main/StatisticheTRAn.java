package games.theRisingAngel.main;

import java.util.function.Consumer;

import common.StatisticsInt;
import common.abstractCommon.StatisticField;
import common.abstractCommon.behaviouralObjectsAC.MyComparator;

public class StatisticheTRAn extends StatisticsInt {
	private static final long serialVersionUID = 951408120202551L;

	public static enum StatsTRAn implements StatisticField {
		LIFE, MANA, SHIELD,
		//
		DANNO_MISCHIA_MAX, DANNO_MAGICO_MAX, DANNO_MISCHIA_MIN, DANNO_MAGICO_MIN,
		// danno_distanza_max, danno_distanza_min,

		PROBABIL_DANNO_CRITICO_MISCHIA, PROBABIL_DANNO_CRITICO_MAGICO, COEFF_DANNO_CRITICO_MISCHIA, COEFF_DANNO_CRITICO_MAGICO,
		// probabil_danno_critico_distanza, coeff_danno_critico_distanza,

		// VALORI percentualiiiiiiiiS
		CORAZZA_MISCHIA, CORAZZA_MAGICA, // CORAZZA_DISTANZA,

		PERCENT_PARATA_SCUDO, PERCENT_SCHIVARE, PERCENT_COLPIRE, PERCENT_DISARMARE, PERCENT_MIN_PER_DISARMO,

		// sia mischia che distanza .. percentuale
		ACCELLERAZ_ATTACCO_FISICO, ACCELLERAZ_ATTACCO_MAGICO,
		// valore percentuale che riduce il costo
		RIDUZIONE_COSTO_MAGIE,

		RIGEN_VITA, RIGEN_MANA, RIGEN_SCUDO,

		FORTUNA_MAX,
		// la fascia di numeri random va in [ min , min+random()*max ) ..
		FORTUNA_MIN,

		VITA_RIGEN_NEMICO_COLPITO, MANA_RIGEN_NEMICO_COLPITO, SCUDO_RIGEN_NEMICO_COLPITO, VITA_RIGEN_NEMICO_UCCISO, MANA_RIGEN_NEMICO_UCCISO, SCUDO_RIGEN_NEMICO_UCCISO,

		PERCENT_DANNO_CONVERTITO_VITA, PERCENT_DANNO_CONVERTITO_MANA, PERCENT_DANNO_CONVERTITO_SCUDO, PERCENT_DANNO_DISPERSO, PERCENT_DANNO_RIFLESSO,
		/**
		 * Quando si subisce danno, e' possibile che un pezzo d'equipaggiamento faccia qualcosa,
		 * un'azione offensiva. Per esempio, potrebbe creare una esplosione a danno variabile. Tale
		 * danno potrebbe dipendere da una percentuale del danno che si e' subito.
		 */
		PERCENT_DANNO_RILANCIATO,

		/**
		 * Numero percentuale? che incrementa la QUANTIT�
		 */
		PERCENT_QUANTITA_SOLDI_BONUS, PERCENT_TROVARE_IN_MENO__NULLA, PERCENT_TROVARE_SOLDI, PERCENT_TROVARE_CIBO, PERCENT_TROVARE_EQUIPMENT,

		/**
		 * Pietre peculiari o altra roba che vengono utilizzate per creare o plasmare oggetti
		 */
		PERCENT_TROVARE_MATERIALE_CRAFT,

		// nuove matricole
		VELOC_SPOSTAMENTO, COEFF_ESPERIENZA;

		public static final MyComparator<StatsTRAn> COMPARATOR_STAT_TRAN = (e1, e2) -> {
			int o1, o2;
			if (e1 == e2) return 0;
			if (e1 == null) return -1;
			if (e2 == null) return 1;
			return
			// e1.compareTo(e2)
			((o1 = e1.ordinal()) == (o2 = e2.ordinal())) ? 0 : (o1 > o2 ? 1 : -1);
		};

		final boolean isBounded;
		Integer idStat;

		StatsTRAn() {
			this(false);
		}

		StatsTRAn(boolean b) {
			isBounded = b;
			this.idStat = ordinal();
		}

		@Override
		public boolean isBounded() {
			return isBounded;
		}

		@Override
		public Integer getIdStatAsInteger() {
			return idStat;
		}
	}

	public static final StatsTRAn[] valuesStatsTRAn = StatsTRAn.values();
	public static final int LENGTH_StatsTRAn = valuesStatsTRAn.length;

	public StatisticheTRAn() {
		super();
		setNumberOfStats(LENGTH_StatsTRAn);
	}

	//

	//

	public StatisticheTRAn forEachStat(Consumer<StatsTRAn> toDo) {
		if (toDo != null) {
			int i, len;
			StatsTRAn[] v;
			v = valuesStatsTRAn;
			len = LENGTH_StatsTRAn;
			i = -1;
			while (++i < len) {
				toDo.accept(v[i]);
			}
		}
		return this;
	}
}
