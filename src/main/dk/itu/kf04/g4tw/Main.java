package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.controller.MapController;
import dk.itu.kf04.g4tw.util.RoadParser;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry-point for the application.
 */
public class Main {

    /**
     * The logging-util for the Main class.
     */
    protected static Logger Log = Logger.getLogger(Main.class.getName());
    
    /**
     * Starts the application by initializing data and then starting server.
     * @param args  The arguments to feed the application.
     */
    public static void main(String[] args) {
        System.setProperty("file.encoding", "ISO8859_1");
        Log.setLevel(Level.ALL);
        
        // Log program start
        Log.info("Main starting up. Importing map-data...");
        
        // Import data
        //DataStore.loadRoads();
        RoadParser.load(new File("kdv_node_unload.txt"), new File("kdv_unload.txt"));
        
        Log.info("Import of map-data done. Starting server...");

        // Start the controller
        new MapController();
    }

}