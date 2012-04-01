package dk.itu.kf04.g4tw.model.tree;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * A test for the Red-Black tree node.
 */
public class RBNodeTest {
    
    @Test
    public void canBeConstructedTest() {

        RoadRectangle rect1 = new RoadRectangle(0, 0, 100, 100);
        RoadRectangle rect2 = new RoadRectangle(100, 100, 200, -300);

        RBNode<RoadRectangle, String> node1 = new RBNode<RoadRectangle, String>(rect1, "NodeTest1", 42, RBTree.BLACK, (byte) 3);
        assertEquals(node1.key, rect1);
        assertEquals(node1.value, "NodeTest1");
        assertEquals(node1.N, 42);
        assertEquals(node1.color, dk.itu.kf04.g4tw.model.tree.RBTree.BLACK);

        RBNode<RoadRectangle, String> node2 = new RBNode<RoadRectangle, String>(rect2, "NodeTest2", -1003, RBTree.RED, (byte) 1);
        assertEquals(node2.key, rect2);
        assertEquals(node2.value, "NodeTest2");
        assertEquals(node2.N, 0);
        assertEquals(node2.color, dk.itu.kf04.g4tw.model.tree.RBTree.RED);
    }
    
}
