package dk.itu.kf04.g4tw.model;

import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.Random;

import static junit.framework.Assert.*;

/**
 * Tests the MapModel.
 */
public class MapModelTest extends MapModel {

    public MapModelTest() {
        super();
    }

    @Test
    public void creation() {
        assertEquals(32, roadTypeMap.size());
        assertEquals(0, roadTrees.size());
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void tooBig() {
        getRoad(numberOfRoads);
    }

    @Test
    public void edgeAndRoadTheSame() {
        for (int i = 0; i < 10; i++) {
            int id = new Random().nextInt(numberOfRoads);
            assertSame(getRoad(id), getEdge(id));
        }
    }

    @Test
    public void canAddRoad() {
        Road r = new Road(12, "A Road", new Point2D.Double(0, 0), new Point2D.Double(-102, 311), MapModel.MINOR_ROAD, 80, 213, 1, 11, "A", "C", 6830, 6830);
        addRoad(r);
        
        // Right one?
        assertEquals(getRoad(r.id).id, r.id);
        assertEquals(getRoad(r.id).name, r.name);
    }

    @Test
    public void roadTypeFromId() {
        int type;
        type = getRoadTypeFromId(22);
        assertEquals(type, MapModel.EXPRESSWAY);
        type = getRoadTypeFromId(48);
        assertEquals(type, MapModel.PATH);
    }

}
