package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.RoadTypeTree;
import dk.itu.kf04.g4tw.util.RoadParser;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * The DataStore can store already parsed map-data loaded from the {@link RoadParser}
 * and retrieve it for later use. It significantly reduces load-time.
 */
public class DataStore {

    protected static final String dataFile = "data.bin";
    
    public static Logger Log = Logger.getLogger(DataStore.class.getName());

    /**
     * Retrieves the data from the krak data-files and stores them in two binary files - roads.bin for
     * roads and edges.bin for edges.
     * @param args  The arguments for the run method.
     */
    public static void main(String[] args) {          
        // Log starting
        Log.info("Loading map data from files...");
        
        // Load the roads
        RoadParser.load(new File("kdv_node_unload.txt"), new File("kdv_unload.txt"));
        
        // Log status
        Log.info("Starting binary compression of data...");

        try {
            // Create the output streams
            FileOutputStream fs   = new FileOutputStream(dataFile);
            ObjectOutputStream os = new ObjectOutputStream(fs);

            // Write all the roads
            os.writeObject(MapModel.getRoads());
            
            // Flush!
            os.flush();
            
            // Close
            os.close();
            
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
        long time = System.currentTimeMillis();
        
        // Log status
        Log.info("Importing roads...");
        
        // Load roads and edges
        try {
            // Create the stream from the data file
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(dataFile));
                        
            // Read the roads
            Object roads = is.readObject();
            
            // Cast to HashMap
            HashMap<Integer, RoadTypeTree> map = (HashMap<Integer, RoadTypeTree>) roads;
            
            // Insert into the model
            MapModel.setRoads(map);
            
            // Close stream
            is.close();
            
            // Log success!
            Log.info("Import done in " + ((System.currentTimeMillis() - time) / 1000) + " seconds.");

        } catch (FileNotFoundException e) {
            Log.severe("Error loading data. Could not locate file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.severe("Error loading data. Data was malformed: " + e.getMessage());
        } catch (ClassCastException e) {
            Log.severe("Error loading data. Could not cast to HashMap: " + e.getMessage());
        } catch (IOException e) {
            Log.severe("Error loading data: Bad format. Please recompile.");
        }
    }
}
