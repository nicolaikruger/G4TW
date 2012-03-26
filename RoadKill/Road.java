import java.awt.geom.Point2D;

public class Road {
	
	private String name;
	private int type;
	private Node from;
	private Node to;
	private double speed;
	private double length;
	
	public Road(String name, Node f, Node t, int type, double speed, double length) {
		this.name = name;
		this.type = type;
		this.from = f;
		this.to = t;
		this.speed = speed;
		this.length = length;
	}
	
	public String toString() {
		return ("Name: "+name + "; From: ("+from.getX()+","+from.getY()+")" + " To: ("+to.getX()+","+to.getY()+")" + "; Type: "+type + "; Speed: "+speed + "; Length: "+length + ";");
	}
	
	public double getLength() {
		return length;
	}
	
	public Node getFrom() {
		return from;
	}
	
	public Node getTo() {
		return to;
	}
	
	public void assignNodes() {
		getFrom().createRelation(this);
		getTo().createRelation(this);
	}
}