package dk.itu.kf04.g4tw.model.tree;

import dk.itu.kf04.g4tw.model.Road;
import dk.itu.kf04.g4tw.util.DynamicArray;

/**
 * A TreeNode containing information about the geometric properties of the road (RoadRectangle) and the
 * id of the road.
 */
public class TreeNode {

	/**
	 * A boolean value indicating whether we use the x-axis
	 */
	public final boolean useX;

	/**
	 * The left TreeNode - if any.
	 */
	protected TreeNode leftTreeNode = null;

    /**
	 * The right TreeNode - if any.
	 */
	protected TreeNode rightTreeNode = null;

	/**
	 * The id of the road.
	 */
	public final Road road;

	/**
	 * Constructs a TreeNode
	 * @param useX  Whether or not to use the x-axis to compare other rectangles.
	 * @param road  This road.
	 */
	public TreeNode (boolean useX, Road road)
	{
		this.useX = useX;
		this.road = road;
	}

    /**
     * Constructs a TreeNode with the given children
     * @param useX  Whether to compare the x- or y-axis
     * @param road  The road of the node
     * @param left  The left child
     * @param right The right child              
     */
    public TreeNode (boolean useX, Road road, TreeNode left, TreeNode right) {
        this.useX = useX;
        this.road = road;
        this.leftTreeNode = left;
        this.rightTreeNode = right;
    }

	/**
	 * Adds a road beneath this node.
     * @param that  The road to add.
	 */
	public void add(Road that)
	{
		if(useX) {
			if(road.rect.xMin < that.rect.xMin) {
				if(rightTreeNode == null)	rightTreeNode = new TreeNode(false, that);
				else						rightTreeNode.add(that);
			} else {
				if(leftTreeNode == null)	leftTreeNode = new TreeNode(false, that);
				else						leftTreeNode.add(that);
			}
		} else {
			if(road.rect.yMin < that.rect.yMin) {
				if(rightTreeNode == null)	rightTreeNode = new TreeNode(true, that);
				else						rightTreeNode.add(that);
			} else {
				if(leftTreeNode == null)	leftTreeNode = new TreeNode(true, that);
				else						leftTreeNode.add(that);
			}
		}
	}

    /**
     * Fetches the left tree node. Can be null
     * @return  The left node if it exists.
     */
    public TreeNode getLeftTreeNode() {
        return leftTreeNode;
    }

    /**
     * Fetches the right tree node. Can be null
     * @return  The right node if it exists.
     */
    public TreeNode getRightTreeNode() {
        return rightTreeNode;
    }
	
	/**
	 *Searches for nodes that intersects the given RoadRectangle.
     * @param arr  The array to insert the search-result in
	 * @param query  The rectangle we want to find roads inside.
	 * @return The previous search plus the roads in the node that intersects the given query-rectangle.
	 */
	public DynamicArray<Road> search(DynamicArray<Road> arr, RoadRectangle query)
	{
		if(road.rect.intersects(query)) arr.add(road);
		
		if(useX) {
			if(road.rect.xMin >= query.xMax && leftTreeNode != null) // Search only in the TreeNodes to the left
				leftTreeNode.search(arr, query);
			else if(road.rect.xMax <= query.xMin && rightTreeNode != null) // Search only in the TreeNodes to the right
				rightTreeNode.search(arr, query);
			else {  // Search in the TreeNodes to the left and the right
				if(leftTreeNode != null)
					leftTreeNode.search(arr, query);
				if(rightTreeNode != null)
					rightTreeNode.search(arr, query);
			}
		} else {
			if(road.rect.yMin >= query.yMax && leftTreeNode != null) // Search only in the TreeNodes to the left
				leftTreeNode.search(arr, query);
			else if(road.rect.yMax <= query.yMin && rightTreeNode != null) // Search only in the TreeNodes to the right
				rightTreeNode.search(arr, query);
			else {  // Search in the TreeNodes to the left and the right
				if(leftTreeNode != null)
					leftTreeNode.search(arr, query);
				if(rightTreeNode != null)
					rightTreeNode.search(arr, query);
			}
		}
		return arr;
	}
}