package dk.itu.kf04.g4tw.RoadKill;

import java.io.*;
import java.util.*;
import java.lang.Math;
import java.awt.geom.Point2D;

public class Main {

	public static HashMap<Integer, Node> nodeMap;
	public static int connections = 0;
	public static long startTime;
	public static DynamicArray<Road> roads;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		startTime = System.currentTimeMillis();
		
		try{
			nodeMap = getNodes("../krak-data/kdv_node_unload.txt");
		} catch (Exception e) {
			System.out.println("Fuuuuuuuuuuuuu! Program said:\n\t" + e);
		}
		
		try{
			roads = getEdges("../krak-data/kdv_unload.txt");
		} catch (Exception e) {
			System.out.println("Fuuu! Program said:\n\t" + e);
		}
		
		System.out.println(nodeMap.size() + "\t\t" + connections);
		
		//removeNodes();
		
		double max = FindMax.getMax(roads);
		System.out.println(max);
		
		assignRoadToNode();
		
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

	
	private static DynamicArray<Road> getEdges(String url) throws FileNotFoundException
	{		
		// Fills in the data in the arrays
		Scanner scanner = new Scanner(new FileReader(url));
		try {
			DynamicArray<Road> tmpRoads = new DynamicArray<Road>();
			
			scanner.nextLine();
			while(scanner.hasNextLine()) {
				String[] nextLine = scanner.nextLine().split(",");
				String name = nextLine[6].toString();
				
				int a = Integer.parseInt(nextLine[0]);
				int b = Integer.parseInt(nextLine[1]);
				Node nodeA = nodeMap.get(a);
				Node nodeB = nodeMap.get(b);
				
				int type = Integer.parseInt(nextLine[5]);
				double speed = Double.parseDouble(nextLine[25]);
				double length = Double.parseDouble(nextLine[2]);
				
				nodeA.connect(b);
				nodeB.connect(a);
				
				connections++;
				
				Road tmp = new Road(name, nodeA, nodeB, type, speed, length);
				//System.out.println(tmp.toString());
				tmpRoads.add(tmp);
			}
			
			return tmpRoads;
		}
		finally {
			scanner.close();
		}
	}

	
	private static HashMap<Integer, Node> getNodes(String url) throws FileNotFoundException 
	{		
		HashMap<Integer, Node> hmap = new HashMap<Integer, Node>();
		Scanner scanner = new Scanner(new FileReader(url));
		try {
			scanner.nextLine();
			while(scanner.hasNextLine()) {
				String[] nextLine = scanner.nextLine().split(",");
				int id	= Integer.parseInt(nextLine[2]);
				double xPos = Double.parseDouble(nextLine[3]);
				double yPos = Double.parseDouble(nextLine[4]);
				
				Node node = new Node(xPos, yPos);
				
				hmap.put(id, node);
			}
		}
		finally {
			scanner.close();
		}
		return hmap;
	}
	
	private static void assignRoadToNode() {
		for (int i = 0; i<roads.length(); i++) {
			roads.get(i).assignNodes();
		}
	}
}
