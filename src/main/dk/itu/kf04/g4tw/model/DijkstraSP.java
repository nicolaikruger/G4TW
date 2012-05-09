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
        final double[] dist = new double[N];
        boolean[] visited = new boolean[N];
        Road[] previous = new Road[N];

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(visited, false);

        dist[from.getId()] = 0;

        PriorityQueue<Road> Q = new PriorityQueue<Road>(N, new Comparator<Road>() {
            public int compare(Road o1, Road o2) {
                double dist1 = dist[o1.getId()];
                double dist2 = dist[o2.getId()];

                if(dist1 > dist2) return 1;
                if(dist1 == dist2) return 0;
                else return -1;
            }
        });

        Q.add(from);

        while(!Q.isEmpty())
        {
            Road U = Q.poll();
            visited[U.getId()] = true;

            if(dist[U.getId()] == Double.POSITIVE_INFINITY)
            {
                System.out.println("THERE IS NO PATH!");
                return null;
            }

            if(U.getId() == to.getId()) {
				System.out.println("Found the road");
				break;
			}

            // Krüger: I made this into a foreach loop. IntelliJ insisted...! Sorry...
            for (Integer i : U) {
                Road V = model.getRoad(i);
                if(!visited[V.getId()])
                {
                    visited[V.getId()] = true;
                    Q.add(V);
                    double alt = dist[U.getId()] + V.getLength();
                    double vDist = dist[V.getId()];
                    if(alt < vDist)
                    {
                        dist[V.getId()] = alt;
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

        Road AB = new Road(0, "AB",p,p,2,2.0,1, 1, 2, "a", "b", 1, 1); model.addRoad(AB);
        Road AC = new Road(1, "AC",p,p,2,2.0,2, 1, 2, "a", "b", 1, 1); model.addRoad(AC);
        Road AD = new Road(2, "AD",p,p,2,2.0,1, 1, 2, "a", "b", 1, 1); model.addRoad(AD);
        Road BD = new Road(3, "BD",p,p,2,2.0,2, 1, 2, "a", "b", 1, 1); model.addRoad(BD);
        Road BE = new Road(4, "BE",p,p,2,2.0,3, 1, 2, "a", "b", 1, 1); model.addRoad(BE);
        Road CF = new Road(5, "CF",p,p,2,2.0,1, 1, 2, "a", "b", 1, 1); model.addRoad(CF);
        Road DG = new Road(6, "DG",p,p,2,2.0,2, 1, 2, "a", "b", 1, 1); model.addRoad(DG);
        Road EF = new Road(7, "EF",p,p,2,2.0,1, 1, 2, "a", "b", 1, 1); model.addRoad(EF);
        Road EG = new Road(8, "EG",p,p,2,2.0,4, 1, 2, "a", "b", 1, 1); model.addRoad(EG);
        Road FH = new Road(9, "FH",p,p,2,2.0,4, 1, 2, "a", "b", 1, 1); model.addRoad(FH);
        Road GH = new Road(10, "GH",p,p,2,2.0,1, 1, 2, "a", "b", 1, 1); model.addRoad(GH);
        Road GI = new Road(11, "GI",p,p,2,2.0,3, 1, 2, "a", "b", 1, 1); model.addRoad(GI);
        Road HI = new Road(12, "HI",p,p,2,2.0,1, 1, 2, "a", "b", 1, 1); model.addRoad(HI);

        AB.addEdge(AC);        AB.addEdge(AD);        AB.addEdge(BD);        AB.addEdge(BD);

        AC.addEdge(AB);        AC.addEdge(AD);        AC.addEdge(CF);

        AD.addEdge(AB);        AD.addEdge(AC);        AD.addEdge(BD);        AD.addEdge(DG);

        BD.addEdge(AB);        BD.addEdge(AC);        BD.addEdge(AD);        BD.addEdge(BE);        BD.addEdge(DG);

        BE.addEdge(AB);        BE.addEdge(BD);        BE.addEdge(EF);        BE.addEdge(EG);

        CF.addEdge(AC);        CF.addEdge(EF);        CF.addEdge(FH);

        DG.addEdge(AD);        DG.addEdge(BD);        DG.addEdge(EG);        DG.addEdge(GH);        DG.addEdge(GI);

        EF.addEdge(BE);        EF.addEdge(CF);        EF.addEdge(EG);        EF.addEdge(FH);

        EG.addEdge(BE);        EG.addEdge(DG);        EG.addEdge(EF);        EG.addEdge(GH);        EG.addEdge(GI);

        FH.addEdge(CF);        FH.addEdge(EF);        FH.addEdge(GH);

        GH.addEdge(DG);        GH.addEdge(EG);        GH.addEdge(FH);        GH.addEdge(GI);        GH.addEdge(HI);

        GI.addEdge(DG);        GI.addEdge(EG);        GI.addEdge(GH);        GI.addEdge(HI);

        HI.addEdge(FH);        HI.addEdge(GH);        HI.addEdge(GI);

        DijkstraEdge[] arr = DijkstraSP.onLiner(model, 13, AB, HI);
        int prev = HI.getId();
        while(arr[prev] != null)
        {
            System.out.println(arr[prev] + "-->");
            prev = arr[prev].getId();
        }
    }

}
