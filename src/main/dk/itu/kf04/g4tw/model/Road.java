package dk.itu.kf04.g4tw.model;
import dk.itu.kf04.g4tw.model.tree.RoadRectangle;

import java.awt.geom.Point2D;

public class Road {
	
	private String name;
	private int type;
	public final Point2D.Double from;
	public final Point2D.Double to;
	private double speed;
	private double length;

	/**
	 * The rectangle enclosing this road.
	 */
	public final RoadRectangle rect;
	
	public Road(String name, Point2D.Double f, Point2D.Double t, int type, double speed, double length) {
		this.name = name;
		this.type = type;
		this.from = f;
		this.to = t;
		this.speed = speed;
		this.length = length;
		
		rect = new RoadRectangle(f, t);
	}
	
	public String toString() {
		return ("Name: "+name + "; From: ("+from.getX()+","+from.getY()+")" + " To: ("+to.getX()+","+to.getY()+")" + "; Type: "+type + "; Speed: "+speed + "; Length: "+length + ";");
	}
	
	public double getLength() {
		return length;
	}
	
	public Point2D.Double getFrom() {
		return from;
	}
	
	public Point2D.Double getTo() {
		return to;
	}

	public int getType() {
		return type;
	}

	/**
	public void assignNodes() {
		getFrom().createRelation(this);
		getTo().createRelation(this);
	}*/
	
	public String toXML()
	{
		String returnString;

		returnString = 	"<r><n>" + name +
						"</n><l>" + length +
						"</l><s>" + speed +
						"</s><fx>" + from.x +
						"</fx><fy>" + from.y +
						"</fy><tx>" + to.x +
						"</tx><ty>" + to.y +
						 "</ty></r>";

		return returnString;
	}
}