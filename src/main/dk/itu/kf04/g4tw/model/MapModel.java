package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.HashMap;
import java.util.Map;

/**
 * A Model of the map-data. The model is currently split into 8 different {@link RoadTypeTree}s to easen
 * the search of particular road-types.
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

    /**
     * Maps relationships between road-ids given by the input-file and road-types given as static fields in the MapModel.
     */
    protected static HashMap<Integer, Integer> roadTypeMap = new HashMap<Integer, Integer>();

    /**
     * Contains a map between the different road-types and the corresponding Search-Trees.
     */
    protected static HashMap<Integer, RoadTypeTree> roadTrees = new HashMap<Integer, RoadTypeTree>();

    // Add road-type references
    static {
        setTypeReference(HIGHWAY, 1, 21, 31, 41);
        setTypeReference(EXPRESSWAY, 2, 22, 32, 42);
        setTypeReference(PRIMARY_ROAD, 3, 23, 33, 43);
        setTypeReference(SECONDARY_ROAD, 4, 24, 34, 44, 95);
        setTypeReference(MINOR_ROAD, 0, 5, 25, 26, 6, 35, 45, 46);
        setTypeReference(PATH, 8, 28, 48, 10, 11);
        setTypeReference(SEAWAY, 80);
        setTypeReference(LOCATION, 99);
    }

    /**
     * Adds a road to the Model.
     * @param road The road to add.
     */
	public static void addRoad(Road road) {
        // Construct the tree if it does not exist
        if (!roadTrees.containsKey(road.type)) roadTrees.put(road.type, new RoadTypeTree(road.type));
        
        // Insert
        roadTrees.get(road.type).addNode(road);
	}

    /**
     * Replaces the road-map with the given input.
     */
    public static void setRoads(HashMap<Integer, RoadTypeTree> map) { roadTrees = map; }

    /**
     * Insert a map from a set of given road-ids to a road-type.
     * @param type  The type of the road, given in MapModel
     * @param values The set of road-ids to map.
     */
    protected static void setTypeReference(int type, int... values) {
        // Insert a map from the road id to the road type
        for (int i = 0; i < values.length; i++) {
            roadTypeMap.put(values[i], type);
        }
    }

    /**
     * Retrieves the entire data-structure for the model.
     * @return A hash map containing the integer-types and the roads mapped to the types.
     */
    public static HashMap<Integer, RoadTypeTree> getRoads() { return roadTrees; }

    /**
     * Retrieves the road-type from the given id.
     * @param id  The id of the road.
     * @return  The road-type given in the MapModel.
     */
    public static int getRoadTypeFromId(int id) {
        return roadTypeMap.get(id);
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
	public static DynamicArray<Road> search(double xMin, double yMin, double xMax, double yMax, int type)
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

}
