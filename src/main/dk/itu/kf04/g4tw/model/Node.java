package dk.itu.kf04.g4tw.model;

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
    
	public void createRelation(Road r) {
		for(int i = 0; i < roads.length(); i++)
        {
            roads.get(i).addEdge(r);
        }

        roads.add(r);
	}

}
