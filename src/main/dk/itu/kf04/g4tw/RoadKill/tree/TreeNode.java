package dk.itu.kf04.g4tw.RoadKill.tree;

import java.util.ArrayList;

/**
 * A TreeNode containing information about the geometric properties of the road (RoadRectangle) and the
 * id of the road.
 */
public class TreeNode {

	/**
	 * A boolean value indicating whether or not this TreeNode is the current root
	 */
	private  boolean  isRoot;

	/**
	 * A boolean value indicating whether we use the x-axis
	 */
	private boolean useX;

	/**
	 * A boolean value indicating whether this is a "red" or a "black" node
	 */
	private boolean isRed = true;

	/**
	 * The left TreeNode - if any.
	 */
	private TreeNode leftTreeNode = null;

	/**
	 * The right TreeNode - if any.
	 */
	private TreeNode rightTreeNode = null;

	/**
	 * The rectangle enclosing this road.
	 */
	private final RoadRectangle rect;

	/**
	 * The id of the road.
	 */
	public final int id;

	/**
	 * Constructs a TreeNode. This node is being set as the root.
	 * @param useX  Whether or not to use the x-axis to compare other rectangles.
	 * @param isRed Whether or not to this node has a "red" link to its parent
	 * @param rect  The rectangle enclosing this road.
	 * @param id  The id of this road.
	 */
	public TreeNode (RoadRectangle rect, int id)
	{
		this.isRoot = true;
		this.useX = true;
		this.rect = rect;
		this.id = id;
	}

	/**
	 * Constructs a TreeNode.
	 * @param useX  Whether or not to use the x-axis to compare other rectangles.
	 * @param rect  The rectangle enclosing this road.
	 * @param id  The id of this road.
	 */
	public TreeNode (boolean useX, RoadRectangle rect, int id)
	{
		this.isRoot = false;
		this.useX = useX;
		this.rect = rect;
		this.id = id;
	}

	/**
	 * Adds a tree node beneath this node.
	 * @param that  The rectangle of the road.
	 * @param id The id of the road.
	 */
	public void addTreeNode(RoadRectangle that, int id)
	{
		if(useX) {
			if(this.rect.xMin < that.xMin) {
				if(rightTreeNode == null)	rightTreeNode = new TreeNode(false, rect, id);
				else					rightTreeNode.addTreeNode(rect, id);
			} else {
				if(leftTreeNode == null)	leftTreeNode = new TreeNode(false, rect, id);
				else					leftTreeNode.addTreeNode(rect, id);
			}
		} else {
			if(this.rect.yMin < that.yMin) {
				if(rightTreeNode == null)	rightTreeNode = new TreeNode(true, rect, id);
				else					rightTreeNode.addTreeNode(rect, id);
			} else {
				if(leftTreeNode == null)	leftTreeNode = new TreeNode(true, rect, id);
				else					leftTreeNode.addTreeNode(rect, id);
			}
		}
	}
	
	/**
	 *Searches for nodes that intersects the given RoadRectangle.
	 * @param query  The rectangle we want to find roads inside.
	 * @return A list of ids of the roads the intersects the given query-rectangle.
	 */
	public ArrayList<Integer> search(RoadRectangle query)
	{
		// TODO: Create own dynamic array with no generic types!
		ArrayList<Integer> returnList = new ArrayList<Integer>();
		if(rect.intersects(query)) returnList.add(id);
		
		if(useX) {
			returnList.addAll(search(this.rect.xMin, this.rect.xMax, query.xMin, query.xMax, query));
//			if(this.rect.xMin >= query.xMax && leftTreeNode != null) // Search only in the TreeNodes to the left
//				returnList.addAll(leftTreeNode.search(query));
//			else if(this.rect.xMax <= query.xMin && rightTreeNode != null) // Search only in the TreeNodes to the right
//				returnList.addAll(rightTreeNode.search(query));
//			else {  // Search in the TreeNodes to the left and the right
//				if(leftTreeNode != null)
//					returnList.addAll(leftTreeNode.search(query));
//				if(rightTreeNode != null)
//					returnList.addAll(rightTreeNode.search(query));
//			}
		} else {
			returnList.addAll(search(this.rect.yMin, this.rect.yMax, query.yMin, query.yMax, query));
//			if(this.rect.yMin >= query.yMax && leftTreeNode != null) // Search only in the TreeNodes to the left
//				returnList.addAll(leftTreeNode.search(query));
//			else if(this.rect.yMax <= query.yMin && rightTreeNode != null) // Search only in the TreeNodes to the right
//				returnList.addAll(rightTreeNode.search(query));
//			else {  // Search in the TreeNodes to the left and the right
//				if(leftTreeNode != null)
//					returnList.addAll(leftTreeNode.search(query));
//				if(rightTreeNode != null)
//					returnList.addAll(rightTreeNode.search(query));
//			}
		}
		return returnList;
	}
	
	private ArrayList<Integer> search(double thisMin, double thisMax, double queryMin, double queryMax, RoadRectangle query)
	{
		// TODO: Create own dynamic array with no generic types!
		ArrayList<Integer> returnList = new ArrayList<Integer>();

		if(thisMin >= queryMax && leftTreeNode != null) // Search only in the TreeNodes to the left
			returnList.addAll(leftTreeNode.search(query));
		else if(thisMax <= queryMin && rightTreeNode != null) // Search only in the TreeNodes to the right
			returnList.addAll(rightTreeNode.search(query));
		else {  // Search in the TreeNodes to the left and the right
			if(leftTreeNode != null)
				returnList.addAll(leftTreeNode.search(query));
			if(rightTreeNode != null)
				returnList.addAll(rightTreeNode.search(query));
		}

		return returnList;
	}

	public boolean isUseX() {
		return useX;
	}

	public boolean isRed() {
		return isRed;
	}

	public TreeNode getLeftTreeNode() {
		return leftTreeNode;
	}

	public TreeNode getRightTreeNode() {
		return rightTreeNode;
	}

	public void setUseX(boolean useX) {
		this.useX = useX;
	}

	public void setRed(boolean red) {
		isRed = red;
	}

	public void setLeftTreeNode(TreeNode leftTreeNode) {
		this.leftTreeNode = leftTreeNode;
	}

	public void setRightTreeNode(TreeNode rightTreeNode) {
		this.rightTreeNode = rightTreeNode;
	}
}