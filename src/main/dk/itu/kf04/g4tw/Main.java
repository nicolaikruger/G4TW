package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.controller.WebServer;
import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.MapParser;

import java.io.File;
import java.util.logging.Logger;

/**
 * Main entry-point for the application.
 */
public class Main {

    static Logger Log = Logger.getLogger(Main.class.getName());
    
    /**
     * Starts the application by initializing data and then starting server.
     * @param args  The arguments to feed the application.
     */
    public static void main(String[] args) {
        // Log start
        Log.finest("Main starting up.");
        
        // Find the files
        File nodeFile = new File("krak/kdv_node_unload.txt");
        File edgeFile = new File("krak/kdv_unload.txt");

        Log.finest("Starting import of map-data.");
        
        // Import data
        MapModel model = MapParser.load(nodeFile, edgeFile);
        
        Log.finest("Import of map-data done.");

        // Init server
        boolean success = WebServer.init();

        // Log statis
        if (success) {
            Log.info("Program initialized correct and web-server has been started.");
        } else {
            Log.warning("Program initialized correct but web-server failed to start.");
        }
    }

}