package videogamesOldVersion.common.utilities;

import java.io.Serializable;

/**
 * Evento di qualsiasi tipo, lanciato solitamente dalle creature e dagli altri oggetti nella mappa
 * (torri, proiettili, lava) possono essere innescati da qualsiasi cosa (abilita di oggetti, come
 * "all'inizio del turno", "Ogni volta che si cura", "ogni volta che subisce danno", da esplosioni,
 * ecc) e possono fare qualsiasi cosa (curare, creare altri oggetti nella mappa, sparare proiettili,
 * spostare oggetti (teletrasporto), ecc)
 */
@Deprecated
public abstract class GameEventGeneric implements Serializable {

	// GameEventGeneric prev, next;

	private static final long serialVersionUID = 51306517778877L;

	public GameEventGeneric() {
		// prev = next = null;
	}

	// protected
	public abstract void performStep(long millisecondsToBePassed);

	public boolean mustBeRemoved() {
		return true;
	}

	// public class GameEventQueue {}
}