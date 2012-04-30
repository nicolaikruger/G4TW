package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.model.DijkstraEdge;
import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Road;
import dk.itu.kf04.g4tw.util.DynamicArray;
import dk.itu.kf04.g4tw.util.RoadParser;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.logging.Logger;

/**
 * The DataStore can store already parsed map-data loaded from the {@link RoadParser}
 * and retrieve it for later use. It significantly reduces load-time.
 */
public class DataStore {

    protected static final String roadFile = "roads.bin";
    protected static final String edgeFile = "edges.bin";
    
    public static Logger Log = Logger.getLogger(DataStore.class.getName());

    /**
     * Retrieves the data from the krak data-files and stores them in two binary files - roads.bin for
     * roads and edges.bin for edges.
     * @param args  The arguments for the run method.
     */
    public static void main(String[] args) {
        // Load the roads
        DynamicArray<Road> roads = RoadParser.load(new File("kdv_node_unload.txt"), new File("kdv_unload.txt"));

        try {
            // Create the output streams
            DataOutputStream ros = new DataOutputStream(new FileOutputStream(roadFile));
            DataOutputStream eos = new DataOutputStream(new FileOutputStream(edgeFile));

            // Log status
            Log.info("Starting import...");
            
            // Write the number of roads
            ros.writeInt(roads.length());
            
            // Iterate over the roads
            for (int i = 0; i < roads.length(); i++) {
                Road road = roads.get(i);
                ros.writeUTF(road.name);
                ros.writeDouble(road.from.getX()); // From
                ros.writeDouble(road.from.getY());
                ros.writeDouble(road.to.getX()); // To
                ros.writeDouble(road.to.getY());
                ros.writeInt(road.type);
                ros.writeDouble(road.speed);
                ros.writeDouble(road.getLength());
                
                // Iterate the edges in the road
                for (DijkstraEdge d : road) {
                    eos.writeInt(road.getId());
                    eos.writeInt(d.getId());
                }
                
                if (i != 0 && i % 100000 == 0) Log.info("Status: Successfully stored " + i + " roads and edges.");
            }
            
            // Flush!
            ros.flush();
            eos.flush();
            
            // Log success.
            Log.info("Successfully wrote roads and edges to file.");
        } catch (FileNotFoundException e) {
            Log.severe("Error storing map-data. Could not find file: " + e.getMessage());
        } catch (IOException e) {
            Log.warning("Failure while writing roads to disk.");
            e.printStackTrace();
        }
    }

    /**
     * This method loads the roads and edges from the data-files generated in the main method above
     * and adds them to the {@link MapModel}.
     */
    public static void loadRoads() {
        DataInputStream is;
        DynamicArray<Road> roads;
        long time = System.currentTimeMillis();
        
        // Load roads and edges
        try {
            is  = new DataInputStream(new FileInputStream(roadFile)); 
            
            // Read the number of roads
            int numberOfRoads = is.readInt();
            roads = new DynamicArray<Road>(numberOfRoads);
            
            // Find roads
            try {
                while (true) {
                    String name         = is.readUTF();
                    Point2D.Double from = new Point2D.Double(is.readDouble(), is.readDouble());
                    Point2D.Double to   = new Point2D.Double(is.readDouble(), is.readDouble());
                    int type            = is.readInt();
                    double speed        = is.readDouble();
                    double length       = is.readDouble();
                    Road road = new Road(roads.length(), name, from, to, type, speed, length);

                    MapModel.addRoad(road);
                    roads.add(road);
                }
            } catch (IOException e) {} // Expected 
            
            // Close stream
            is.close();
            
            // Load edges
            is  = new DataInputStream(new FileInputStream(edgeFile));
            try {
                while (true) {
                    int from = is.readInt();
                    int to   = is.readInt();
                    roads.get(from).addEdge(roads.get(to));
                }
            } catch (IOException e) {} // Expected

            // Close the stream
            is.close();
            
            // Log success!
            Log.info("Import done in " + ((System.currentTimeMillis() - time) / 1000) + " seconds. Read " + roads.length() + " roads.");
            
        } catch (FileNotFoundException e) {
            Log.severe("Error loading data. Could not locate file: " + e.getMessage());
        } catch (IOException e) {
            Log.severe("Error loading data: Bad format. Please recompile.");
        }
    }
}
