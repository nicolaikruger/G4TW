package dk.itu.kf04.g4tw.model.tree;

import dk.itu.kf04.g4tw.model.Road;
import dk.itu.kf04.g4tw.util.DynamicArray;

import java.io.Serializable;

/**
 * A TreeNode containing information about the geometric properties of the road (RoadRectangle) and the
 * id of the road.
 */
public class TreeNode implements Serializable {

	/**
	 * A boolean value indicating whether we use the x-axis
	 */
	private boolean useX;

	/**
	 * The left TreeNode - if any.
	 */
	private TreeNode leftTreeNode = null;

	/**
	 * The right TreeNode - if any.
	 */
	private TreeNode rightTreeNode = null;

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
	 * Adds a tree node beneath this node.
	 */
	public void addTreeNode(Road that)
	{
		if(useX) {
			if(road.rect.xMin < that.rect.xMin) {
				if(rightTreeNode == null)	rightTreeNode = new TreeNode(false, that);
				else						rightTreeNode.addTreeNode(that);
			} else {
				if(leftTreeNode == null)	leftTreeNode = new TreeNode(false, that);
				else						leftTreeNode.addTreeNode(that);
			}
		} else {
			if(road.rect.yMin < that.rect.yMin) {
				if(rightTreeNode == null)	rightTreeNode = new TreeNode(true, that);
				else						rightTreeNode.addTreeNode(that);
			} else {
				if(leftTreeNode == null)	leftTreeNode = new TreeNode(true, that);
				else						leftTreeNode.addTreeNode(that);
			}
		}
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