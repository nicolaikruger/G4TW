package dk.itu.kf04.g4tw.model.tree;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Road;
import dk.itu.kf04.g4tw.util.DynamicArray;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * A 2-dimensional binary tree storing roads.
 */
public class Tree2D implements Externalizable {

    /**
     * The model in which the roads are stored.
     */
    protected MapModel model;

	/**
	 * The root of the tree.
	 */
	protected TreeNode root;

    /**
     * The number of nodes in the tree.
     */
    protected int size = 0;

    /**
     * Creates a tree based in the given model.
     * @param model  The model in which the roads of the tree are stored.
     * @throws IllegalArgumentException If the model is null.
     */
    public Tree2D(MapModel model) throws IllegalArgumentException {
        if (model == null) throw new IllegalArgumentException("Model cannot be null.");

        this.model = model;
    }

	/**
	 * Adds a node to the tree.
	 * @param road The road that belongs to the node
	 */
	public void addNode(Road road)
	{
        // Add the road
		if(root == null) root = new TreeNode(true, road);
		else             root.add(road);
        
        // Increment size
        size++;
	}

	/**
	 * Search for the roads the intersects with the rectangle defined by the given coordinates.
     * @param arr The array to search in
     * @param xMin The x-value of the upper left corner of the window-query
     * @param yMin The y-value of the upper left corner of the window-query
     * @param xMax The x-value of the lower right corner of the window-query
     * @param yMax The y-value of the lower right corner of the window-query
	 */
	public void search(DynamicArray<Road> arr, double xMin, double yMin, double xMax, double yMax) {
		root.search(arr, new RoadRectangle(xMin, yMin, xMax, yMax));
	}

    /**
     * Writes this tree to an output stream.
     * @param out  The output stream to write to
     * @throws IOException
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        writeNode(out, root);
    }

    /**
     * Writes nodes recursively
     * @param out  The output stream to write to
     * @param node  The parent node of the sub-tree to write
     * @throws IOException Unexpected IO exception.
     */
    protected void writeNode(ObjectOutput out, TreeNode node) throws IOException {
        if (node != null) {
            // Yes a node exists!
            out.writeBoolean(true);
            
            // Write the node
            out.writeBoolean(node.useX);
            out.writeInt(node.road.id);
    
            // Write the children
            writeNode(out, node.getLeftTreeNode());
            writeNode(out, node.getRightTreeNode());
        } else {
            // No, this node does not exist
            out.writeBoolean(false);
        }
    }

    /**
     * Reads an external ObjectInput and attempts to convert it to a Tree2D.
     * @param in  The ObjectInput from which to read the data.
     * @throws IOException  Unexpected IO exception.
     * @throws ClassNotFoundException  If casting to Tree2D - or TreeNode - fails.
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.root = readNode(in);
    }

    /**
     * Reads a single node and its children recursively.
     * @param in  The ObjectInput containing the data.
     * @return A TreeNode with the loaded data.
     * @throws IOException  Unexpected IO exception.
     */
    protected TreeNode readNode(ObjectInput in) throws IOException {
        // Test if the node exists
        boolean exists = in.readBoolean();
        if (exists) {
            // Fetch the data
            boolean useX = in.readBoolean();
            int id       = in.readInt();

            // Fetch the child nodes (if any)
            TreeNode left  = readNode(in);
            TreeNode right = readNode(in);

            // Return the node
            return new TreeNode(useX, model.getRoad(id), left, right);

        } else return null;
    }
    
}