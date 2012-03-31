package dk.itu.kf04.g4tw.RoadKill.tree;

/**
 * A Red-Black binary k-dimensional search-tree. The tree builds on the ComparableByDimension interface
 * to compare different comparable objects.
 */
public class RBTree<Key extends ComparableByDimension, Value> {

    //
    protected static final boolean RED   = true;
    protected static final boolean BLACK = false;
    
    protected final byte MAX_DIMENSIONS;

    /**
     * Constructs a Red-Black tree with the given number of dimensions.
     */
    public RBTree(int dimensions) {
        if (dimensions < 2) {
            throw new IllegalArgumentException("Cannot construct a Red-Black tree with less than 2 dimensions.");
        } else if (dimensions > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("Cannot construct a Red-Black tree with more dimensions than " + Byte.MAX_VALUE + ".");
        }
        
        MAX_DIMENSIONS = (byte) dimensions;
    }

    /**
     * The root of the tree.
     */
    protected RBNode<Key, Value> root;

    /**
     * Compares two keys by all the axises available. If the first comparison is zero (equality)
     * then examine the next dimension, until a difference is found or the same dimension is compared again.
     *
     * @param k1  The first key
     * @param k2  The second key
     * @return  A negative number if k1 < k2, zero if they are equal (in all axises) and a positive number if k1 > k2
     */
    protected int compare(Key k1, Key k2, byte axis) {
        final byte initialAxis = axis;

        // Iterate through the axises until we find a difference
        do {
            int comparison = k1.compareTo(k2, axis);
            if (comparison != 0) return comparison;
            else axis = incrementAxis(axis);
        } while (axis != initialAxis);

        // .. or we are back at the initial axis
        // In that case we have equality
        return 0;
    }

    /**
     * Flips the color of the given Node and its two subtrees.
     *
     * @param node  The node whose color to flip
     */
    public void flipColors(RBNode node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    /**
     * Increments the axis-value by one, and crops it inside a floor of 1 <= dimension <= MAX_DIMENSIONS.
     * @param axis  The current axis to increment.
     * @return The axis incremented or set to 1.
     */
    protected byte incrementAxis(byte axis) {
        return (byte) ((axis + 1) % MAX_DIMENSIONS + 1);
    }

    /**
     * Examines whether a given node is red.
     * @param node  The node to examine.
     * @return  True if node is red, false if node is black or null.
     */
    protected boolean isRed(RBNode node) {
        return node != null && node.color;
    }

    /**
     * Inserts a key-value pair into the tree.
     * @param key  The key to insert
     * @param value  The value to insert
     */
    public void put(Key key, Value value) {
        root = put(root, key, value, (byte) (root == null ? 1 : root.axis));
        root.color = BLACK;
    }
    
    /**
     * Inserts a key-value pair into the root Node.
     *
     * @param node  The node to insert into.
     * @param key  The key to insert.
     * @param value  The value to insert.
     * @return  The new node with the key-value pair inserted.
     */
    protected RBNode<Key, Value> put(RBNode<Key, Value> node, Key key, Value value, byte axis) {
        if (node == null) {
            return new RBNode<Key, Value>(key, value, 1, RED, incrementAxis(axis));
        }

        // Do the comparison (in all directions!)
        int comparison = compare(key, node.key, node.axis);

        // Insert
        if      (comparison < 0) node.left  = put(node.left, key, value, incrementAxis(axis));
        else if (comparison > 0) node.right = put(node.right, key, value, incrementAxis(axis));
        else node.value = value;
        
        // Balance the tree
        if (isRed(node.right) && !isRed(node.left))    node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);
        
        node.N = 1 + size(node.left) + size(node.right);
        return node;
    }

    /**
     * Rotates the given Node and its children left. This is done to avoid right-leaning trees and
     * two red links in a row. The rotation preserves the order of the tree-invariants.
     * 
     * @param node  The Node to rotate.
     * @return The resulting parent Node after the rotation.            
     */
    protected RBNode<Key, Value> rotateLeft(RBNode<Key, Value> node) {
        RBNode<Key, Value> tmp = node.right;
        node.right = tmp.left;
        tmp.left = node;
        tmp.color = node.color;
        node.color = RED;
        tmp.N = node.N;
        node.N = 1 + size(node.left) + size(node.right);
        return tmp;
    }

    /**
     * Rotates the given Node and its children left. This can be useful when we need to avoid double red-links
     * in the left subtree, relative to the given node.
     *
     * @param node  The Node to rotate.
     * @return The resulting parent Node after the rotation.
     */
    protected RBNode<Key, Value> rotateRight(RBNode<Key, Value> node) {
        RBNode<Key, Value> tmp = node.left;
        node.left = tmp.right;
        tmp.right = node;
        tmp.color = node.color;
        node.color = RED;
        tmp.N = node.N;
        node.N = 1 + size(node.left) + size(node.right);
        return tmp;
    }

    /**
     * A quick-access method for the size of a Node
     * @param node The node to examine.
     * @return The number of children below the Node (Node included).
     */
    private int size(RBNode node) { return node == null ? 0 : node.N; }
    
    @Override public String toString() {
        return "RBTree: " + (root == null ? 0 : root.N) + " node" + (size(root) == 1 ? "" : "s") + " sorted in " + MAX_DIMENSIONS + " axises.";
    }
    
    public static void main(String[] args) {
        RBTree<RoadRectangle, String> tree = new RBTree(4);
        RoadRectangle r1 = new RoadRectangle(0, 0, 0, 0);
        RoadRectangle r2 = new RoadRectangle(1, 1, 1, 1);
        RoadRectangle r3 = new RoadRectangle(4, 4, 4, 4);
        RoadRectangle r4 = new RoadRectangle(3, 3, 3, 3);
        tree.put(r1, "Hej");
        tree.put(r2, "Hej2");
        tree.put(r3, "Hej3");
        tree.put(r4, "Hej4");

        System.out.println(tree);
    }

}
