package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.controller.WebServer;

/**
 * Main entry-point for the application.
 */
public class Main {

    /**
     * Starts the application by initializing data and then starting server.
     * @param args  The arguments to feed the application.
     */
    public static void main(String[] args) {
        // Import data

        // Init server
        boolean success = WebServer.init();
    }

}