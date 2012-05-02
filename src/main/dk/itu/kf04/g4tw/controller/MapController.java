package dk.itu.kf04.g4tw.controller;

import java.util.logging.Logger;

/**
 * The overall controller for the application.
 */
public class MapController {

    protected static Logger Log = Logger.getLogger(MapController.class.getName());

    /**
     * Creates a new controller with the information stored in the model.
     */
	public MapController() {
        // Init server
        boolean success = WebServer.init();
        
        // Log status
        if (success) {
            Log.info("Web-server has been started.");
        } else {
            Log.warning("Web-server failed to start.");
        }
    }
}
