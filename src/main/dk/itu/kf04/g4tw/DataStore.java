package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.util.RoadParser;

import java.io.*;
import java.util.logging.Logger;

/**
 * The DataStore can store and read already parsed map-data loaded from the {@link RoadParser}
 * and retrieve it for later use. The binary format significantly reduces load-time.
 */
public class DataStore {

    /**
     * The data-file in which to store the binary compressed data.
     */
    static final File dataFile = new File("data.bin");

    /**
     * The nodes, i. e. the points, on the map.
     */
    static final File nodes = new File("kdv_node_unload.txt");

    /**
     * The edges, i. e. the roads, on the map.
     */
    static final File edges = new File("kdv_unload.txt");

    /**
     * The file containing directed information about the roads.
     */
    static final File turn  = new File("turn.txt");

    /**
     * The logger for the DataStore.
     */
    public static Logger Log = Logger.getLogger(DataStore.class.getName());

    /**
     * This method loads the roads and edges from the data-files generated in the main method above
     * and adds them to the {@link MapModel}.
     * @return  A MapModel containing the fetched data.
     * @throws IOException When encountering an IOException.
     * @throws ClassNotFoundException If the binary loading fails to cast to the right class.
     */
    public static MapModel loadRoads() throws IOException, ClassNotFoundException {
        // Create the model
        MapModel model = new MapModel();

        // Log status
        long start = System.currentTimeMillis();

        // Create the binary file if it doesn't exist
        if (!dataFile.exists()) {
            Log.info("Could not locate " + dataFile + ". Compiling data anew.");
            storeRoads();
        } else {
            Log.info("Loading map-data from " + dataFile + ".");
        }

        // Create the stream from the data file
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(dataFile));

        // Read the roads
        model.readExternal(is);

        // Close stream
        is.close();

        // Log success!
        Log.info("Map-data successfully loaded in " + ((System.currentTimeMillis() - start)) + "ms.");

        // Return the model
        return model;
    }

    /**
     * Stores the MapModel in a binary data-file loaded from the
     * @throws FileNotFoundException If one or more of the data-files could not be found. 
     */
    public static void storeRoads() throws FileNotFoundException {
        MapModel model = RoadParser.load(nodes, edges, turn);

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
