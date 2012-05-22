package dk.itu.kf04.g4tw.model.tree;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the RoadRectangle class.
 */
public class RoadRectangleTest {

    protected RoadRectangle r1;
    protected RoadRectangle r2;

    @Before
    public void setUp() { r1 = null; r2 = null; }

    @Test
    public void sameValueTest() {
        new RoadRectangle(0, 0, 0, 0);
    }

    @Test
    public void sameXValueTest() {
        r1 = new RoadRectangle(0, -12313.42, 0, 71023.12);
    }

    @Test
    public void sameYValueTest() {
        r1 = new RoadRectangle(92721.3, 0, -123312.14, 0);
    }

    @Test public void storeCorrectValueTest() {
        // All flipped
        RoadRectangle r = new RoadRectangle(4, 3, 2, 1);
        assertEquals(r.xMin, 2.0);
        assertEquals(r.xMax, 4.0);
        assertEquals(r.yMin, 1.0);
        assertEquals(r.yMax, 3.0);

        // Only y coordinates flipped
        r = new RoadRectangle(4, 1, 2, 3);
        assertEquals(r.xMin, 2.0);
        assertEquals(r.xMax, 4.0);
        assertEquals(r.yMin, 1.0);
        assertEquals(r.yMax, 3.0);

        // Only x coordinates flipped
        r = new RoadRectangle(2, 3, 4, 1);
        assertEquals(r.xMin, 2.0);
        assertEquals(r.xMax, 4.0);
        assertEquals(r.yMin, 1.0);
        assertEquals(r.yMax, 3.0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getDimensionFailTest1() {
        r1 = new RoadRectangle(3, 5, 7, 9);
        r1.getDimensionValue((byte) 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getDimensionFailTest2() {
        r1 = new RoadRectangle(3, 5, 7, 9);
        r1.getDimensionValue((byte) 5);
    }

    @Test
    public void getDimensionTest() {
        r1 = new RoadRectangle(3, 5, 7, 9);
        assertEquals(r1.getDimensionValue((byte) 1), 3.0);
        assertEquals(r1.getDimensionValue((byte) 2), 5.0);
        assertEquals(r1.getDimensionValue((byte) 3), 7.0);
        assertEquals(r1.getDimensionValue((byte) 4), 9.0);
    }

    @Test
    public void compareTest() {
        // Normal values
        r1 = new RoadRectangle(-312321.32121, Math.PI, Math.E, 812314422.1);
        r2 = new RoadRectangle(-312321.32121, Math.PI, Math.E, 812314422.1);
        assertEquals(r1.compareTo(r2, (byte) 1), 0);
        assertEquals(r1.compareTo(r2, (byte) 2), 0);
        assertEquals(r1.compareTo(r2, (byte) 3), 0);
        assertEquals(r1.compareTo(r2, (byte) 4), 0);
        
        // Weird values
        r1 = new RoadRectangle(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN, Double.NEGATIVE_INFINITY);
        r2 = new RoadRectangle(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN, Double.NEGATIVE_INFINITY);
        assertEquals(r1.compareTo(r2, (byte) 1), 0);
        assertEquals(r1.compareTo(r2, (byte) 2), 0);
        assertEquals(r1.compareTo(r2, (byte) 3), 0);
        assertEquals(r1.compareTo(r2, (byte) 4), 0);
    }
    
    @Test
    public void equalsTest() {
        // Normal values
        r1 = new RoadRectangle(-312321.32121, Math.PI, Math.E, 812314422.1);
        r2 = new RoadRectangle(-312321.32121, Math.PI, Math.E, 812314422.1);
        assertTrue(r1.equals(r2));
        
        // Weird values
        r1 = new RoadRectangle(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN, Double.NEGATIVE_INFINITY);
        r2 = new RoadRectangle(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN, Double.NEGATIVE_INFINITY);
        assertTrue(r1.equals(r2));
    }
    
    @Test
    public void intersectionTest() {
        // Diagonal test 1
        r1 = new RoadRectangle(0, 0, 1, 1);
        r2 = new RoadRectangle(1, 1, 2, 2);
        assertTrue(r1.intersects(r2));
        assertTrue(r2.intersects(r1));
        assertTrue(r1.intersects(r1));
        assertTrue(r2.intersects(r2));

        // Diagonal test 2
        r1 = new RoadRectangle(1, 1, 2, 2);
        r2 = new RoadRectangle(1.99999, 1.99999, 2.5, 2.5);
        assertTrue(r1.intersects(r2));
        assertTrue(r2.intersects(r1));

        // Horizontal
        r1 = new RoadRectangle(0, 0, 1, 1);
        r2 = new RoadRectangle(0.5, 0, 1.5, 1);
        assertTrue(r1.intersects(r2));
        assertTrue(r2.intersects(r1));

        // Vertical
        r1 = new RoadRectangle(0, 0, 1, 1);
        r2 = new RoadRectangle(0, 0.5, 1, 1.5);
        assertTrue(r1.intersects(r2));
        assertTrue(r2.intersects(r1));
    }

}
