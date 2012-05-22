package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.model.tree.Tree2D;
import dk.itu.kf04.g4tw.util.DynamicArray;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * A Model of the map-data. The model is currently split into 8 different {@link Tree2D}s to simplify
 * the search of particular road-types.
 *
 * @author Nicolai Krüger <nkrk@itu.dk>
 * @author Jens Egholm <jegp@itu.dk>
 */
public class MapModel extends DijkstraSP<Road> implements Externalizable {

    public static final int HIGHWAY        = 1;
    public static final int EXPRESSWAY     = 2;
    public static final int PRIMARY_ROAD   = 4;
    public static final int SECONDARY_ROAD = 8;
    public static final int MINOR_ROAD     = 16;
    public static final int PATH           = 32;
    public static final int SEAWAY         = 64;
    public static final int LOCATION       = 128;

    public static final int numberOfRoads = 812301; // Number of roads + 1. ID = index in arrays. (ID starts at 1)

    /**
     * The logger for the class
     */
    static Logger Log = Logger.getLogger(MapModel.class.getName());

    /**
     * An array containing all the roads.
     */
    protected Road[] roads = new Road[numberOfRoads];
    
    /**
     * Maps relationships between road-ids given by the input-file and road-types given as static fields in the MapModel.
     */
    protected HashMap<Integer, Integer> roadTypeMap = new HashMap<Integer, Integer>();

    /**
     * Contains a map between the different road-types and the corresponding Search-Trees.
     */
    protected HashMap<Integer, Tree2D> roadTrees = new HashMap<Integer, Tree2D>();

    /**
     * Instantiates a MapModel and maps different road-types to the correct trees.
     */
    public MapModel() {
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
     * Adds a road to the Model with the given id.
     * @param road The road to add.
     */
	public void addRoad(Road road) {
        // Add the road to our array
        roads[road.id] = road;
        
        // Construct the tree if it does not exist
        if (!roadTrees.containsKey(road.type)) roadTrees.put(road.type, new Tree2D(this));
        
        // Insert
        roadTrees.get(road.type).addNode(road);
        
        // Log
        Log.fine("Successfully added road: " + road);
	}

    /**
     * Retrieves a road from a given index
     * @param index  The unique index for the road
     * @return The road with the given index or null if no road could be found
     */
    public Road getEdge(int index) { return roads[index]; }

    /**
     * Retrieves a road from a given index
     * @param index  The unique index for the road
     * @return The road with the given index or null if no road could be found
     */
    public Road getRoad(int index) { return roads[index]; }

    /**
     * Retrieves the underlying collection of roads.
     * @return  An array with roads.
     */
    public Road[] getRoads() { return roads; }

    /**
     * Insert a map from a set of given road-ids to a road-type.
     * @param type  The type of the road, given in MapModel
     * @param values The set of road-ids to map.
     */
    protected void setTypeReference(int type, int... values) {
        // Insert a map from the road id to the road type
        for (int v : values) {
            roadTypeMap.put(v, type);
        }
    }

    /**
     * Retrieves the road-type from the given id.
     * @param id  The id of the road.
     * @return  The road-type given in the MapModel.
     */
    public int getRoadTypeFromId(int id) {
        return roadTypeMap.get(id);
    }

    /**
     * Searches through the model for roads that intersects the window-query represented by
     * the coordinates given.
     * @param xMin The x-value of the upper left corner of the window-query
     * @param yMin The y-value of the upper left corner of the window-query
     * @param xMax The x-value of the lower right corner of the window-query
     * @param yMax The y-value of the lower right corner of the window-query
     * @param type The type of roads to search in
     * @return  An array containing the search result
     */
	public DynamicArray<Road> search(double xMin, double yMin, double xMax, double yMax, int type) {

        // Test input - avoid empty searches
        if (type < 1 || type >= 256) throw new IllegalArgumentException("Unable to search in roads of type " + type + ".");

        DynamicArray<Road> results = new DynamicArray<Road>();
        for (Map.Entry<Integer, Tree2D> entry : roadTrees.entrySet()) {
            int key = entry.getKey();
            // Test if the type contains the key-byte (bitwise AND)
            if ((key & type) == key) {
                roadTrees.get(key).search(results, xMin, yMin, xMax, yMax);
            }
        }

		return results;
	}

    public void writeExternal(ObjectOutput out) throws IOException {
        // Write the roads
        int i = 0;
        for (; i < numberOfRoads; i++) {
            Road r = roads[i];
            out.writeInt(r.id);             // id
            out.writeUTF(r.name);           // name
            out.writeDouble(r.from.getX()); // from
            out.writeDouble(r.from.getY());
            out.writeDouble(r.to.getX());   // to
            out.writeDouble(r.to.getY());
            out.writeInt(r.type);           // type
            out.writeDouble(r.speed);       // speed
            out.writeDouble(r.length);      // length
            out.writeInt(r.startNumber);    // start number
            out.writeInt(r.endNumber);      // end number
            out.writeUTF(r.startLetter);    // start letter
            out.writeUTF(r.endLetter);      // end letter
            out.writeInt(r.leftPostalCode); // left postal code
            out.writeInt(r.rightPostalCode);// right postal code
            // Connections
            out.writeInt(r.numberOfEdges()); // Number of edges
            for (Integer connection : r) {
                out.writeInt(connection);    // Edge
            }
            
        }

        // Write the trees
        for (Map.Entry<Integer, Tree2D> entry : roadTrees.entrySet()) {
            // Write the road id
            out.writeInt(entry.getKey());
            
            // Write the tree
            entry.getValue().writeExternal(out);
        }
        
        // Log
        Log.fine("Successfully wrote MapModel to external.");
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // Hashmap of the roads with a name
        HashMap<String, DynamicArray<Road>> namedRoads = new HashMap<String, DynamicArray<Road>>();

        // Read the roads
        int i = 0;
        for (; i < numberOfRoads; i++) {
			Road tmp = new Road(
				in.readInt(),    // id
				in.readUTF(),    // name
				new Point2D.Double(in.readDouble(), in.readDouble()), // from
				new Point2D.Double(in.readDouble(), in.readDouble()), // to
				in.readInt(),    // type
				in.readDouble(), // speed
				in.readDouble(), // length
				in.readInt(),    // start number
				in.readInt(),    // end number
				in.readUTF(),    // start letter
				in.readUTF(),    // end letter
				in.readInt(),    // left postal code
				in.readInt()     // right postal code
			);
            
            // Add connections
            int connections = in.readInt();
            for (int n = 0; n < connections; n++) {
                tmp.addEdge(in.readInt());
            }
            
            // Add the road to the named roads map
            if(tmp.name.length() > 2) {
                String name = tmp.name.toLowerCase();
                // If the road-name is not yet in the namedRoads-hashmap, add it
                if(!namedRoads.containsKey(name))
                    namedRoads.put(name, new DynamicArray<Road>());

                // Add the road to the corresponding collection
                namedRoads.get(name).add(tmp);
            }

            roads[i] = tmp;
        }

        // Set the namedRoads hashmap in AddressParser
        AddressParser.setNamedRoads(namedRoads);

        // Read the trees (as many as possible)
        try {
            while (true) {
                // Find the type of the roads the tree contains
                int roadType = in.readInt();
                
                // Read in the tree!
                Tree2D t = new Tree2D(this);
                t.readExternal(in);

                // Insert it
                roadTrees.put(roadType, t);
            }
        } catch (EOFException e) {
            // Expected
        }
        
        // Log
        Log.fine("Successfully read MapModel from external.");
    }

}
