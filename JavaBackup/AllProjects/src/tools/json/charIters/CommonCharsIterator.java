package tools.json.charIters;

import tools.json.CharactersIterator;

public abstract class CommonCharsIterator implements CharactersIterator {

	public CommonCharsIterator() { this.isOpen = true; }

	protected boolean isOpen;

	protected void checkIsOpen() { if (!this.isOpen) { throw new IllegalStateException("The iterator is closed"); } }

	@Override
	public void closeIterator() { this.isOpen = false; }
}
