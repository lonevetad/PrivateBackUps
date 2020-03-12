package games.generic.controlModel;

import java.util.Map;

public abstract class GameController {
	
	protected Map<String, GameModalityFactory> gameModalities;
	
	
	//
	
	//

	public abstract void init();
	public abstract void closeAll();
}