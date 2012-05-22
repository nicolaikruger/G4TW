package dk.itu.kf04.g4tw.model;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Makes it possible to find the shortest path within a graph of T by using the Dijkstra's algorithm
 */
public abstract class DijkstraSP<T extends DijkstraEdge> {

    /**
     * Retrieves the edge with the given id connecting to this edge.
     * @param i  The id of the edge.
     * @return  The edge in the model.
     */
    public abstract T getEdge(int i);

    /**
     * Calls onLiner, but with a fixed number for N
     * @param from  The edge from which to find the shortest path.
     * @param to  The edge to which the shortest path should go.
     * @return An array of elements completing the path.
     * @see DijkstraSP#onLiner(int N, T from, T to);
     */
    @SuppressWarnings("unchecked")
    public T[] shortestPath(T from, T to) {
        return onLiner(MapModel.numberOfRoads, from, to);
    }

    /**
     * Perform a shortest path search with Dijkstra's algorithm on a given graph of T.
     * @param N     Number of nodes in the graph
     * @param from  The start node
     * @param to    The end node
     * @return      Returns a array of T with the previous node. If A leads to B, then array[B.ID] = A.
     *              The start node has no previous, so array[from.ID] = null.
     *              If no path could be found, null will be returned.
     */
    @SuppressWarnings("unchecked")
    protected T[] onLiner(int N, T from, T to)
    {
        final double[] dist = new double[N]; // Holds the distance from a node back to the starting node
        boolean[] visited = new boolean[N];	 // If a node have been visited, the visited[node.id] will be set to true
        T[] previous = (T[]) Array.newInstance(from.getClass(), N);
                                             // The node that led to the current node
                                             // --> If a lead to b, then previous[b.id] = a

		// Fills the arrays
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(visited, false);

		// Mark the starting node
        dist[from.getId()] = 0;

		// Create a minimum priority queue of nodes, with custom comparator on the distance for the nodes
        PriorityQueue<T> Q = new PriorityQueue<T>(N, new Comparator<T>() {
            public int compare(T o1, T o2) {
                double dist1 = dist[o1.getId()];
                double dist2 = dist[o2.getId()];

                if(dist1 > dist2) return 1;
                if(dist1 == dist2) return 0;
                else return -1;
            }
        });

		// Add the starting node to the queue
        Q.add(from);

		// Start the Dijstra search
        while(!Q.isEmpty())
        {
			// Get the first road in the queue
            T U = Q.poll();

			// Mark the road as visited
            visited[U.getId()] = true;

			// If the distance from U to the starting node is infinity, there ain't no path.
            if(dist[U.getId()] == Double.POSITIVE_INFINITY) {
                System.out.println("THERE IS NO PATH!");
                return null;
            }

			// If the ID of node U is the same as the ID of the end node, the path has been found.
			// no need to continue the search.
            if(U.getId() == to.getId()) {
				break;
			}

            // Runs trough all the nodes that U leads to. One at a time.
            for (Integer i : U) {
                T V = getEdge(i);

				// If the rode, V, has not yet been visited, go on.
                if(!visited[V.getId()])
                {
					// Mark V as visited
                    visited[V.getId()] = true;

					// Add V to the queue
                    Q.add(V);

					// Calculate the distance from V to the start node, via U
                    double alt = dist[U.getId()] + V.getLength();

					// Gets the current distance from V to the star node
                    double vDist = dist[V.getId()];

					// If the calculated distance, alt, is smaller than the current distance, vDist, go on.
                    if(alt < vDist)
                    {
						// Set the distance from V to the start node to be the calculated distance, alt
                        dist[V.getId()] = alt;

						// Set the node that leads to V to be U.
                        previous[V.getId()] = U;
                    }
                }
            }
        }
        return previous;
    }
    
}
