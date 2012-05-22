package dk.itu.kf04.g4tw.model;

import org.junit.Test;

import java.awt.geom.Point2D;

import static junit.framework.Assert.assertEquals;

/**
 * Test the Road class.
 */
public class RoadTest {
    
    protected Road a = new Road(0, "Ferskenvej",
            new Point2D.Double(0, 0), new Point2D.Double(100, 100),
            MapModel.MINOR_ROAD, 80, 213.5, 1, 11, "A", "C", 6830, 6830);
    protected Road b = new Road(1, "Åboulevarden",
            new Point2D.Double(50, 50), new Point2D.Double(150, 150),
            MapModel.PATH, 1, Math.PI, 2, 16, "", "", 1350, 1350);
    
    @Test public void getIdTest() {
        assertEquals(a.getId(), 0);
        assertEquals(b.getId(), 1);
    }
    
    @Test public void getLengthTest() {
        assertEquals(a.getLength(), 213.5);
        assertEquals(b.getLength(), Math.PI);
    }
    
}
