package videogamesOldVersion.common.abstractCommon;

import tools.Stringable;
import videogamesOldVersion.common.ExperienceLevelHolder;

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
	ExperienceLevelHolder experienceLevelHolder;

	//

	// TODO GETTER

	public String getName() {
		return name;
	}

	public ExperienceLevelHolder getExperienceLevelHolder() {
		return experienceLevelHolder;
	}
	//

	// TODO SETTER

	public AbstractPlayer setName(String name) {
		this.name = name;
		return this;
	}

	public AbstractPlayer setExperienceLevelHolder(ExperienceLevelHolder experienceLevelHolder) {
		this.experienceLevelHolder = experienceLevelHolder;
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
			if (experienceLevelHolder == null)
				sb.append("experienceLevelHolder null");
			else {
				sb.append("experienceLevelHolder: ");
				experienceLevelHolder.toString(sb, tabLevel + 1);
			}
		}
	}
}