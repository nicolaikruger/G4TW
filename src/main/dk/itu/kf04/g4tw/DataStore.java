package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.model.*;
import dk.itu.kf04.g4tw.util.DynamicArray;
import dk.itu.kf04.g4tw.util.RoadParser;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 */
public class DataStore {
    
    protected static String file = "data.bin";
    
    public static Logger Log = Logger.getLogger(DataStore.class.getName());
    
    public static void main(String[] args) {
        // Find the files
        File nodeFile = new File("kdv_node_unload.txt");
        File edgeFile = new File("kdv_unload.txt");
        
        // Load the roads
        DynamicArray<Road> roads = RoadParser.load(nodeFile, edgeFile);

        try {
            // Create the output stream
            DataOutputStream os = new DataOutputStream(new FileOutputStream(file));

            // Iterate over the roads
            for (int i = 0; i < roads.length(); i++) {
                Road road = roads.get(i);
                os.writeInt(road.id);
                os.writeUTF(road.name);
                os.writeDouble(road.from.getX()); // From
                os.writeDouble(road.from.getY());
                os.writeDouble(road.to.getX()); // To
                os.writeDouble(road.to.getY());
                os.writeInt(road.type);
                os.writeDouble(road.speed);
                os.writeDouble(road.getLength());
            }
            
            // Flush!
            os.flush();
            
            // Log success.
            Log.info("Successfully wrote to file.");
        } catch (IOException e) {
            Log.warning("Failure while writing roads to disk.");
            e.printStackTrace();
        }
    }
    
    public static MapModel loadRoads() {
        HashMap<Integer, RoadTypeTree> roads = new HashMap<Integer, RoadTypeTree>();

        // TODO: Find et dynamisk fix til at sætte størrelse
        Road[] edges = new Road[812302];
        HashMap<Point2D.Double, ArrayList<Integer>> nodeRoadPair = new HashMap<Point2D.Double, ArrayList<Integer>>();

        int numberOfRoads = 0;
        long time = System.currentTimeMillis();

        Road r1 = null;
        Road r2 = null;

        try {
            DataInputStream is  = new DataInputStream(new FileInputStream(file));
            while (true) {
                int id              = is.readInt();
                String name         = is.readUTF();
                Point2D.Double from = new Point2D.Double(is.readDouble(), is.readDouble());
                Point2D.Double to   = new Point2D.Double(is.readDouble(), is.readDouble());
                int type            = is.readInt();
                double speed        = is.readDouble();
                double length       = is.readDouble();
                Road road = new Road(id, name, from, to, type, speed, length);

                // If the points are not yet in the hashmap, add them.
                if(!nodeRoadPair.containsKey(from)) nodeRoadPair.put(from, new ArrayList<Integer>());
                if(!nodeRoadPair.containsKey(to)) nodeRoadPair.put(to, new ArrayList<Integer>());

                // Add the new road as an edge to all other roads that shares the same points
                // Add all other roads with same points to the new road
                // --> Creates a UNDIRECTED graph!
                for(int i : nodeRoadPair.get(from))
                {
                    edges[i].addEdge(road);
                    road.addEdge(edges[i]);
                }

                for(int i : nodeRoadPair.get(to))
                {
                    edges[i].addEdge(road);
                    road.addEdge(edges[i]);
                }

                // Add the new roads ID to the hashmap
                nodeRoadPair.get(from).add(id);
                nodeRoadPair.get(to).add(id);

                // Add the road to the edges collection
                edges[id] = road;

                //addRoad(roads, road);
                numberOfRoads++;
                if(r1 == null && r2 != null) r1 = road;
                if(r2 == null) r2 = road;
            }
        } catch (IOException e) {} // Expected

        edges = trim(edges);
        for(Road road : edges)
        {
            addRoad(roads, road);
        }

        System.out.println("Import done in " + ((System.currentTimeMillis() - time) / 1000) + " seconds. Read " + numberOfRoads + " roads.");

        System.out.println("Road 1: " + r1);
        System.out.println("Road 2: " + r2);

        DijkstraEdge[] arr = DijkstraSP.onLiner(numberOfRoads, r1, r2);
        //DijkstraEdge[] arr = DSP.findPath(AB, CD);
        int prev = r2.getId();
        if(arr == null) System.out.println("THERE IS NO PATH!");
        else {
            while(arr[prev] != null)
            {
                System.out.println(arr[prev] + "-->");
                prev = arr[prev].getId();
            }
        }

        return new MapModel(roads);
    }

    /**
     * Adds a road to the Model.
     * @param road The road to add.
     */
	protected static void addRoad(HashMap<Integer, RoadTypeTree> roads, Road road) {
        // Construct the tree if it does not exist
        if (!roads.containsKey(road.type)) roads.put(road.type, new RoadTypeTree(road.type));
        
        // Insert
		roads.get(road.type).addNode(road);
	}

    protected static Road[] trim(Road[] arr)
    {
        Scanner scanner = null;

        try {
            scanner = new Scanner(new FileReader("turn.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        scanner.nextLine();
        while(scanner.hasNextLine())
        {
            String[] nextLine = scanner.nextLine().split(",");
            int fID = Integer.parseInt(nextLine[2]);
            int tID = Integer.parseInt(nextLine[3]);


            // Make the graph directed
            Iterator<DijkstraEdge> it = arr[fID].iterator();
            while(it.hasNext())
            {
                if(it.next().getId() == tID) {
                    it.remove();
                    break;
                }
            }
        }

        return arr;
    }
}
