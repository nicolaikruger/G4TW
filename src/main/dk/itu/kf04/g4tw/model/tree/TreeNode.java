package dk.itu.kf04.g4tw.model.tree;

import dk.itu.kf04.g4tw.model.Road;
import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.ArrayList;

/**
 * A TreeNode containing information about the geometric properties of the road (RoadRectangle) and the
 * id of the road.
 */
public class TreeNode {

	/**
	 * A boolean value indicating whether we use the x-axis
	 */
	private boolean useX;

	/**
	 * A boolean value indicating whether this is a "red" or a "black" node
	 */
	private boolean isRed;

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
	 * @param isRed Whether or not to this node has a "red" link to its parent
	 * @param road  This road.
	 */
	public TreeNode (boolean useX, boolean isRed, Road road)
	{
		this.useX = useX;
		this.isRed = isRed;
		this.road = road;
	}

	/**
	 * Constructs a TreeNode. The isRed boolean is being set to true;
	 * @param road  This road.
	 */
	public TreeNode (boolean useX, Road road)
	{
		this.useX = useX;
		this.isRed = true;
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
	 * @param query  The rectangle we want to find roads inside.
	 * @return A list of ids of the roads the intersects the given query-rectangle.
	 */
	public DynamicArray<Road> search(RoadRectangle query)
	{
		// TODO: Create own dynamic array with no generic types!
		DynamicArray<Road> returnList = new DynamicArray<Road>();
		if(road.rect.intersects(query)) returnList.add(road);
		
		if(useX) {
			if(road.rect.xMin >= query.xMax && leftTreeNode != null) // Search only in the TreeNodes to the left
				returnList.add(leftTreeNode.search(query));
			else if(road.rect.xMax <= query.xMin && rightTreeNode != null) // Search only in the TreeNodes to the right
				returnList.add(rightTreeNode.search(query));
			else {  // Search in the TreeNodes to the left and the right
				if(leftTreeNode != null)
					returnList.add(leftTreeNode.search(query));
				if(rightTreeNode != null)
					returnList.add(rightTreeNode.search(query));
			}
		} else {
			if(road.rect.yMin >= query.yMax && leftTreeNode != null) // Search only in the TreeNodes to the left
				returnList.add(leftTreeNode.search(query));
			else if(road.rect.yMax <= query.yMin && rightTreeNode != null) // Search only in the TreeNodes to the right
				returnList.add(rightTreeNode.search(query));
			else {  // Search in the TreeNodes to the left and the right
				if(leftTreeNode != null)
					returnList.add(leftTreeNode.search(query));
				if(rightTreeNode != null)
					returnList.add(rightTreeNode.search(query));
			}
		}
		return returnList;
	}
}