package dk.itu.kf04.g4tw.model.map;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A map
 */
public class MapModel {

	private static HashMap<Integer, Node> nodeMap;
    
    public static final int HIGHWAY        = 1;
    public static final int EXPRESSWAY     = 2;
    public static final int PRIMARY_ROAD   = 4;
    public static final int SECONDARY_ROAD = 8;
    public static final int MINOR_ROAD     = 16;
    public static final int PATH           = 32;
    public static final int SEAWAY         = 64;
    public static final int LOCATION       = 128;
	
	private int i = 1;

    protected HashMap<Integer, Integer> mapTypeReference = new HashMap<Integer, Integer>();
    protected HashMap<Integer, RoadTypeTree> roadTrees = new HashMap<Integer, RoadTypeTree>();

    protected void loadTypeReference(int type, int... values) {
		roadTrees.put(type, new RoadTypeTree(type));
        for (int i = 0; i < values.length; i++) {
			mapTypeReference.put(values[i], type);
        }
    }

    public MapModel() {
		// Added 0, 31, 95

        loadTypeReference(HIGHWAY,        1, 21, 31, 41);
        loadTypeReference(EXPRESSWAY,     2, 22, 32, 42);
        loadTypeReference(PRIMARY_ROAD,   3, 23, 33, 43);
        loadTypeReference(SECONDARY_ROAD, 4, 24, 34, 44, 95);
        loadTypeReference(MINOR_ROAD,     0, 5, 25, 26, 6, 35, 45, 46);
        loadTypeReference(PATH,           8, 28, 48, 10, 11);
        loadTypeReference(SEAWAY,         80);
        loadTypeReference(LOCATION,       99);
		
		loadMapData();
    }
	
	public String getXML(double xMin, double yMin, double xMax, double yMax, int... type)
	{
		String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
				"<roadCollection xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
				"xsi:noNamespaceSchemaLocation=\"http://online-sporstudstyr.dk/kraX.xsd\"" +
				"xmlns=\"http://www.w3schools.com\">";
		for(Integer i : type)
		{
			RoadTypeTree tree = roadTrees.get(i);
			String newXML = tree.search(xMin, yMin, xMax, yMax);
			xmlData += newXML;
		}

		xmlData += "</roadCollection>";

		return xmlData;
	}

	private void addRoad(Road road)
	{
		int roadType = road.getType();
		int treeType = mapTypeReference.get(roadType);
		RoadTypeTree tree = roadTrees.get(treeType);
		tree.addNode(road);
	}
    
    // ... //

	private void loadMapData()
	{
		String folderPath = "krak/";
		
		try{
			nodeMap = getNodes(folderPath + "kdv_node_unload.txt");
		} catch (Exception e) {
			System.out.println("Fuuuuuuuuuuuuu! Program said:\n\t" + e);
		}

		try{
			getEdges(folderPath + "kdv_unload.txt");
		} catch (Exception e) {
			System.out.println("Fuuu! Program said:\n\t" + e);
		}
	}

	private void getEdges(String url) throws FileNotFoundException {
		// Fills in the data in the arrays
		Scanner scanner = new Scanner(new FileReader(url));
		try {
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

				addRoad(tmp);
			}

		} catch (IndexOutOfBoundsException e) {
			System.out.println (e.getMessage() + "\n");
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}


	private HashMap<Integer, Node> getNodes(String url) throws FileNotFoundException
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
}
