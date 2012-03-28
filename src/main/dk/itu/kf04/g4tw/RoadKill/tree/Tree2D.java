package dk.itu.kf04.g4tw.RoadKill.tree;

public class Tree2D {

	TreeNode root;

	public Tree2D() 
	{	}
	
	public void addNode(double X, double Y, String name)
	{
		if(root == null) 	root = new TreeNode(true, X, Y, name);
		else 				root.addTreeNode(X, Y, name);
	}
	
	public void search(double X1, double Y1, double X2, double Y2)
	{
		for(TreeNode n : root.search(X1, Y1, X2, Y2))
			System.out.println(n.getName());
	}
	
	
	public static void main(String[] args)
	{
		Tree2D T = new Tree2D();
		
		T.addNode(6,4,"1");
		T.addNode(4,2,"2");
		T.addNode(8,3,"3");
		T.addNode(7,6,"4");
		T.addNode(9,8,"5");
		T.addNode(10,10,"6");
		T.addNode(5,9,"7");
		T.addNode(2,7,"8");
		T.addNode(3,5,"9");
		T.addNode(1,1,"10");
		T.addNode(1,2,"11");
		T.addNode(2,1,"12");
		
		T.search(1,1,2,1);
		
	}
}