package dk.itu.kf04.g4tw.RoadKill.tree;

/**
 * A mutable node in a Red-Black binary kd-tree.
 */
class RBNode<Key extends ComparableByDimension, Value> {

    /**
     * The color of the Node.
     */
    boolean color;

    /**
     * The axis the Node compares its children against.
     */
    byte axis;

    /**
     * The key in the Node.
     */
    Key key;

    /**
     * The left subtree.
     */
    RBNode<Key, Value> left;

    /**
     * The number of nodes in this tree and its subtrees.
     */
    int N;

    /**
     * The right subtree.
     */
    RBNode<Key, Value> right;

    /**
     * The value of the Node.
     */
    Value value;

    /**
     * Constructs a Red-Black node with no subtrees.
     * @param key  The key of the Node
     * @param value  The value of the Node
     * @param N  The number of children in the subtree
     * @param color  The color of the Node
     * @param axis  The axis to use for comparison in this node.
     */
    public RBNode(Key key, Value value, int N, boolean color, byte axis) {
        // Ensure N is positive.
        if (N < 0) N = 0;

        this.key   = key;
        this.value = value;
        this.N     = N;
        this.color = color;
        this.axis  = axis;
        this.left  = null;
        this.right = null;
    }

    @Override public String toString() {
        return "Node with " + N + " children: " + key + " -> " + value + ".";
    }
}