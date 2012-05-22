package dk.itu.kf04.g4tw.util;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Road;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
     * @param turn  The file to direct the graph with.
     * @return A MapModel containing the loaded data
     * @throws FileNotFoundException If one or more of the data-files could not be found.
     * @see MapModel
     * @see RoadParser
     */
    public static MapModel load(File nodes, File edges, File turn) throws FileNotFoundException {
        // Create the model
        MapModel model = new MapModel();

        // Log the time
        long start = System.currentTimeMillis();

        // Parse the nodes
        HashMap<Integer, Point2D.Double> nodeMap = parseNodes(nodes);

        // Parse the edges
        int numberOfRoads = parseEdges(edges, nodeMap, model);
        
        // Direct the model
        trim(turn, model);

        // Log success
        Log.info("Successfully loaded " + numberOfRoads + " roads in " + (System.currentTimeMillis() - start) + "ms.");

        // Return
        return model;
    }
    
    /**
     * Iterates through a file to the edges as Nodes and add them to the model.
     *
     * @param file  The file to parse the edges from
     * @param nodeMap  The nodeMap to receive the data from
     * @param model  The model to load the data into
     * @throws FileNotFoundException If the given file could not be found
     * @return The number of roads loaded.
     */
    protected static int parseEdges(File file, HashMap<Integer, Point2D.Double> nodeMap, MapModel model) throws FileNotFoundException {
        // Start the id-counter
        // We use our own id's to promise consistency
        int id = 0;

        // Initialize the hashmap with nodes and points
        HashMap<Point2D.Double, ArrayList<Integer>> nodeRoadPair = new HashMap<Point2D.Double, ArrayList<Integer>>();

        // Load the file
        Scanner scanner = new Scanner(new FileInputStream(file), "ISO8859_1");

        // Fills in the data in the map
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            // Split the line on everything except blocks with ['...,...']
            //String[] nextLine = scanner.nextLine().split("((?<=\\d)|'),('|(?=\\d))");//split(",(?=([^']*'[^']*')*[^']*$)");
            String[] nextLine = scanner.nextLine().split(",(?=([^']*'[^']*')*[^']*$)");
            String name = nextLine[6];
            // removes ' at the beginning and the end of the name
            name = name.replace("'", "");

            int a = Integer.parseInt(nextLine[0]);
            int b = Integer.parseInt(nextLine[1]);

            Point2D.Double nodeA = nodeMap.get(a);
            Point2D.Double nodeB = nodeMap.get(b);

            Point2D.Double pointA = new Point2D.Double(nodeA.x,nodeA.y);
            Point2D.Double pointB = new Point2D.Double(nodeB.x,nodeB.y);

            int type = Integer.parseInt(nextLine[5]);
            double speed = Double.parseDouble(nextLine[25]);
            double length = Double.parseDouble(nextLine[2]);

            int startNumber = 0, endNumber = 0;
            String startLetter = null, endLetter = null;

            // Only if the road has a name, it house numbers and letters should be saved
            if(!name.equals(" ") && !name.isEmpty()) {
                int left = Integer.parseInt(nextLine[7]);
                int right = Integer.parseInt(nextLine[9]);
                if(left > 0) {
                    if(right > 0) {
                        if(left < right) startNumber = left;
                        else startNumber = right;
                    } else startNumber = left;
                } else if(right > 0) startNumber = right;

                left = Integer.parseInt(nextLine[8]);
                right = Integer.parseInt(nextLine[9]);
                if(left > 0) {
                    if(right > 0) {
                        if(left > right) endNumber = left;
                        else endNumber = right;
                    } else endNumber = left;
                } else if(right > 0) endNumber = right;


                if(!nextLine[11].equals("''")) {
                    if(!nextLine[13].equals("''")) {
                        if(nextLine[11].compareTo(nextLine[13]) < 0) startLetter = nextLine[11];
                        else startLetter = nextLine[13];
                    } else startLetter = nextLine[11];
                } else if (!nextLine[13].equals("''")) startLetter = nextLine[13];

				if (startLetter != null) {
                    startLetter = startLetter.toLowerCase();
                }

                if(!nextLine[12].equals("''")) {
                    if(!nextLine[14].equals("''")) {
                        if(nextLine[12].compareTo(nextLine[14]) > 0) endLetter = nextLine[12];
                        else endLetter = nextLine[14];
                    } else endLetter = nextLine[12];
                } else if(!nextLine[14].equals("''")) endLetter = nextLine[14];

				if (endLetter != null) {
				    endLetter = endLetter.toLowerCase();
                }
            }

            int leftPostalCode = Integer.parseInt(nextLine[17]);
            int rightPostalCode = Integer.parseInt(nextLine[18]);

            type = model.getRoadTypeFromId(type);

            // Create the road and setup connections/edges
            Road tmp = new Road(id, name, pointA, pointB, type, speed, length, startNumber, endNumber, startLetter, endLetter, leftPostalCode, rightPostalCode);

            // Add the road to the edges collection
            model.addRoad(tmp);

            // If the points are not yet in the nodeRoadPair-hashmap, add them.
            if(!nodeRoadPair.containsKey(pointA)) nodeRoadPair.put(pointA, new ArrayList<Integer>());
            if(!nodeRoadPair.containsKey(pointB)) nodeRoadPair.put(pointB, new ArrayList<Integer>());

            // Add the new road as an edge to all other roads that shares the same points
            // Add all other roads with same points to the new road
            // --> Creates a UNDIRECTED graph!
            for(int i : nodeRoadPair.get(pointA)) {
                model.getRoad(i).addEdge(tmp.id);
                tmp.addEdge(model.getRoad(i).id);
            }

            for(int i : nodeRoadPair.get(pointB)) {
                model.getRoad(i).addEdge(tmp.id);
                tmp.addEdge(model.getRoad(i).id);
            }

            // Add the new roads ID to the hashmap
            nodeRoadPair.get(pointA).add(id);
            nodeRoadPair.get(pointB).add(id);

            // Increment id
            id++;
        }
        // Close the scanner
        scanner.close();

        // Log success
        Log.fine("Successfully loaded Map edges into model.");

        // Return number of roads
        return id + 1;
    }

    /**
     * Tries to parse a HashMap of Nodes from a given file.
     * @param file  The file to take data from
     * @return  The HashMap of nodes
     * @throws FileNotFoundException  If the file could not be found
     */
    protected static HashMap<Integer, Point2D.Double> parseNodes(File file) throws FileNotFoundException {
        // Load the file
        Scanner scanner = new Scanner(new FileReader(file));

        // Create The HashMap
        HashMap<Integer, Point2D.Double> nodeMap = new HashMap<Integer, Point2D.Double>();
        
        // Try to load the data
        scanner.nextLine();
        while(scanner.hasNextLine()) {
            String[] nextLine = scanner.nextLine().split(",");
            // Find the id and positions
            int id = Integer.parseInt(nextLine[2]);
            double xPos = Double.parseDouble(nextLine[3]);
            double yPos = Double.parseDouble(nextLine[4]);
            
            // Create the node
            Point2D.Double node = new Point2D.Double(xPos, yPos);

            // Insert it into the map
            nodeMap.put(id, node);
        }
        scanner.close();

        Log.fine("Successfully parsed Map nodes.");
        
        return nodeMap;
    }

    /**
     * Directs the graph, by following the turn.txt file
     * @param file  The file with the directed graph-data.
     * @param model The model to direct
     */
    protected static void trim(File file, MapModel model)
    {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            scanner.nextLine();
            while(scanner.hasNextLine())
            {
                String[] nextLine = scanner.nextLine().split(",");
                int fID = Integer.parseInt(nextLine[2]);
                int tID = Integer.parseInt(nextLine[3]);

                // Make the graph directed
                Iterator<Integer> it = model.getRoads()[fID].iterator();
                while(it.hasNext()) {
                    if(model.getRoad(it.next()).id == tID) {
                        it.remove();
                        break;
                    }
                }
            }
        } catch (NullPointerException e) {
            Log.warning("Unable to direct graph - error in reading: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

}
