package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.controller.MapArtist;
import dk.itu.kf04.g4tw.controller.WebServer;
import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.MapParser;

import java.io.File;

/**
 * Main entry-point for the application.
 */
public class Main {

    /**
     * Starts the application by initializing data and then starting server.
     * @param args  The arguments to feed the application.
     */
    public static void main(String[] args) {
        // Find the files
        File nodeFile = new File("krak/kdv_node_unload.txt");
        File edgeFile = new File("krak/kdv_unload.txt");

        // Import data
        MapModel model = MapParser.load(nodeFile, edgeFile);

        System.out.println("Done!");

		// Init draw
		MapArtist.initMapArtist();

        // Init server
        boolean success = WebServer.init();
    }

}