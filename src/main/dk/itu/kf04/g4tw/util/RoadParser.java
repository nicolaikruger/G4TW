package dk.itu.kf04.g4tw.util;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Node;
import dk.itu.kf04.g4tw.model.Road;

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
public class RoadParser {

    /**
     * The logger for the class
     */
    static Logger Log = Logger.getLogger(RoadParser.class.getName());
    
    /**
     * Initializes the RoadParser and loads the map-data into the MapModel.
     * 
     * @param nodes  The file to load the Nodes from.
     * @param edges  The file to load the edges from.
     * @see MapModel
     * @see RoadParser
     */
    public static void load(File nodes, File edges) {
        try {
            HashMap<Integer, Node> nodeMap = parseNodes(nodes);
            parseEdges(edges, nodeMap);
        } catch (FileNotFoundException e) {
            Log.warning("Exception while processing map-data: ");
            e.printStackTrace();
        }
    }
    
    /**
     * Iterates through a file to the edges as Nodes and add them to the model.
     *
     * @param file  The file to parse the edges from
     * @param nodeMap  The nodeMap to receive the data from
     * @throws FileNotFoundException If the given file could not be found
     */
    protected static void parseEdges(File file, HashMap<Integer, Node> nodeMap) throws FileNotFoundException {
        // Start the id-counter
        int id = 0;

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

            Point2D.Double pointA = new Point2D.Double(nodeA.x,nodeA.y);
            Point2D.Double pointB = new Point2D.Double(nodeB.x,nodeB.y);

            // Use the id's of the dynamic array so we promise consistency
            //int id = Integer.parseInt(nextLine[3]); // DAV_DK-ID
            //int id2 = Integer.parseInt(nextLine[4]); // DAV_DK-ID
            int type = Integer.parseInt(nextLine[5]);
            double speed = Double.parseDouble(nextLine[25]);
            double length = Double.parseDouble(nextLine[2]);

            type = MapModel.getRoadTypeFromId(type);

            // Create the road and setup connections/edges
            Road tmp = new Road(id++, name, pointA, pointB, type, speed, length);
            nodeA.connectTo(tmp);
            nodeB.connectTo(tmp);
            MapModel.addRoad(tmp);
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