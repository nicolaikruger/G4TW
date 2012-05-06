package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.Iterator;

/**
 *
 */
public abstract class DijkstraEdge implements Iterable<Road>, Comparable<DijkstraEdge> {

    public abstract double getLength();

    public abstract int getId();

    private DynamicArray<Road> edges = new DynamicArray<Road>();

    public void addEdge(Road e){
           edges.add(e);
    }

    public int compareTo(DijkstraEdge o) {
        return Double.compare(this.getLength(), o.getLength());
    }

    public Iterator<Road> iterator() {
        return new Iterator<Road>() {
            private int n = 0;
            public boolean hasNext() {
                return n < edges.length();
            }

            public Road next() {
                return edges.get(n++);
            }

            public void remove() {
                edges.remove(--n);
            }
        };
    }
}
