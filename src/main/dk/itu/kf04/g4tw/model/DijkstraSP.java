package dk.itu.kf04.g4tw.model;

import com.sun.org.apache.xpath.internal.SourceTree;
import dk.itu.kf04.g4tw.util.DynamicArray;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *
 */
public class DijkstraSP {

    private DijkstraEdge[] edgeTo;
    private double[] distTo;
    private boolean[] visited;
    private PriorityQueue<DijkstraEdge> queue;

    private int jackPot;
    private int N;

    
    public DijkstraSP(int N) {
        this.N = N;
    }

    public DijkstraEdge[] findPath (Road from, Road to)
    {
        init();

        jackPot = to.id;
        distTo[from.id] = 0;

        visit(from);
        return edgeTo;
    }

    private void visit(DijkstraEdge e)
    {
        Iterator<DijkstraEdge> it = e.iterator();
        while(it.hasNext())
        {
            queue.add(it.next());
        }

        while(!queue.isEmpty())
        {
            DijkstraEdge current = queue.poll();
            if(current == null) {System.out.println("Current == null!"); return; }

            if(!visited[current.getId()])
            {
                visited[current.getId()] = true;
                if(distTo[e.getId()] == Double.POSITIVE_INFINITY) { System.out.println("Dist == INFINITY (and beyond)!"); return; }
                double length = distTo[e.getId()] + current.getLength();

                if(distTo[current.getId()] > length)
                {
                    distTo[current.getId()] = length;
                    edgeTo[current.getId()] = e;
                }

                if(current.getId() == jackPot) {System.out.println("DISCO! The length is: " + distTo[jackPot]);}

                visit(current);
            }
        }
    }

    public double getDist()
    {
        if(distTo[jackPot] < Double.POSITIVE_INFINITY) return distTo[jackPot];
        else return -1;
    }

    private void init()
    {
        edgeTo = new DijkstraEdge[N];
        distTo = new double[N];
        visited = new boolean[N];

        queue = new PriorityQueue<DijkstraEdge>(N, new Comparator<DijkstraEdge>() {
            public int compare(DijkstraEdge o1, DijkstraEdge o2) {
                if(o1.getLength() < o2.getLength()) return 1;
                if(o1.getLength() == o2.getLength()) return 0;
                else return -1;
            }
        });

        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
    }

    public static DijkstraEdge[] onLiner(int N, DijkstraEdge from, DijkstraEdge to)
    {
        final double[] dist = new double[N];
        boolean[] visited = new boolean[N];
        DijkstraEdge[] previous = new DijkstraEdge[N];

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(visited, false);

        dist[from.getId()] = 0;
        visited[from.getId()] = true;

        PriorityQueue<DijkstraEdge> Q = new PriorityQueue<DijkstraEdge>(N, new Comparator<DijkstraEdge>() {
            public int compare(DijkstraEdge o1, DijkstraEdge o2) {
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
            DijkstraEdge U = Q.poll();
            visited[U.getId()] = true;

            if(dist[U.getId()] == Double.POSITIVE_INFINITY)
            {
                System.out.println("THERE IS NO PATH!");
                return null;
            }

            if(U.getId() == to.getId()) break;

            Iterator<DijkstraEdge> it = U.iterator();
            while(it.hasNext())
            {
                DijkstraEdge V = it.next();
                if(!visited[V.getId()])
                {
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
        DijkstraSP DSP = new DijkstraSP(4);
        Point2D.Double p = new Point2D.Double(2.0, 2.0);

        Road AB = new Road(0, "AB",p,p,2,2.0,1);
        Road AC = new Road(1, "AC",p,p,2,2.0,2);
        Road AD = new Road(2, "AD",p,p,2,2.0,1);
        Road BD = new Road(3, "BD",p,p,2,2.0,2);
        Road BE = new Road(4, "BE",p,p,2,2.0,3);
        Road CF = new Road(5, "CF",p,p,2,2.0,1);
        Road DG = new Road(6, "DG",p,p,2,2.0,2);
        Road EF = new Road(7, "EF",p,p,2,2.0,1);
        Road EG = new Road(8, "EG",p,p,2,2.0,4);
        Road FH = new Road(9, "FH",p,p,2,2.0,4);
        Road GH = new Road(10, "GH",p,p,2,2.0,1);
        Road GI = new Road(11, "GI",p,p,2,2.0,3);
        Road HI = new Road(12, "HI",p,p,2,2.0,1);

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

        DijkstraEdge[] arr = DijkstraSP.onLiner(13, AB, HI);
        //DijkstraEdge[] arr = DSP.findPath(AB, CD);
        int prev = HI.getId();
        while(arr[prev] != null)
        {
            System.out.println(arr[prev] + "-->");
            prev = arr[prev].getId();
        }

        //System.out.println(arr[HI.getId()].toString());
        //System.out.println(Arrays.toString(DijkstraSP.onLiner(4, AB, CD)));
        //System.out.println(DSP.getDist());
    }

}
