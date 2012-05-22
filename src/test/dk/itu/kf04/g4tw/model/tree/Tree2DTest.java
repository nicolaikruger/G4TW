package dk.itu.kf04.g4tw.model.tree;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Road;
import dk.itu.kf04.g4tw.util.DynamicArray;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static junit.framework.Assert.assertEquals;

/**
 * Tests the KD-tree.
 */
public class Tree2DTest extends Tree2D {
    
    TreeNode t1;
    TreeNode t2;

    protected Road a = new Road(0, "Ferskenvej",
            new Point2D.Double(0, 0), new Point2D.Double(100, 100),
            MapModel.MINOR_ROAD, 80, 213, 1, 11, "A", "C", 6830, 6830);
    protected Road b = new Road(1, "Åboulevarden",
            new Point2D.Double(50, 50), new Point2D.Double(150, 150),
            MapModel.PATH, 1, 17, 2, 16, "", "", 1350, 1350);

    @Before
    public void setUp() {
        t1 = null;
        t2 = null;
    }
    
    public Tree2DTest() {
        super(new MapModel());
    }

    @Test (expected = IllegalArgumentException.class)
    public void creationNullTest() {
        new Tree2D(null);
    }
    
    @Test
    public void additionTest() {
        // Test for null root
        assertEquals(null, root);
        
        // Add a root
        addNode(a);
        
        assertEquals(a, root.road);
        assertEquals(true, root.useX);
        
        // Add a child
        addNode(b);
        assertEquals(a, root.road);
        assertEquals(true, root.useX);
        assertEquals(b, root.getRightTreeNode().road);
        assertEquals(false, root.getRightTreeNode().useX);
    }

    /**
     * Test the searching in the tree.
     * @see dk.itu.kf04.g4tw.model.tree.TreeNodeTest#searchTest()
     */
    @Test public void searchTest() {
        DynamicArray<Road> arr = new DynamicArray<Road>();
        addNode(b);
        addNode(a);

        search(arr, 0, 0, 200, 200);
        assertEquals(2, arr.length());

        arr = new DynamicArray<Road>();
        search(arr, -1000, -2000, -0.0000001, -0.0000001);
        assertEquals(0, arr.length());
    }

}
