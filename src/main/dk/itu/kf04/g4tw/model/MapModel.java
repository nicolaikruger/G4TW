package dk.itu.kf04.g4tw.model;

import java.util.HashMap;

/**
 * A Model of the map-data. This class is responsible for 
 */
public class MapModel {

    public static final int HIGHWAY        = 1;
    public static final int EXPRESSWAY     = 2;
    public static final int PRIMARY_ROAD   = 4;
    public static final int SECONDARY_ROAD = 8;
    public static final int MINOR_ROAD     = 16;
    public static final int PATH           = 32;
    public static final int SEAWAY         = 64;
    public static final int LOCATION       = 128;

    private int i = 1;

    protected HashMap<Integer, Integer> mapTypeReference = new HashMap<Integer, Integer>();
    protected HashMap<Integer, RoadTypeTree> roadTrees = new HashMap<Integer, RoadTypeTree>();

    protected void loadTypeReference(int type, int... values) {
        // Create the tree
		roadTrees.put(type, new RoadTypeTree(type));

        // Insert a map from the road id to the road type
        for (int i = 0; i < values.length; i++) {
			mapTypeReference.put(values[i], type);
        }
    }

    /**
     * Instantiates the MapModel with the given directory.
     */
    public MapModel() {
		// Added 0, 31, 95

        loadTypeReference(HIGHWAY,        1, 21, 31, 41);
        loadTypeReference(EXPRESSWAY,     2, 22, 32, 42);
        loadTypeReference(PRIMARY_ROAD,   3, 23, 33, 43);
        loadTypeReference(SECONDARY_ROAD, 4, 24, 34, 44, 95);
        loadTypeReference(MINOR_ROAD,     0, 5, 25, 26, 6, 35, 45, 46);
        loadTypeReference(PATH,           8, 28, 48, 10, 11);
        loadTypeReference(SEAWAY,         80);
        loadTypeReference(LOCATION,       99);
    }
	
	public String getXML(double xMin, double yMin, double xMax, double yMax, int... type)
	{
		String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
				"<roadCollection xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
				"xsi:noNamespaceSchemaLocation=\"localhost/kraX.xsd\"" +
				"xmlns=\"http://www.w3schools.com\">";
		for(Integer i : type)
		{
			RoadTypeTree tree = roadTrees.get(i);
			String newXML = tree.search(xMin, yMin, xMax, yMax);
			xmlData += newXML;
		}

		xmlData += "</roadCollection>";

		return xmlData;
	}

	public void addRoad(Road road)
	{
		int roadType = road.getType();
		int treeType = mapTypeReference.get(roadType);
		RoadTypeTree tree = roadTrees.get(treeType);
		tree.addNode(road);
	}

}
