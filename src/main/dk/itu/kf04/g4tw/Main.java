package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.controller.WebServer;
import dk.itu.kf04.g4tw.model.MapModel;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry-point for the application.
 */
public class Main {

    // General TODOs for the project
    // TODO: Clean up in the javascript code
    // TODO: Make som white- and blackbox testing
    // TODO: Fix HTML layout
    // TODO: Fix JavaScript load time
    // TODO: Krüger: Comment Road - Especially all the fields
    // TODO: Krüger: Comment Dijkstras methods
    // TODO: Krüger: Compile the turn-file into binary
    // TODO: Krüger: Move trim() to RoadParser
    // TODO: Krüger: Der er en test i AddressParser der fejler - fikser du den? :P

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
        Log.info("Program starting up. Importing map-data...");
        
        // Import data
        MapModel model = DataStore.loadRoads();

        // Start the server on port 80
        new WebServer(model, 80);
    }

}