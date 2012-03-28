package dk.itu.kf04.g4tw.RoadKill.tree;

import java.util.ArrayList;

public class TreeNode {

	private boolean useX;
	private TreeNode leftTreeNode = null;
	private TreeNode rightTreeNode = null;
	
	private double X;
	private double Y;
	
	private int childs = 0;
	private String name;
	
	public TreeNode (boolean useX, double X, double Y, String name)
	{
		this.useX = useX;
		this.X = X;
		this.Y = Y;
		this.name = name;
	}
	
	public void addTreeNode(double X, double Y, String name)
	{
		childs++;
		if(useX) {
			if(this.X < X) {
				if(rightTreeNode == null)	rightTreeNode = new TreeNode(false, X, Y, name);
				else					rightTreeNode.addTreeNode(X, Y, name);
			} else {
				if(leftTreeNode == null)	leftTreeNode = new TreeNode(false, X, Y, name);
				else					leftTreeNode.addTreeNode(X, Y, name);	
			}
		} else {
			if(this.Y < Y) {
				if(rightTreeNode == null)	rightTreeNode = new TreeNode(true, X, Y, name);
				else					rightTreeNode.addTreeNode(X, Y, name);	
			} else {
				if(leftTreeNode == null)	leftTreeNode = new TreeNode(true, X, Y, name);
				else					leftTreeNode.addTreeNode(X, Y, name);	
			}
		}
	}
	
	/**
	/*	X1 and Y1 are the coordiante for the down left corner
	/*  X2 and Y2 are the coordiante for the upper right corner.
	**/
	public ArrayList<TreeNode> search(double X1, double Y1, double X2, double Y2)
	{
		ArrayList<TreeNode> returnList = new ArrayList<TreeNode>();
		if(this.X <= X2 && this.X >= X1 && this.Y <= Y2 && this.Y >= Y1)	
			returnList.add(this);
		
		if(useX) {
			if(this.X >= X2 && leftTreeNode != null) // Search only in the TreeNodes to the left
				returnList.addAll(leftTreeNode.search(X1, Y1, X2, Y2));
			else if(this.X <= X1 && rightTreeNode != null) // Search only in the TreeNodes to the right
				returnList.addAll(rightTreeNode.search(X1, Y1, X2, Y2));
			else {  // Search in the TreeNodes to the left and the right
				if(leftTreeNode != null)
					returnList.addAll(leftTreeNode.search(X1, Y1, X2, Y2));
				if(rightTreeNode != null)
					returnList.addAll(rightTreeNode.search(X1, Y1, X2, Y2));
			}
		} else {
			if(this.Y >= Y2 && leftTreeNode != null) // Search only in the TreeNodes to the left
				returnList.addAll(leftTreeNode.search(X1, Y1, X2, Y2));
			else if(this.Y <= Y1 && rightTreeNode != null) // Search only in the TreeNodes to the right
				returnList.addAll(rightTreeNode.search(X1, Y1, X2, Y2));
			else {  // Search in the TreeNodes to the left and the right
				if(leftTreeNode != null)
					returnList.addAll(leftTreeNode.search(X1, Y1, X2, Y2));
				if(rightTreeNode != null)
					returnList.addAll(rightTreeNode.search(X1, Y1, X2, Y2));
			}
		}
		return returnList;
	}
	
	public int getChilds()
	{
		return childs;
	}
	
	public TreeNode getLeftTreeNode()
	{
		return leftTreeNode;
	}
	
	public TreeNode getRightTreeNode()
	{
		return rightTreeNode;
	}
	
	public String getName()
	{
		return name;
	}
}