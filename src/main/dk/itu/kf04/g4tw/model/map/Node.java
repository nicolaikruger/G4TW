package dk.itu.kf04.g4tw.model.map;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.ArrayList;

/**
 * A node that contains information about a point in a 2-dimensional space 
 * and its connections to other points.
 */
public class Node {

	// Krüger implementation //
	private ArrayList<Integer> roadsArray = new ArrayList<Integer>();
	// Krüger end //

	DynamicArray<Road> roads = new DynamicArray<Road>();
	
    /**
     * The x coordinate of the Node.
     */
	private final double x;
    
    /**
     * The y coordinate of the Node.
     */
	private final double y;

	public double getX() { return x; }
	public double getY() { return y; }
    
    /**
     * The connections to other Nodes.
     */
	private DynamicArray<Integer> connections = new DynamicArray<Integer>();
	
    /**
     * Constructs a Node with an x and y coordinate.
     * @param x  The x coordinate.
     * @param y  The y coordinate.
     */
	public Node(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
    /**
     * Connect this node to another Node.
     * @param id the ID of the other node
     */
	public void connect(int id) {
		connections.add(id);
	}
	
	/**
	 * Replace an ID with a new one
     * @param oldId  The old identifier
     * @param newId  The new identifier to insert
	 */
	public boolean replace(int oldId, int newId)
	{
        return connections.replace(oldId, newId);
	}
	
    /**
     * Returns the number of the connections.
     */
	public int getConnectionsLength() {
		return connections.length();
	}
	
    /**
     * Return the connection of the given index, if it exists.
     * @param index  The index of the connection to retrieve (handy for iterations).
     */
	public int getConnection(int index)
	{
		return connections.get(index);
	}
    
	public void createRelation(Road r) {
		roads.add(r);
	}
	
	// Krüger //
	public void addRoadID(int ID)
	{
		roadsArray.add(ID);
	}
	// Krüger end //
}
