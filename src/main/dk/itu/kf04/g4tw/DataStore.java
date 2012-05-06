package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.util.RoadParser;

import java.io.*;
import java.util.logging.Logger;

/**
 * The DataStore can store already parsed map-data loaded from the {@link RoadParser}
 * and retrieve it for later use. It significantly reduces load-time.
 */
public class DataStore {

    protected static final File dataFile = new File("data.bin");
    
    public static Logger Log = Logger.getLogger(DataStore.class.getName());

    /**
     * Retrieves the data from the krak data-files and stores them in two binary files - roads.bin for
     * roads and edges.bin for edges.
     * @param args  The arguments for the run method.
     */
    public static void main(String[] args) {          
        storeRoads();
    }

    /**
     * This method loads the roads and edges from the data-files generated in the main method above
     * and adds them to the {@link MapModel}.
     * @return  A MapModel containing the fetched data.
     */
    public static MapModel loadRoads() {
        // Create the model
        MapModel model = new MapModel();

        // Log status
        long start = System.currentTimeMillis();

        // Create the binary file if it doesn't exist
        if (!dataFile.exists()) {
            Log.info("Could not locate " + dataFile + ". Compiling data anew.");
            storeRoads();
        }
        
        // Load roads and edges
        try {
            // Create the stream from the data file
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(dataFile));

            // Read the roads
            model.readExternal(is);
            
            // Close stream
            is.close();

            // Log success!
            Log.info("Map-data successfully loaded in " + ((System.currentTimeMillis() - start)) + "ms.");

        } catch (FileNotFoundException e) {
            Log.severe("Error loading data. Could not locate file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.severe("Error loading data. Data was malformed: " + e.getMessage());
        } catch (ClassCastException e) {
            Log.severe("Error loading data. Could not cast to HashMap: " + e.getMessage());
        } catch (IOException e) {
            Log.severe("Error loading data, bad format.");
            e.printStackTrace();
        }

        // Return the model
        return model;
    }

    /**
     * Stores the MapModel
     */
    public static void storeRoads() {
        // Log starting
        Log.info("Loading map data from files...");

        // Load the roads
        MapModel model = RoadParser.load(new File("kdv_node_unload.txt"), new File("kdv_unload.txt"));

        // Log status
        Log.info("Starting binary compression of data...");

        try {
            // Create the output streams
            FileOutputStream fs   = new FileOutputStream(dataFile);
            ObjectOutput out      = new ObjectOutputStream(fs);

            // Write all the roads
            model.writeExternal(out);

            // Flush!
            out.flush();

            // Close
            out.close();

            // Log success.
            Log.info("Successfully wrote data to file.");
        } catch (FileNotFoundException e) {
            Log.severe("Error storing map-data. Could not find file: " + e.getMessage());
        } catch (IOException e) {
            Log.warning("Failure while writing roads to disk.");
            e.printStackTrace();
        }
    }
}
