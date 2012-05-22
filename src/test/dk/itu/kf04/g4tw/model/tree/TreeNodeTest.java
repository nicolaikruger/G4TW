package dk.itu.kf04.g4tw.model.tree;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Road;
import dk.itu.kf04.g4tw.util.DynamicArray;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

/**
 * Tests the TreeNode.
 */
public class TreeNodeTest {

    TreeNode t1;
    TreeNode t2;
    TreeNode t3;
    TreeNode t4;

    @Before
    public void setUp() {
        t1 = null; t2 = null; t3 = null; t4 = null;
    }

    protected Road a = new Road(0, "Ferskenvej",
            new Point2D.Double(0, 0), new Point2D.Double(100, 100),
            MapModel.MINOR_ROAD, 80, 213, 1, 11, "A", "C", 6830, 6830);
    protected Road b = new Road(1, "Åboulevarden",
            new Point2D.Double(50, 50), new Point2D.Double(150, 150),
            MapModel.PATH, 1, 17, 2, 16, "", "", 1350, 1350);

    @Test
    public void constructionTest() {
        t1 = new TreeNode(true, a);
        assertEquals(t1.road, a);
        assertEquals(t1.useX, true);

        // And now with children
        t1 = new TreeNode(false, a);
        t2 = null;
        t3 = new TreeNode(true, b, t1, t2);
        assertEquals(t3.useX, true);
        assertEquals(t3.getLeftTreeNode(), t1);
        assertEquals(t3.getLeftTreeNode().useX, false);
        assertEquals(t3.getRightTreeNode(), t2);
    }
    
    @Test
    public void additionTest() {
        // Add to the right
        t1 = new TreeNode(true, a);
        t1.add(b);
        assertEquals(t1.getRightTreeNode().useX, false); // Must be !true
        assertEquals(t1.getRightTreeNode().road, b);
        
        // Add to the left
        t1 = new TreeNode(false, b);
        t1.add(a);
        assertEquals(t1.getLeftTreeNode().useX, true); // Must be !false
        assertEquals(t1.getLeftTreeNode().road, a);
    }
    
    @Test
    public void searchTest() {
        DynamicArray<Road> arr = new DynamicArray<Road>();
        t1 = new TreeNode(true, a);
        t1.add(b);
        RoadRectangle q1 = new RoadRectangle(0, 0, 200, 200);
        RoadRectangle q2 = new RoadRectangle(140, 140, 200, 200);
        RoadRectangle q3 = new RoadRectangle(200, 200, 300, 300);

        t1.search(arr, q1);
        assertEquals(a, arr.get(0));
        assertEquals(b, arr.get(1));

        arr = new DynamicArray<Road>();

        t1.search(arr, q2);
        assertEquals(b, arr.get(0));

        arr = new DynamicArray<Road>();

        t1.search(arr, q3);
        assertEquals(arr.length(), 0);
    }
    
}
