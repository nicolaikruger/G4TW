package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Road;
import dk.itu.kf04.g4tw.model.RoadTypeTree;
import dk.itu.kf04.g4tw.util.DynamicArray;
import dk.itu.kf04.g4tw.util.RoadParser;
import sun.rmi.runtime.Log;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 *
 */
public class DataStore {
    
    protected static String file = "data.bin";
    
    public static Logger Log = Logger.getLogger(DataStore.class.getName());
    
    public static void main(String[] args) {
        // Find the files
        File nodeFile = new File("kdv_node_unload.txt");
        File edgeFile = new File("kdv_unload.txt");
        
        // Load the roads
        DynamicArray<Road> roads = RoadParser.load(nodeFile, edgeFile);

        try {
            // Create the output stream
            DataOutputStream os = new DataOutputStream(new FileOutputStream(file));
            
            // Iterate over the roads
            for (int i = 0; i < roads.length(); i++) {
                Road road = roads.get(i);
                os.writeInt(road.id);
                os.writeUTF(road.name);
                os.writeDouble(road.from.getX()); // From
                os.writeDouble(road.from.getY());
                os.writeDouble(road.to.getX()); // To
                os.writeDouble(road.to.getY());
                os.writeInt(road.type);
                os.writeDouble(road.speed);
                os.writeDouble(road.getLength());
            }
            
            // Flush!
            os.flush();
            
            // Log success.
            Log.info("Successfully wrote to file.");
        } catch (IOException e) {
            Log.warning("Failure while writing roads to disk.");
            e.printStackTrace();
        }
    }
    
    public static MapModel loadRoads() {
        HashMap<Integer, RoadTypeTree> roads = new HashMap<Integer, RoadTypeTree>();

        int numberOfRoads = 0;

        try {
            DataInputStream is  = new DataInputStream(new FileInputStream(file));
            while (true) {
                int id              = is.readInt();
                String name         = is.readUTF();
                Point2D.Double from = new Point2D.Double(is.readDouble(), is.readDouble());
                Point2D.Double to   = new Point2D.Double(is.readDouble(), is.readDouble());
                int type            = is.readInt();
                double speed        = is.readDouble();
                double length       = is.readDouble();
                addRoad(roads, new Road(id, name, from, to, type, speed, length));
                numberOfRoads++;
            }
        } catch (IOException e) {} // Expected

        System.out.println("Import done. Read " + numberOfRoads + " roads.");
        return new MapModel(roads);
    }

    /**
     * Adds a road to the Model.
     * @param road The road to add.
     */
	protected static void addRoad(HashMap<Integer, RoadTypeTree> roads, Road road) {
        // Construct the tree if it does not exist
        if (!roads.containsKey(road.type)) roads.put(road.type, new RoadTypeTree(road.type));
        
        // Insert
		roads.get(road.type).addNode(road);
	}
    
}
