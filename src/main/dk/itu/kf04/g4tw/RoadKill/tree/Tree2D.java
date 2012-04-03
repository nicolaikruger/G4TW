package dk.itu.kf04.g4tw.RoadKill.tree;

import java.awt.geom.Point2D;

/**
 * A 2-dimensional binary tree storing roads.
 */
public class Tree2D {

	/**
	 * The root of the tree.
	 */
	private TreeNode root;

	/**
	 * Adds a node to the tree.
	 * @param from  The point the road starts.
	 * @param to  The point the road ends.
	 * @param id  The id of the road.
	 */
	public void addNode(Point2D.Double from, Point2D.Double to, int id)
	{
		RoadRectangle rect = new RoadRectangle(from.x, from.y, to.x, to.y);
		if(root == null) 	root = new TreeNode(rect, id);
		else 				root.addTreeNode(rect, id);
	}

	private TreeNode rotateLeft(TreeNode h)
	{
		TreeNode x = h.getRightTreeNode();
		h.setRightTreeNode(x.getLeftTreeNode());
		x.setLeftTreeNode(h);
		x.setRed(h.isRed());
		h.setRed(true);
		return x;
	}

	private TreeNode rotateRight(TreeNode h)
	{
		TreeNode x = h.getLeftTreeNode();
		h.setLeftTreeNode(x.getRightTreeNode());
		x.setRightTreeNode(h);
		x.setRed(h.isRed());
		h.setRed(true);
		return  x;
	}

	private void flipColors(TreeNode h)
	{
		h.setRed(true);
		h.getLeftTreeNode().setRed(false);
		h.getRightTreeNode().setRed(false);
		h.getLeftTreeNode().setUseX(!h.isUseX());
		h.getRightTreeNode().setUseX(!h.isUseX());
	}

	/**
	 * Search for the roads the intersects with the given coordinates.
	 * @param xMin
	 * @param yMin
	 * @param xMax
	 * @param yMax
	 */
	public void search(double xMin, double yMin, double xMax, double yMax)
	{
		for(Integer n : root.search(new RoadRectangle(xMin, yMin, xMax, yMax)))
			System.out.println(n);
	}

}