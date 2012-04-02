package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * Searches through the model for roads.
     * @param xMin
     * @param yMin
     * @param xMax
     * @param yMax
     * @param type
     * @return
     */
	public DynamicArray<Road> search(double xMin, double yMin, double xMax, double yMax, int type)
	{
        DynamicArray<Road> results = new DynamicArray<Road>();
        for (Map.Entry<Integer, RoadTypeTree> entry : roadTrees.entrySet()) {
            int key = entry.getKey();
            // Test if the type contains the key-byte (bitwise AND)
            if ((key & type) == key) {
                DynamicArray<Road> roads = roadTrees.get(key).search(xMin, yMin, xMax, yMax);
                results.add(roads);
            }
        }

		return results;
	}

    /**
     * Adds a road to the Model.
     * @param road The road to add.
     */
	public void addRoad(Road road) {
		int roadType = road.getType();
		int treeType = mapTypeReference.get(roadType);
		RoadTypeTree tree = roadTrees.get(treeType);
		tree.addNode(road);
	}

}
