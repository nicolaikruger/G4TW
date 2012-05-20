package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.Iterator;

/**
 *
 */
public abstract class DijkstraEdge implements Iterable<Integer>, Comparable<DijkstraEdge> {

    public abstract double getLength();

    public abstract int getId();

    private DynamicArray<Integer> edges = new DynamicArray<Integer>();
    
    public void addEdge(int e) {
        edges.add(e);
    }

    public int compareTo(DijkstraEdge o) {
        return Double.compare(this.getLength(), o.getLength());
    }

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
