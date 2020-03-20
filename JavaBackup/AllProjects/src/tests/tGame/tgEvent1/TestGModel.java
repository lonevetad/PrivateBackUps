package tests.tGame.tgEvent1;

import java.util.Set;
import java.util.function.Consumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GModel;
import games.generic.controlModel.GObjectsHolder;
import games.generic.controlModel.gameObj.TimedObject;
import games.generic.controlModel.subImpl.GameModelTimeBased;

public class TestGModel {

	public static void main(String[] args) {
		GModel gm;
		gm = new GModel() {
			@Override
			public void onCreate() {
				System.out.println("CIAOOOO");
			}
		};
		System.out.println("helooo");
		gm.addObjHolder("TEST", new GObjectsHolder<TimedObject>() {

			@Override
			public Set<TimedObject> getObjects() {
				return null;
			}

			@Override
			public boolean add(TimedObject o) {
				return false;
			}

			@Override
			public boolean remove(TimedObject o) {
				return false;
			}

			@Override
			public boolean contains(TimedObject o) {
				return false;
			}

			@Override
			public void forEach(Consumer<TimedObject> action) {
			}
		});
		System.out.println("FINE");
		GameModelTimeBased gmt;
		gmt = new GameModelTimeBased() {

			@Override
			public void onCreate() {
				System.out.println("REAL timed model");
			}
		};
		gmt.addTimedObject(new TimedObject() {
			@Override
			public Integer getID() {
				return 777;
			}

			@Override
			public void act(GModality modality, long milliseconds) {
				System.out.println("time " + milliseconds);
			}
		});
		System.out.println("fine 2");
		gmt.forEach(o -> System.out.println(o.getID()));
	}
}
