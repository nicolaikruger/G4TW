package dk.itu.kf04.g4tw.model;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 */
public class DijkstraSP {

    public static Road[] shortestPath(MapModel model, Road from, Road to)
    {
        return onLiner(model, 812301, from, to);
    }

    public static Road[] onLiner(MapModel model, int N, Road from, Road to)
    {
        final double[] dist = new double[N];	// Holds the distance from a node back to the starting node
        boolean[] visited = new boolean[N];		// If a node have been visited, the visited[node.id] will be set to true
        Road[] previous = new Road[N];			// The node that led to the current node
        											// --> If a lead to b, then previous[b.id] = a

		// Fills the arrays
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(visited, false);

		// Mark the starting node
        dist[from.getId()] = 0;

		// Create a minimum priority queue of nodes, with custom comparator on the distance for the nodes
        PriorityQueue<Road> Q = new PriorityQueue<Road>(N, new Comparator<Road>() {
            public int compare(Road o1, Road o2) {
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
            Road U = Q.poll();

			// Mark the road as visited
            visited[U.getId()] = true;

			// If the distance from U to the starting node is infinity, there ain't no path.
            if(dist[U.getId()] == Double.POSITIVE_INFINITY)
            {
                System.out.println("THERE IS NO PATH!");
                return null;
            }

			// If the ID of node U is the same as the ID of the end node, the path has been found.
			// no need to continue the search.
            if(U.getId() == to.getId()) {
				System.out.println("Found the road");
				break;
			}

            // Runs trough all the nodes that U leads to. One at a time.
            for (Integer i : U) {
                Road V = model.getRoad(i);

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

    public static void main(String[] args)
    {
		Point2D.Double p = new Point2D.Double(2.0, 2.0);

		MapModel model = new MapModel();

		Road AAA = new Road(0, "AA",p,p,2,2.0,5, 1, 2, "a", "b", 1, 1); model.addRoad(AAA);
		Road AB = new Road(1, "AB",p,p,2,2.0,1, 1, 2, "a", "b", 1, 1); model.addRoad(AB);
		Road AAC = new Road(2, "AAC",p,p,2,2.0,2, 1, 2, "a", "b", 1, 1); model.addRoad(AAC);
		Road BD = new Road(3, "BD",p,p,2,2.0,1, 1, 2, "a", "b", 1, 1); model.addRoad(BD);
		Road CE = new Road(4, "CE",p,p,2,2.0,2, 1, 2, "a", "b", 1, 1); model.addRoad(CE);
		Road DF = new Road(5, "DF",p,p,2,2.0,1, 1, 2, "a", "b", 1, 1); model.addRoad(DF);
		Road EG = new Road(6, "EG",p,p,2,2.0,2, 1, 2, "a", "b", 1, 1); model.addRoad(EG);
		Road FG = new Road(7, "FG",p,p,2,2.0,5, 1, 2, "a", "b", 1, 1); model.addRoad(FG);

		AAA.addEdge(AB); AAA.addEdge(AAC);
		AB.addEdge(BD);
		BD.addEdge(DF);
		DF.addEdge(FG);
		AAC.addEdge(CE);
		CE.addEdge(EG);
		EG.addEdge(FG);


		DijkstraEdge[] arr = DijkstraSP.onLiner(model, 8, AAA, FG);
		int prev = FG.getId();
		System.out.println(arr[prev].getId());
	}
}
