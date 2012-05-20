package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.util.DynamicArray;

/**
 * A node that contains information about a point in a 2-dimensional space 
 * and its connections to roads - i. e. edges.
 */
public class Node {

    /**
     * The roads connected to this Node in the graph.
     */
	private DynamicArray<Road> edges = new DynamicArray<Road>();
	
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

    /**
     * Connects all the roads attached to this node to the given Road, treated as an edge in
     * a Dijkstra graph. In other words we iterate over all the roads currently connected to
     * this node and adds the road as an "edge" in a graph.
     * @param r  The road to connect the already connected roads to.
     */
	public void connectTo(Road r) {
		for(int i = 0; i < edges.length(); i++) {
            edges.get(i).addEdge(r.id);
        }

        edges.add(r);
	}

}
