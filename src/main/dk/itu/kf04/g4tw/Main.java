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
    // TODO: Adjust colours
    // TODO: Krüger: Comment Road - Especially all the fields
    // TODO: Krüger: Comment Dijkstras methods
    // TODO: Krüger: Compile the turn-file into binary
    // TODO: Krüger: Move trim() to RoadParser
    // TODO: Jens: Skriv RequestParser test
    // TODO: Jens: Test for helvede, test!

    /**
     * The logging-util for the Main class.
     */
    protected static Logger Log = Logger.getLogger(Main.class.getName());
    
    /**
     * Starts the application by initializing data and then starting server.
     * @param args  The arguments to feed the application.
     */
    public static void main(String[] args) {
        // Set properties
        System.setProperty("file.encoding", "ISO8859_1");
        Log.setLevel(Level.ALL);
        int port;

        // Examine input
        if (args.length > 0) {
            if (args[0].equals("help")) {
                System.out.print(helpText());
                return;
            } else if (args[0].equals("version")) {
                System.out.print("G4TW Map Server Application\nVersion 1.0");
                return;
            } else {
                // Start the server on the given port or, if none, 80
                try { port = Integer.parseInt(args[0]); }
                catch (ArrayIndexOutOfBoundsException e) {
                    Log.info("No port was given. Starting server on port 80.");
                    port = 80;
                }
                catch (NumberFormatException e) {
                    Log.severe("Failed to parse number to port, starting server on port 80: " + e);
                    port = 80;
                }
            }
        } else {
            port = 80;
        }

        // Log program start
        System.out.print("***********************************\n");
        System.out.print("*** G4TW Map Server Application ***\n");
        System.out.print("***   Compiled 22nd May 2012    ***\n");
        System.out.print("***        Version 1.0          ***\n");
        System.out.print("***********************************\n\n");

        Log.info("Importing map-data...");
        
        // Import data
        MapModel model = DataStore.loadRoads();

        // Create the server instance.
        new WebServer(model, port);
    }
    
    protected static String helpText() {
        return "G4TW Map Server Application, version 1.0\n" +
                "Usage: Main [PORT|OPTION]\n" +
                "A Map Server Application which loads data from kdv-data files and serves them through a HTML5-interface at the given port, defaulting to 80.\n" +
                "Port: A number between 1 and 65536 on which port the server listens." +
                "Options:\n" +
                "\t\thelp\tDisplays this help text\n" +
                "\t\tversion\nDisplays the version of the software";
    }

}