package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.model.tree.RoadRectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Point2D;

public class Road extends DijkstraEdge {

	public final String name;
	public final int id;
	public final int type;
	public final Point2D.Double from;
	public final Point2D.Double to;
	public final double speed;
	public final double length;
    public final int startNumber;
    public final int endNumber;
    public final String startLetter;
    public final String endLetter;
    public final int leftPostalCode;
    public final int rightPostalCode;

	/**
	 * The rectangle enclosing this road.
	 */
	public final RoadRectangle rect;
	
	public Road(int id, String name, Point2D.Double f, Point2D.Double t, int type, double speed, double length,
                int startNumber, int endNumber, String startLetter, String endLetter, int leftPostalCode, int rightPostalCode ) {
        this.id   = id;
		this.name = name;
		this.type = type;
		this.from = f;
		this.to = t;
		this.speed = speed;
		this.length = length;
        this.startNumber = startNumber;
        this.endNumber = endNumber;

        if(startLetter != null) this.startLetter = startLetter.replace("'", "");
        else this.startLetter = "";

        if(endLetter != null)  this.endLetter = endLetter.replace("'", "");
        else this.endLetter = "";

        this.leftPostalCode = leftPostalCode;
        this.rightPostalCode = rightPostalCode;

		rect = new RoadRectangle(f, t);
	}

    public int getId()        { return id; }

	public double getLength() { return length; }

    @Override public String toString() {
        return ("Road[Id " + id + " Name '" + name + "' houseNumberStart: " + startNumber + " houseNumberEnd: " + endNumber + " houseLetterStart: " + startLetter + " houseLetterEnd: " + endLetter + "]");
    }

	/**
	 * This method converts a road into an XML element. The XML element can be inserted into a document
	 * making searching and printing faster.
	 *
	 * @param doc The document used to create the elements
	 * @return The road converted to an XML element.
	 */
	public Element toXML(Document doc)
	{
		Element road = doc.createElement("r");

		Element idElement = doc.createElement("i");
		idElement.appendChild(doc.createTextNode("" + id));
		road.appendChild(idElement);

		Element typeElement = doc.createElement("t");
		typeElement.appendChild(doc.createTextNode("" + type));
		road.appendChild(typeElement);

		Element fxElement = doc.createElement("fx");
		fxElement.appendChild(doc.createTextNode("" + from.x));
		road.appendChild(fxElement);

		Element fyElement = doc.createElement("fy");
		fyElement.appendChild(doc.createTextNode("" + from.y));
		road.appendChild(fyElement);

		Element txElement = doc.createElement("tx");
		txElement.appendChild(doc.createTextNode("" + to.x));
		road.appendChild(txElement);

		Element tyElement = doc.createElement("ty");
		tyElement.appendChild(doc.createTextNode("" + to.y));
		road.appendChild(tyElement);

		return road;
	}

    public Element toErrorXML(Document doc) {
        Element road = doc.createElement("r");

        Element startNum = doc.createElement("sn");
        startNum.appendChild(doc.createTextNode("" + startNumber));
        road.appendChild(startNum);

        Element endNum = doc.createElement("en");
        endNum.appendChild(doc.createTextNode("" + endNumber));
        road.appendChild(endNum);

        Element sLetter = doc.createElement("sl");
        sLetter.appendChild(doc.createTextNode(startLetter));
        road.appendChild(sLetter);

        Element eLetter = doc.createElement("el");
        eLetter.appendChild(doc.createTextNode(endLetter));
        road.appendChild(eLetter);

        Element lpc = doc.createElement("lpc"); //Left postal code
        lpc.appendChild(doc.createTextNode("" + leftPostalCode));
        road.appendChild(lpc);

        Element rpc = doc.createElement("rpc");
        rpc.appendChild(doc.createTextNode("" + rightPostalCode));
        road.appendChild(rpc);

        return road;
    }
}