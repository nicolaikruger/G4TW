package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.controller.MapController;
import dk.itu.kf04.g4tw.model.MapModel;

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
        Log.setLevel(Level.ALL);
        
        // Log program start
        Log.info("Main starting up. Importing map-data...");
        
        // Import data
        MapModel model = DataStore.loadRoads();
        
        Log.info("Import of map-data done. Starting server...");

        // Start the controller
        new MapController(model);
    }

}