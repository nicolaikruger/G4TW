package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.Iterator;

/**
 * An edge in a directed, weighted graph containing a weight (length) and an id.
 * @see DijkstraSP
 */
public abstract class DijkstraEdge implements Iterable<Integer>, Comparable<DijkstraEdge> {

    /**
     * The length of the edge in terms of distance to travel along the edge.
     * @return  The length in meters.
     */
    public abstract double getLength();

    /**
     * The unique id of the edge.
     * @return  A unique integer.
     */
    public abstract int getId();

    /**
     * The edges this edge is connected to.
     */
    private DynamicArray<Integer> edges = new DynamicArray<Integer>();

    /**
     * Adds a directed connection between this edge and the given edge.
     * @param e  The edge to connect to.
     */
    public void addEdge(int e) {
        edges.add(e);
    }

    /**
     * Compares this edge to another based on the length of the edges.
     * @param o  The other edge to compare.
     * @return  Less than 0 if this edge is shortest, 0 if they are the same length and a number above 0 if this edge is the longest of the two.
     */
    public int compareTo(DijkstraEdge o) {
        return Double.compare(this.getLength(), o.getLength());
    }

    /**
     * Provides an iterator to iterate the connections of this edge.
     * @return An iterator iterating all the connections.
     */
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int n = 0;
            public boolean hasNext() {
                return n < edges.length();
            }

            public Integer next() {
                return edges.get(n++);
            }

            public void remove() {
                edges.remove(--n);
            }
        };
    }

    /**
     * Find the number of roads this road is connected to.
     * @return A number equal to the number of connections for this road.
     */
    public int numberOfEdges() { return edges.length(); }
}
