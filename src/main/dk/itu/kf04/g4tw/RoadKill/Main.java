package dk.itu.kf04.g4tw.RoadKill;

import dk.itu.kf04.g4tw.RoadKill.tree.RBTree;
import dk.itu.kf04.g4tw.RoadKill.tree.RoadRectangle;
import dk.itu.kf04.g4tw.RoadKill.tree.Tree2D;

import javax.naming.directory.SearchControls;
import java.io.*;
import java.util.*;
import java.lang.Math;
import java.awt.geom.Point2D;

public class Main {

	/**
	 * The kd-tree for our awesome data.
	 */
	private static Tree2D tree = new Tree2D();
	private static RBTree<RoadRectangle, Road> rbTree = new RBTree<RoadRectangle, Road>(4);

	/**
	 * The nodes mapped to integers.
	 */
	public static HashMap<Integer, Node> nodeMap;

	/**
	 * The number of connections in the map.
	 */
	public static int connections = 0;

	/**
	 * The start time.
	 */
	public static long startTime;

	/**
	 * The roads of the map.
	 */
	public static DynamicArray<Road> roads;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		startTime = System.currentTimeMillis();

		try{
			nodeMap = getNodes("./src/main/dk/itu/kf04/g4tw/krakData/kdv_node_unload.txt");
		} catch (Exception e) {
			System.out.println("Fuuuuuuuuuuuuu! Program said:\n\t" + e);
		}

		try{
			roads = getEdges("./src/main/dk/itu/kf04/g4tw/krakData/kdv_unload.txt");
		} catch (Exception e) {
			System.out.println("Fuuu! Program said:\n\t" + e);
		}
		
		// Såfle!
		//roads.shuffle();

		// Cræajte che tree
		//tree = new Tree2D();

		// Krüger //
		System.out.println("Tree-Building initialized !");
		long time = System.currentTimeMillis();
		for(int i = 0; i < roads.length(); i++)
		{
			Point2D.Double p1 = roads.get(i).from;
			Point2D.Double p2 = roads.get(i).to;


			tree.addNode(p1, p2, i);
		}
		System.out.println("Tree has been built!\n\t Build time is: " + (System.currentTimeMillis()-time) + " ms.");
		time = System.currentTimeMillis();

		/*for(int i = 0; i < roads.length(); i++)
		{
			Point2D.Double p1 = roads.get(i).from;
			Point2D.Double p2 = roads.get(i).to;

			rbTree.put(new RoadRectangle(p1, p2), roads.get(i));
		}
		System.out.println("RBTree has been built!\n\t Build time is: " + (System.currentTimeMillis()-time) + " ms: " + rbTree);
		time = System.currentTimeMillis();
		DynamicArray<Road> arr = rbTree.search(new RoadRectangle(0,0,9999999,9999999));
		System.out.println("Search takes: " + (System.currentTimeMillis()-time) + " ms - Number of elements returned: "+ arr.length());
		*/// Krüger end //

		//System.out.println(nodeMap.size() + "\t\t" + connections);

		//removeNodes();
		//double max = FindMax.getMax(roads);
		//System.out.println(max);

		//assignRoadToNode();

		//calculateConnections();
	}

	private static void calculateConnections() {
		for (Map.Entry<Integer, Node> entry : nodeMap.entrySet()) {
			int id = entry.getKey();
			Node n = entry.getValue();
			for (int i = 0; i < n.getConnectionsLength(); i++) {
				if (n.getConnection(i) < id) {
					Node n2 = nodeMap.get(n.getConnection(i));
					double dist = Math.sqrt(Math.pow(n2.getX() - n.getX(), 2) + Math.pow(n2.getY() - n.getY(), 2));
					System.out.println(id + "\t" + n.getConnection(i) + "\t" + dist);
				}
			}
		}
	}

	private static void removeNodes()
	{
		Iterator<Map.Entry<Integer, Node>> it = nodeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Node> pairs = (Map.Entry<Integer, Node>) it.next();
			Node n = pairs.getValue();
			int id = pairs.getKey();

			if(n.getConnectionsLength() == 2)
			{
				int a = n.getConnection(0);
				int b = n.getConnection(1);

				Node nodeA = nodeMap.get(a);
				Node nodeB = nodeMap.get(b);

				nodeA.replace(id, b);
				nodeB.replace(id, a);

				it.remove();

				connections--;
			}
		}
	}

	private static DynamicArray<Road> getEdges(String url) throws FileNotFoundException {
// Fills in the data in the arrays
		Scanner scanner = new Scanner(new FileReader(url));
		DynamicArray<Road> tmpRoads = null;
		try {
			tmpRoads = new DynamicArray<Road>();

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

				connections++;

				Road tmp = new Road(name, pointA, pointB, type, speed, length);
				//System.out.println(tmp.toString());
				tmpRoads.add(tmp);
			}

		} catch (IndexOutOfBoundsException e) {
			System.out.println (e.getMessage() + "\n");
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return tmpRoads;
	}


	private static HashMap<Integer, Node> getNodes(String url) throws FileNotFoundException
	{
		HashMap<Integer, Node> hmap = new HashMap<Integer, Node>();
		Scanner scanner = new Scanner(new FileReader(url));
		try {
			scanner.nextLine();
			while(scanner.hasNextLine()) {
				String[] nextLine = scanner.nextLine().split(",");
				int id = Integer.parseInt(nextLine[2]);
				double xPos = Double.parseDouble(nextLine[3]);
				double yPos = Double.parseDouble(nextLine[4]);

				Node node = new Node(xPos, yPos);

				hmap.put(id, node);
			}
		} catch (Exception e) {
			System.out.println("Exception while creating the map: " + e);
		} finally {
			scanner.close();
		}
		return hmap;
	}
	
	/**
	private static void assignRoadToNode() {
		for (int i = 0; i<roads.length(); i++) {
			roads.get(i).assignNodes();
		}
	}*/
}