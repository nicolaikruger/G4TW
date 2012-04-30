package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.ArrayList;

/**
 * A node that contains information about a point in a 2-dimensional space 
 * and its connections to roads - i. e. edges.
 */
public class Node {

    /**
     * The roads connected to this Node in the graph.
     */
	private DynamicArray<Road> roads = new DynamicArray<Road>();
	
    /**
     * The x coordinate of the Node.
     */
	public final double x;
    
    /**
     * The y coordinate of the Node.
     */
	public final double y;
	
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
