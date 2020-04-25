class GraphPlan {
	Set<ActionGP> getActionsAt(int i){
		// TODO
		return null;
	}

	void computeMutexAt(int i){
		Set<ActionGP> actions_i = getActionsAt(i);
		// check inconsistenza
		forEachPairActions(actions_i, i, (a1, a2, level)->{
			if( inconsistentEffects(a1,a2) || areInterferring(a1,a2) || preconditionsCompetiting(a1, a2, level) )
					addMutexLink(a1, a2);
		});
	}

	/** 
	 * Returns true if exists a pair of literal produced by those actions that are complementary
	*/
	boolean inconsistentEffects(ActionGP a1, ActionGP a2){
		if( a1.effects.size() > a2.effects.size() ){ // ottimizzazione del foreach interno
			for(Literal l2: a2.effects){
				for(Literal l1: a1.effects){
					if( l1.isComplementaryOf(l2) )
						return true;
				}
			}
			return false;
		} else {
			for(Literal l1: a1.effects){
				for(Literal l2: a1.effects){
					if( l1.isComplementaryOf(l2) )
						return true;
				}
			}
			return false;
		}
	}

	boolean areInterferring(ActionGP a1, ActionGP a2){
		for(Literal l2: a2.preconditions){
			for(Literal l1: a1.effects){
				if( l1.isComplementaryOf(l2) )
					return true;
			}
		}
		for(Literal l1: a1.preconditions){
			for(Literal l2: a1.effects){
				if( l1.isComplementaryOf(l2) )
					return true;
			}
		}
		return false;
	}
	boolean preconditionsCompetiting(ActionGP a1, ActionGP a2, int i){
		if( a1.preconditions.size() > a2.preconditions.size() ){ // ottimizzazione del foreach interno
			for(Literal l2: a2.preconditions){
				for(Literal l1: a1.preconditions){
					if( areInMutexAt(l1, l2, i) )
						return true;
				}
			}
			return false;
		} else {
			for(Literal l1: a1.preconditions){
				for(Literal l2: a1.preconditions){
					if( areInMutexAt(l1, l2, i) )
						return true;
				}
			}
			return false;
		}
	}

	//

	void addMutexLink(ActionGP a1, ActionGP a2){
		// TODO
	}

	boolean areInMutexAt(Literal l1, Literal l2, int i){
		// TODO
		return false;
	}

	void forEachPairActions(Set<ActionGP> actions_i, int i, BiActionConsumerAt consumer){
		Set<PairOfActionGP> actions_i_visited = ...;
		PairOfActionGP p = new PairOfActionGP(null, null);
		actions_i.forEach(a1 ->{
			actions_i.forEach(a2->{
				if(a1 != a2){
					p.a1 = a1; p.a2 = a2;
					if(! actions_i_visited.contains(p)){
						actions_i_visited.add(p.cloneMe());
						consumer.apply(a1, a2, i);
					}
				}
			});
		});
	}

	interface BiActionConsumerAt { //extends BiConsumer<ActionGP, ActionGP>{
		public void apply(ActionGP a1, ActionGP a2, int i);
	}
	interface PairActionConsumer extends Consumer<PairOfActionGP>{
	}
	static class Literal {
		boolean isPositive;
		String text;
		Literal(boolean isPositive, String text){
			this.isPositive=isPositive; this.text=text;
		}
		public boolean isComplementaryOf(Literal l2){
			return text.equals(l2.text) && (isPositive != l2.isPositive);
		}
		public Literal negate(){
			return new Literal(! isPositive, text);
		}
	}
	static class ActionGP{
		List<Literal> preconditions, effects;
	}
	static class PairOfActionGP{
		ActionGP a1, a2;
		PairOfActionGP(ActionGP a1, ActionGP a2){
			this.a1 = a1; this.a2 = a2;
		}
		public PairOfActionGP cloneMe(){
			return new PairOfActionGP(a1, a2);
		}
	}
}