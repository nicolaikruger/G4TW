package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.Iterator;

/**
 *
 */
public abstract class DijkstraEdge implements Iterable<DijkstraEdge>, Comparable<DijkstraEdge> {

    public abstract double getLength();

    public abstract int getId();

    public abstract DijkstraEdge getEdge(int x);

    private DynamicArray<Integer> edges = new DynamicArray<Integer>();

    public void addEdge(Integer e){
           edges.add(e);
    }

    public int compareTo(DijkstraEdge o) {
        return Double.compare(this.getLength(), o.getLength());
    }

    public Iterator<DijkstraEdge> iterator() {
        return new Iterator<DijkstraEdge>() {
            private int n = 0;
            public boolean hasNext() {
                return n < edges.length();
            }

            public DijkstraEdge next() {
                return getEdge(n++);
            }

            public void remove() {
                throw new UnsupportedOperationException("Deletion not permitted");
            }
        };
    }
}
