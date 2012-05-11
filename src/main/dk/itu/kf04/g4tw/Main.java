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
    // TODO (?): Include turn.txt in binary compression
    // TODO: Comment Road - Especially all the fields
    // TODO: Comment RoadRectangle
    // TODO: Comment Dijkstras methods
    // TODO: Clean up in the code
    // TODO: Solve other TODOs
    // TODO: Make som white- and blackbox testing
    // TODO: Go make me a sandwich

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