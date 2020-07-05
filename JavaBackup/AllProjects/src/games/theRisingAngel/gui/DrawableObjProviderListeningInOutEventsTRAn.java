package games.theRisingAngel.gui;

import games.generic.view.DrawableInstantiator;
import games.generic.view.dataProviders.DrawableObjProviderListeningInOutEvents;
import games.generic.view.dataProviders.ObjLocatedProvider;
import games.theRisingAngel.events.EventsTRAn;
import tools.ObjectNamedID;

public class DrawableObjProviderListeningInOutEventsTRAn extends DrawableObjProviderListeningInOutEvents {
	private static final long serialVersionUID = -154021422000L;

	public DrawableObjProviderListeningInOutEventsTRAn(ObjLocatedProvider objLocatedProvider,
			DrawableInstantiator drawableCreator) {
		super(objLocatedProvider, drawableCreator);
	}

	@Override
	public ObjectNamedID getIdentifierObjEnteredInGame() { return EventsTRAn.ObjectAdded; }

	@Override
	public ObjectNamedID getIdentifierObjRemovedFromGame() { return EventsTRAn.ObjectRemoved; }
}