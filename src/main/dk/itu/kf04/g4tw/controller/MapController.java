package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.MapModel;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Krüger-Pony  **mwuahahah**
 * Date: 01-04-12
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
 */
public class MapController {

    protected static Logger Log = Logger.getLogger(MapController.class.getName());

    /**
     * The model containing the Map-data.
     */
    static MapModel model;

    /**
     * Creates a new controller with the information stored in the model,
     * @param model  The model containing the map-data.
     */
	public MapController(MapModel model) {
        MapController.model = model;

        // Init server
        boolean success = WebServer.init();
        
        // Log status
        if (success) {
            Log.info("Program initialized correct and web-server has been started.");
        } else {
            Log.warning("Program initialized correct but web-server failed to start.");
        }
    }
}
