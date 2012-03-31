package dk.itu.kf04.g4tw.RoadKill.tree;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * A test for the RBTree.
 */
public class RBTreeTest extends RBTree {

    public RBTreeTest() {
        super(4);
    }

    private RBNode<RoadRectangle, String> node1 =
            new RBNode<RoadRectangle, String>(new RoadRectangle(0, 0, 100, 100), "TestCase1", 0, BLACK, (byte) 1);

    private RBNode<RoadRectangle, String> node2 =
                new RBNode<RoadRectangle, String>(new RoadRectangle(100, 100, 0, 0), "TestCase2", 2, RED, (byte) 2);

    @Test
    public void isRedTest() {
        assertFalse(isRed(node1));
        assertTrue(isRed(node2));
    }

}
