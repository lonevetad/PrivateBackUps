package games.theRisingAngel;

import java.io.File;

import games.generic.controlModel.GController;
import games.generic.controlModel.misc.LoaderGeneric;

/**
 * should use Maven:
 * 
 * <pre>
 * <code>
 * < !-- https://mvnrepository.com/artifact/org.json/json -- >
	< dependency>
	    < groupId>org.json< /groupId>
	    < artifactId>json< /artifactId>
	    < version>20190722< /version>
	< /dependency>
 * </code>
 * </pre>
 * 
 */
public class LoaderConfigurations extends LoaderGeneric {

	public static final String RESOURCE_REPOSITORY = "TheRisingAngel";
	public static final String RESOURCE_REPOSITORY_PULL_FACT = LoaderGeneric.startPath + RESOURCE_REPOSITORY
			+ File.separatorChar;

	public LoaderConfigurations() {}

	@Override
	public void loadInto(GController gc) {
//		GControllerTRAn gcTrar;
//		GameObjectsProvidersHolderTRAn gophTrar;
		GameOptionsTRAn go;
//		JSONParser jsonReader;
//		gcTrar = (GControllerTRAn) gc;
//		gophTrar = (GameObjectsProvidersHolderTRAn) gcTrar.getGameObjectsProvider();
//		jsonReader=new JSONParser(source, global, dualFields)
		go = new GameOptionsTRAn();
		go.loadConfig();
		System.out.println("Loader Configurations loaded: ");
		//
//		gophTrar.setEquipItemsWeights(go.equipWeights);
		go.probabilitiesContexes.getWeightedIndexesContextes().forEach((nameContex, contextWeightsProbabilities) -> {
			System.out.println("context weights probabilities: " + contextWeightsProbabilities);
			gc.getProbabilityOfContextesHolders().addWeightedIndexesContext(nameContex, contextWeightsProbabilities);
		});
	}
}