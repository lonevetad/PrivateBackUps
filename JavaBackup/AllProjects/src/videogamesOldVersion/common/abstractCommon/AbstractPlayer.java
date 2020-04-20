package videogamesOldVersion.common.abstractCommon;

import tools.Stringable;
import videogamesOldVersion.common.ExperienceLevelHolderOLD;

/**
 * This class hold all players's data.<br>
 * This class MUST NOT be intended as the graphic object shown somewhere acting
 * on a kind of battlefield.<br>
 * This classes' instances are made to store user's data (name, money,
 * experience, level, items, ecc) and serialize them.
 */
public abstract class AbstractPlayer implements Stringable {

	private static final long serialVersionUID = 96203707070892330L;

	public AbstractPlayer() {
	}

	String name;
	ExperienceLevelHolderOLD experienceLevelHolderOLD;

	//

	// TODO GETTER

	public String getName() {
		return name;
	}

	public ExperienceLevelHolderOLD getExperienceLevelHolder() {
		return experienceLevelHolderOLD;
	}
	//

	// TODO SETTER

	public AbstractPlayer setName(String name) {
		this.name = name;
		return this;
	}

	public AbstractPlayer setExperienceLevelHolder(ExperienceLevelHolderOLD experienceLevelHolderOLD) {
		this.experienceLevelHolderOLD = experienceLevelHolderOLD;
		return this;
	}

	@Override
	public void toString(StringBuilder sb, int tabLevel) {
		if (sb != null) {
			addTab(sb, tabLevel++);
			sb.append("Player");
			addTab(sb, tabLevel);
			sb.append("name: ").append(name);
			addTab(sb, tabLevel);
			if (experienceLevelHolderOLD == null)
				sb.append("experienceLevelHolder null");
			else {
				sb.append("experienceLevelHolder: ");
				experienceLevelHolderOLD.toString(sb, tabLevel + 1);
			}
		}
	}
}