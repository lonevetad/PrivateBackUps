package games.theRisingAngel.loaders;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import games.generic.controlModel.loaders.LoaderUniqueIDProvidersState;
import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import games.theRisingAngel.enums.CreatureTypesTRAn;
import tools.json.JSONParser;
import tools.json.JSONValue;
import tools.json.types.JSONObject;

public class LoaderUniqueIDProvidersStateTRAn extends LoaderUniqueIDProvidersState {

	public LoaderUniqueIDProvidersStateTRAn() { super(); }

	@Override
	public void enrichAllKnownUIDPLoadedListenerList(List<Map.Entry<Class<?>, UIDProviderLoadedListener>> list) {
		super.enrichAllKnownUIDPLoadedListenerList(list);

		list.add(Map.entry(CreatureTypesTRAn.class, CreatureTypesTRAn.UIDP_LOAD_LISTENER_CREATURE_TYPES_TRAn));
//		list.add(Map.entry(CreatureTypesTRAn.class, CreatureTypesTRAn.UIDP_LOAD_LISTENER_CREATURE_TYPES_TRAn));

		// TODO do others?
	}

	@Override
	protected Iterable<UIDPState> readSavedUIDPStates() {
		// TODO Auto-generated method stub
		Iterable<UIDPState> iterStates;
		Iterator<JSONValue> iterRaw;

		iterRaw = JSONParser.iterableArrayElements(
				// TODO FILENAME
				null);

		iterStates = new Iterable<UIDPState>() {
			final Iterator<JSONValue> ir_ = iterRaw;

			@Override
			public Iterator<UIDPState> iterator() {

				return new Iterator<UIDPState>() {
					final Iterator<JSONValue> ir__ = ir_;

					@Override
					public UIDPState next() {
						JSONObject jo = (JSONObject) ir__.next();
						return new UIDPState(jo.getFieldValue("classIdentifier").asString(),
								jo.getFieldValue("state").asLong());
					}

					@Override
					public boolean hasNext() { return iterRaw.hasNext(); }
				};
			}
		};

		return iterStates;
	}
}