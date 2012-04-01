package dk.itu.kf04.g4tw.model.map.tree;

import dk.itu.kf04.g4tw.model.map.Road;

import java.awt.geom.Point2D;
import java.util.ArrayList;

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
	 * @param road The road that belongs to the node
	 */
	public void addNode(Road road)
	{
		if(root == null) 	root = new TreeNode(true, road);
		else 				root.addTreeNode(road);
	}

	/**
	 * Search for the roads the intersects with the given coordinates.
	 * @param xMin
	 * @param yMin
	 * @param xMax
	 * @param yMax
	 */
	public String search(double xMin, double yMin, double xMax, double yMax)
	{
		ArrayList<String> resultList = root.search(new RoadRectangle(xMin, yMin, xMax, yMax));

		String resultString = "";
		for(String s : resultList)
		{
			resultString += s;
		}
		return resultString;
	}

}