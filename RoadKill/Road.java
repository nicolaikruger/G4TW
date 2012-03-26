import java.awt.geom.Point2D;

public class Road {
	
	private String name;
	private int type;
	private Point2D.Double from;
	private Point2D.Double to;
	private double speed;
	private double length;
	
	public Road(String name, Point2D.Double f, Point2D.Double t, int type, double speed, double length) {
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
}