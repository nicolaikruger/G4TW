package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.model.tree.RoadRectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Point2D;

/**
 * <p>A Road is basically a connection between two points in a two-dimensional space.
 * Besides the two points the road also contains an id, name and various other information
 * relevant for a map.
 * </p>
 * <p>
 * One of the most important aspects of a Road is its <code>rect</code>-field. This field describes
 * the minimum bounding rectangle (see {@link RoadRectangle}) which is used to examine intersections with
 * the road.
 * </p>
 * <p>
 * Roads also functions as edges in a graph understood as a connection between two points.
 * This makes the road a somewhat strange hybrid of a node and edge, albeit efficient for graph-problems.
 * </p>
 */
public class Road extends DijkstraEdge {

    /**
     * The name of the road
     */
	public final String name;

    /**
     * The ID of the road.
     */
	public final int id;

    /**
     * The type of the road, as seen in the {@link MapModel}.
     */
	public final int type;

    /**
     * The first end-point of the road.
     */
	public final Point2D.Double from;

    /**
     * The last end-point of the road.
     */
	public final Point2D.Double to;

    /**
     * The speed limitation of the road.
     */
	public final double speed;

    /**
     * The length of the road.
     */
	public final double length;

    /**
     * The starting numbers for the houses on this road.
     */
    public final int startNumber;

    /**
     * The last number for the houses on this road.
     */
    public final int endNumber;

    /**
     * The first letter for houses on this road.
     */
    public final String startLetter;

    /**
     * The last letter for houses on this road.
     */
    public final String endLetter;

    /**
     * The postal code for the left side of the road.
     */
    public final int leftPostalCode;

    /**
     * The postal code for the right side of the road.
     */
    public final int rightPostalCode;

	/**
	 * The rectangle enclosing this road.
	 */
	public final RoadRectangle rect;

    /**
     * Instantiates a new road represented as the connection between the two given points f and t.
     * @param id                The unique identifier of the road.
     * @param name              The name of the road
     * @param f                 The first end-point of the road.
     * @param t                 The other end-point of the road.
     * @param type              The road-type of the road corresponding to the ones given in {@link MapModel}.
     * @param speed             The speed with which you can drive on the road.
     * @param length            The length of the road.
     * @param startNumber       The first house-number on the road.
     * @param endNumber         The last house-number on the road.
     * @param startLetter       The first letter on the road.
     * @param endLetter         The last letter on the road.
     * @param leftPostalCode    The postal code for the left side of the road.
     * @param rightPostalCode   The postal code for the right side of the road.
     * @throws IllegalArgumentException If an error occurred during construction.
     */
	public Road(int id, String name, Point2D.Double f, Point2D.Double t, int type, double speed, double length,
                int startNumber, int endNumber, String startLetter, String endLetter, int leftPostalCode, int rightPostalCode )
            throws IllegalArgumentException {
        this.id   = id;

		if(name != null) this.name = name;
		else this.name = "";

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

    /**
     * Finds the unique id of the road segment.
     * @return The id as an int.
     */
    public int getId()        { return id; }

    /**
     * Finds the length of the road segment.
     * @return The length of the road in meters.
     */
	public double getLength() { return length; }

    @Override
    public String toString() {
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
		idElement.appendChild(doc.createTextNode(String.valueOf(id)));
		road.appendChild(idElement);

		Element typeElement = doc.createElement("t");
		typeElement.appendChild(doc.createTextNode(String.valueOf(type)));
		road.appendChild(typeElement);

		Element fxElement = doc.createElement("fx");
		fxElement.appendChild(doc.createTextNode(String.valueOf(from.x)));
		road.appendChild(fxElement);

		Element fyElement = doc.createElement("fy");
		fyElement.appendChild(doc.createTextNode(String.valueOf(from.y)));
		road.appendChild(fyElement);

		Element txElement = doc.createElement("tx");
		txElement.appendChild(doc.createTextNode(String.valueOf(to.x)));
		road.appendChild(txElement);

		Element tyElement = doc.createElement("ty");
		tyElement.appendChild(doc.createTextNode(String.valueOf(to.y)));
		road.appendChild(tyElement);

		return road;
	}

    /**
     * Converts a road into an XML element which contains additional information so the user
     * can handle collisions from the search-string given to the server. 
     * <br />
     * Like the <code>toXML</code> the resulting element can be inserted into a document making 
     * searching and printing faster.
     *
     * @param doc The document used to create the elements
     * @return The road converted to an XML element.
     */
    public Element toErrorXML(Document doc) {
        Element road = doc.createElement("r");

		Element ID = doc.createElement("id");
		ID.appendChild(doc.createTextNode(String.valueOf(id)));
		road.appendChild(ID);

		Element name = doc.createElement("n");
		name.appendChild(doc.createTextNode(String.valueOf(this.name)));
		road.appendChild(name);

		Element startNum = doc.createElement("sn");
        startNum.appendChild(doc.createTextNode(String.valueOf(startNumber)));
        road.appendChild(startNum);

        Element endNum = doc.createElement("en");
        endNum.appendChild(doc.createTextNode(String.valueOf(endNumber)));
        road.appendChild(endNum);

        Element sLetter = doc.createElement("sl");
        sLetter.appendChild(doc.createTextNode(" " + startLetter)); // Prepend space to avoid empty element
        road.appendChild(sLetter);

        Element eLetter = doc.createElement("el");
        eLetter.appendChild(doc.createTextNode(" " + endLetter)); // Prepend space to avoid empty element
        road.appendChild(eLetter);

        Element lpc = doc.createElement("lpc"); //Left postal code
        lpc.appendChild(doc.createTextNode(String.valueOf(leftPostalCode)));
        road.appendChild(lpc);

        Element rpc = doc.createElement("rpc"); // right postal code
        rpc.appendChild(doc.createTextNode(String.valueOf(rightPostalCode)));
        road.appendChild(rpc);

        return road;
    }
}