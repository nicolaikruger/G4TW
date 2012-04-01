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
		if(root == null) 	root = new TreeNode(true, rect, id);
		else 				root.addTreeNode(rect, id);
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
		int i = 0;
		for(Integer n : root.search(new RoadRectangle(xMin, yMin, xMax, yMax)))
		//	System.out.println(n);
			i++;
		System.out.println("Total number of roads: " + i);
	}

}