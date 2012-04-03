package dk.itu.kf04.g4tw.model;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * A parser for Krak map-data.
 */
public class MapParser {

    /**
     * The logger for the class
     */
    static Logger Log = Logger.getLogger(MapParser.class.getName());
    
    /**
     * Initializes the MapParser and loads the map-data into the given model.
     * 
     * @param nodes  The file to load the Nodes from.
     * @param edges  The file to load the edges from.
     * @return  A Model containing the nodes and edges loaded.
     */
    public static MapModel load(File nodes, File edges) {
        // Create the model
        MapModel model = new MapModel();
        
        try {
            HashMap<Integer, Node> nodeMap = parseNodes(nodes);
            parseEdges(edges, model, nodeMap);
        } catch (FileNotFoundException e) {
            Log.warning("Exception while processiong map-data: " + e.getMessage() + ". Model may be empty.");
        }

        return model;
    }
    
    /**
     * Iterates through a file to the edges as Nodes and add them to the model.
     *
     * @param file  The file to parse the edges from
     * @param model  The model to insert the edges in
     * @param nodeMap  The nodeMap to receive the data from
     * @throws FileNotFoundException If the given file could not be found
     */
    protected static void parseEdges(File file, MapModel model, HashMap<Integer, Node> nodeMap) throws FileNotFoundException {
        // Load the file
        Scanner scanner = new Scanner(new FileReader(file));

        // Fills in the data in the map
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] nextLine = scanner.nextLine().split(",");
            String name = nextLine[6];

            int a = Integer.parseInt(nextLine[0]);
            int b = Integer.parseInt(nextLine[1]);

            Node nodeA = nodeMap.get(a);
            Node nodeB = nodeMap.get(b);

            Point2D.Double pointA = new Point2D.Double(nodeA.getX(),nodeA.getY());
            Point2D.Double pointB = new Point2D.Double(nodeB.getX(),nodeB.getY());

            int type = Integer.parseInt(nextLine[5]);
            double speed = Double.parseDouble(nextLine[25]);
            double length = Double.parseDouble(nextLine[2]);

            nodeA.connect(b);
            nodeB.connect(a);
            Road tmp = new Road(name, pointA, pointB, type, speed, length);
            model.addRoad(tmp);
        }
        // Close the scanner
        scanner.close();

        // Log success
        Log.fine("Successfully loaded Map edges into model.");
    }

    /**
     * Tries to parse a HashMap of Nodes from a given file.
     * @param file  The file to take data from
     * @return  The HashMap of nodes
     * @throws FileNotFoundException  If the file could not be found
     */
    protected static HashMap<Integer, Node> parseNodes(File file) throws FileNotFoundException {
        // Load the file
        Scanner scanner = new Scanner(new FileReader(file));

        // Create The HashMap
        HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();
        
        // Try to load the data
        scanner.nextLine();
        while(scanner.hasNextLine()) {
            String[] nextLine = scanner.nextLine().split(",");
            // Find the id and positions
            int id = Integer.parseInt(nextLine[2]);
            double xPos = Double.parseDouble(nextLine[3]);
            double yPos = Double.parseDouble(nextLine[4]);
            
            // Create the node
            Node node = new Node(xPos, yPos);

            // Insert it into the map
            nodeMap.put(id, node);
        }

        scanner.close();

        Log.fine("Successfully parsed Map nodes.");
        
        return nodeMap;
    }
    
}
