package common.gui;

import java.awt.Component;

public interface GuiCardComponent {

	public String getNameCard();

	public Component getComponent();

	//

	// public GuiCardComponent setNameCard(String nameCard);
	// public GuiCardComponent setComponent(Component component);

	//

	public void init();

	public void doOnShowing();
}