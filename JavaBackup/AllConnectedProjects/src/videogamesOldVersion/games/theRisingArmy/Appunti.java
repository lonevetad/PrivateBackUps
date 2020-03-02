package games.theRisingArmy;

/**
 * Appunti per gioco di carte, progetto<br>
 * <br>
 * 1)<br>
 * Definizione:<br>
 * - supertipo: creatura, artefatto, incantesimo, stregoneria, runa (terra), player..<br>
 * - tipo: elfo, goblin, fuoco (montagna), costrutto, aura, ..<br>
 * - skeleton-type : supertipo + tipo + colore<br>
 * <br>
 * Ogni cosa (carta, permanente e personaggio [il character del giocatore]) deve avere il colore
 * impostato, che potrebbe essere un set di ManaType<br>
 * <br>
 * <br>
 * <br>
 * 2)<br>
 * Enumerazione delle priorita'† delle abilita'† statiche al momento del ricalcolo:<br>
 * - skeleton modifier<br>
 * - list ability modifier,<br>
 * - stats modifier<br>
 * In caso di uguale priorita'†, si risolvono prima le abilita'† che sono marcate, tramite un
 * apposito campo, come "diniegative", poi le e poi le "permissive" (ossia, si compara l'ordinale di
 * questi valori di una enumerazione (es: "tutti gli umani non possono attaccare")<br>
 * <br>
 * 18/12/2017: NO: si usa l'ordine temporale : la piu' recente rimpiazza il vecchio <br>
 * Nel MatchController, ci sara'† un array di liste di abilita'† statiche, separate secondo il
 * ordine appena sopra riportato... oppure si una lista ordinata (red black tree?) usando un buon
 * ordinatore<br>
 * <br>
 * I permanenti hanno un campo, ossia una variabile (magari definita da una interfaccia) che e' un
 * booleano "isInsideQueueStaticAbilityRecalc"<br>
 * <br>
 * Bozza dell'algoritmo per il ricalcolo:<br>
 * <br>
 * Permanent p;<br>
 * PermanentQueue = new list [chiamiamola PQ]<br>
 * For( permanent pp : getAllPermanent() ){<br>
 * Pp.resetStatsNowToBase();<br>
 * Pp.applyDamageReceived();<br>
 * Pp..setIsInsideQueueStaticAbilityRecalc(false);<br>
 * PQ.add(pp);<br>
 * }<br>
 * <br>
 * While(! PQ.isEmpty()){<br>
 * P = PQ.poll()<br>
 * <br>
 * For( ability a : getMatch().ListAbility()){<br>
 * P.tryToApplyAblility( a);<br>
 * }<br>
 * }<br>
 * <br>
 * <br>
 * <br>
 * 4) Permanente, Magia e giocatore sono tutti e tre sottoclassi (o classi implentanti una
 * interfaccka<br>
 * 6) le abilit√† delle stregoneria non sono pezzi di codice scritto nella classe, ma vere e proprie
 * abilit√†, come ce le hanno i permanenti .. infatti, la.classe "magia" ha una lista di Abilit√† e
 * la risoluzione di una magia √® l'esecuzione di tutte le sue abilit√†.<br>
 * <br>
 * <br>
 * <br>
 * 5) il metodo tryToApplyAbility<br>
 * Permanent p;<br>
 * PermanentQueue = new list [chiamiamola PQ]<br>
 * For( permanent pp : getAllPermanent() ){<br>
 * Pp.resetStatsNowToBase();<br>
 * Pp.applyDamageReceived();<br>
 * Pp..setIsInsideQueueStaticAbilityRecalc(false);<br>
 * PQ.add(pp);<br>
 * }<br>
 * <br>
 * While(! PQ.isEmpty()){<br>
 * P = PQ.poll()<br>
 * If( !isInsideQueueStaticAbilityRecalc()){<br>
 * <br>
 * For( ability a : getMatch().ListAbility()){<br>
 * if( P.tryToApplyAblility( a) && (!p.isInsideQueueStaticAbilityRecalc())){<br>
 * P.setIsInsideQueueStaticAbilityRecalc(true)<br>
 * PQ.add(p);<br>
 * }<br>
 * }<br>
 * <br>
 * }<br>
 * <br>
 * Nella classe Ability, oltre ad avere il puntatore a <br>
 * AblilitySource (permanente, stregoneria o character player)ha il metodo<br>
 * <br>
 * IfCanApplyAbilityTo( AblilitySource target );<br>
 * <br>
 * Pu√≤ essere risolto come un albero di BoolranCondition in and oppure or .. come una interfaccia
 * che feci tempo fa,..<br>
 * Esempio<br>
 * <br>
 * SetApplyCondition( boolanChecker)<br>
 * Es: setAC( AndBooleanCondition.newIstanza().setFirstSubexpression( new CreatureChecking(
 * Enumtype.Creature)).setSecondSubexpression( something))<br>
 * <br>
 * );<br>
 * <p>
 * 18/12/2017:<br>
 * Questa classe contiene tutto .. GameModel e i thread/Runner del gioco con le rispettive
 * implementazioni.<br>
 * Il MainController delega alcune chiamate di metodi Ë ha solo i thread (e funzioni) "di servizio",
 * come la musica e la gestione delle connessioni "internet" (socket) <br>
 * Il GameMechanism di TRAr (The Rising Army) deve predisporre due metodi : "activateAbility" e
 * "castAbility" . Mentre il secondo "mette in pila" l'abilit‡ secondo il principio di funzionamento
 * del gioco di carte Magic the Gathering, il primo la esegue concretamente. Entrambi richiedono
 * come parametro l'abilit‡ da eseguire .<br>
 * Il primo metodo, "activate", modifica il Model a seconda dell'abilita , ma prima di eseguirla ne
 * fa una deep-clone e poi ciclabile su TUTTE le abilit‡ di tutti i permanenti in gioco per cercare
 * di apportare le varie modifiche, tramite un metodo "tryToAlterAbility". <br>
 * Questo perchÈ:
 * <ul>
 * <li>quasi tutto Ë una abilit‡ : attaccare (in ogni sua forma: melee, range, arc swipe, area,
 * linear, cono, ecc), produrre mana, curare, evocare, ossia le abilit‡ attivate, ma anche quelle
 * innescate e statiche, e alcune di queste abilit‡ potrebbero alterare quella in corso.</li>
 * <li>le abilit‡ possono contenere svariate variabili da cui dipende il funzionamento implementare
 * molteplici interfacce , come "ActivationCostHolder", "DamageHolder", "ManaProductionHolder",
 * "RayHolder" (queste ultime tre generalizzabili come IntHolder, ma non conviene) ed una abilit‡
 * (statica?) potrebbe modificarne il valore .. per esempio, potrebbe incrementare la cura apportata
 * o il danno, o ridurlo, o convertire un costo di mana colorato in incolore, o ridurre il costo
 * incolore, o ampliare il raggio di una esplosione, ecc</li>
 * </ul>
 * L'implementazione di default Ë vuota<br>
 * Le abilit‡ devono anche mettere a disposizione altri metodi, come "alterDamage", con un booleano
 * se Ë ricevuto o inflitto e altre informazioni come fonte e destinatario, E "checkTrigger"
 */
public class Appunti {

	/** Solo una classe di appunti, stop */
	public Appunti() {
	}
}