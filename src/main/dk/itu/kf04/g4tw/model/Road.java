package dk.itu.kf04.g4tw.model;
import dk.itu.kf04.g4tw.model.tree.RoadRectangle;
import dk.itu.kf04.g4tw.util.DynamicArray;

import java.awt.geom.Point2D;

public class Road extends DijkstraEdge{

    public final Point2D.Double from;
    public final int id;
    private double length;
    public final String name;
    public final double speed;
    public final Point2D.Double to;
    public final int type;

	/**
	 * The rectangle enclosing this road.
	 */
	public final RoadRectangle rect;
	
	public Road(int id, String name, Point2D.Double f, Point2D.Double t, int type, double speed, double length) {
        this.id   = id;
		this.name = name;
		this.type = type;
		this.from = f;
		this.to = t;
		this.speed = speed;
		this.length = length;
		
		rect = new RoadRectangle(f, t);
	}
	
	public String toString() {
		return ("Name: "+name);
	}
	
	public double getLength() {
		return length;
	}

    public int getId() {
        return id;
    }

    /**
	public void assignNodes() {
		getFrom().createRelation(this);
		getTo().createRelation(this);
	}*/
	
	public String toXML()
	{
		String returnString;

		returnString = 	"<r>"+//<n>" + name +
						//"</n><l>" + length +
						//"</l>" +
                        "<i>" + id +
                        "</i><t>" + type +
                        "</t><fx>" + from.x +
						"</fx><fy>" + from.y +
						"</fy><tx>" + to.x +
						"</tx><ty>" + to.y +
						 "</ty></r>";

		return returnString;
	}
}