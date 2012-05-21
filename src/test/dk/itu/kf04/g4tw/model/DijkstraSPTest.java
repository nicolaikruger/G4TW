package dk.itu.kf04.g4tw.model;

import org.junit.Test;

import java.awt.geom.Point2D;

/**
 * Tests the Dijkstra graph searching algorithm.
 *
 * @author Nicolai Krüger <nkrk@itu.dk>
 */
public class DijkstraSPTest {

    @Test public void truePath() throws Exception {
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

		AAA.addEdge(AB.id); AAA.addEdge(AAC.id);
		AB.addEdge(BD.id);
		BD.addEdge(DF.id);
		DF.addEdge(FG.id);
		AAC.addEdge(CE.id);
		CE.addEdge(EG.id);
		EG.addEdge(FG.id);


		//DijkstraEdge[] arr = model.Dijkstra(8, AAA, FG);
		int prev = FG.getId();

		//assertEquals(DF, arr[prev]);
	}

	@Test
	public void noPath() throws Exception {
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

		AAA.addEdge(AB.id); AAA.addEdge(AAC.id);
		AB.addEdge(BD.id);
		BD.addEdge(DF.id);
		AAC.addEdge(CE.id);
		EG.addEdge(FG.id);


		//DijkstraEdge[] arr = model.Dijkstra(8, AAA, FG);
		int prev = FG.getId();

		//assertNull(arr[prev]);
	}
}
