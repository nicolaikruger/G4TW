package dk.itu.kf04.g4tw.model;

import java.util.HashMap;

/**
 * A map
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
        for (int i = 0; i < values.length; i++) {
			mapTypeReference.put(values[i], type);
        }
    }

    public MapModel() {
        loadTypeReference(HIGHWAY,        1, 21, 41);
        loadTypeReference(EXPRESSWAY,     2, 22, 32, 42);
        loadTypeReference(PRIMARY_ROAD,   3, 23, 33, 43);
        loadTypeReference(SECONDARY_ROAD, 4, 24, 34, 44);
        loadTypeReference(MINOR_ROAD,     5, 25, 6, 35, 45, 46);
        loadTypeReference(PATH,           8, 28, 48, 10, 11);
        loadTypeReference(SEAWAY,         80);
        loadTypeReference(LOCATION,       99);

        roadTrees.put(HIGHWAY, new RoadTypeTree(HIGHWAY));
    }
    
    // ... //


}
