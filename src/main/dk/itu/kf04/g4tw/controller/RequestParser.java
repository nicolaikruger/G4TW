package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.*;
import dk.itu.kf04.g4tw.util.DynamicArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;

/**
 * Handles request for the web-server via the paseToInputStream method.
 * @author Nicolai Krüger <nkrk@itu.dk>
 * @author Jens Egholm <jegp@itu.dk>
 */
public class RequestParser {

    /**
     * The log of the RequestParser.
     */
    public static Logger Log = Logger.getLogger(RequestParser.class.getName());
    
    protected static String schemaURL  = "http://www.w3.org/2001/XMLSchema-instance";
    protected static String schemaFile = "kraX.xsd";

    /**
     * <p>Handles input from the server through the input parameter, decodes it and returns an appropriate
     * message as an array of bytes, ready to dispatch to the sender.</p>
     *
     * <p>The input must be sent with variables set like so <code>name=value</code> separated with a
     * <code>&</code>. To be understood correctly the method must be given four variables:
     * <code>x1, x2, y1, y2 and filter</code>. <br/>
     * <b>Example:</b> <code>x1=12.4&x2=14.97&y1=0.0&y2=102.5&filter=2</code>.
     * </p>
     *
     * @param model  The model to perform searches on
     * @param input  The input string received from the client
     * @return  A series of bytes as a response
     * @throws IllegalArgumentException  If the input is malformed
     * @throws UnsupportedEncodingException If the input cannot be understood under utf-8 encoding
     * @throws TransformerException  If we fail to transform the xml-document to actual output
     */
    public static byte[] parseQuery(MapModel model, String input)
            throws IllegalArgumentException, UnsupportedEncodingException, TransformerException {
    	// Examine null values
        if (model == null) throw new IllegalArgumentException("Model cannot be null.");
        if (input == null || input.equals("")) throw new IllegalArgumentException("Input string may not be " + input + ".");

    	// Variables for the request
    	double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
    	int filter = 0;

        // Decode the input and split it up
        String[] queries = URLDecoder.decode(input, "UTF-8").split("&");

        if (queries.length != 5) {
            throw new IllegalArgumentException("Must have exactly 5 queries.");
        }

        // Iterate through the queries
        for(String query : queries) {
            String[] arr = query.split("=");
            
            // Something is wrong if there are not exactly 2 values
            if (arr.length != 2) {
                throw new IllegalArgumentException("Must have format x1=lowValue&y1=lowValue&x2=highValue&y2=highValue&filter=[1-128]");
            } else {
            	// Assign input to local variables
                for(int i = 0; i < arr.length; i++) {
					String name = arr[i];
					String value = arr[++i];
					if(name.equals("x1")) x1 = Double.parseDouble(value);
					if(name.equals("x2")) x2 = Double.parseDouble(value);
					if(name.equals("y1")) y1 = Double.parseDouble(value);
					if(name.equals("y2")) y2 = Double.parseDouble(value);
					if(name.equals("filter")) filter = Integer.parseInt(value);
				}
            }
        }

		// Time when the search starts
        long startTime = System.currentTimeMillis();

        // Instantiate the builder
        try {
            XMLBuilder builder = new XMLBuilder();

            // Search the model and concatenate the results with the previous
            DynamicArray<Road> search = model.search(x1, y1, x2, y2, filter);

            // Creates an XML document
            Document docXML = builder.createDocument();

            // Creates a roadCollection element inside the root and add namespaces
            Element roads = insertRoadCollection(docXML);

            // Iterates through the search array, appending the XML element of the current
            // road to the roadCollection element. This is creating the XML document.
            for(int i = 0; i < search.length(); i++) {
                roads.appendChild(search.get(i).toXML(docXML));
            }

            // Get the result as a byte-array
            byte[] result = documentToByteArray(docXML, builder.getTransformer());

            // calculates and prints the time taken.
            long endTime = System.currentTimeMillis() - startTime;
            Log.fine("Found and wrote " + search.length() + " roads in : " + endTime + "ms");

            // Return the result-stream as a byte-array
            return result;
        } catch (ParserConfigurationException e) {
            Log.severe("Could not instantiate XML parser.");
            return null;
        }
    }

    /**
     * <p>Handles input from the server through the input parameter and returns an appropriate
     * message as an array of bytes, ready to dispatch to the sender.</p>
     *
     * <p>To parse a path we need a specific input string with variables <code>adr1 and adr2</code> OR
     * <code>adr1, adr2, id1 and id2</code> set. This accounts for two situations: One where both the
     * start and destination is known and one where the two given road-names have multiple hits, where
     * we need to distinguish them by ids.<br />
     * <b>Example:</b> adr1=Strøget 65 A&adr2=Åboulevarden 62
     * <b>Example:</b> adr1=Strøget 65 A&adr2=Åboulevarden 62&id1=1323&id2=71539
     * </p>
     *
     * @param model  The model to perform the search on
     * @param input  The input string received from the client
     * @return  A series of bytes as a response
     * @throws IllegalArgumentException  If the input is malformed
     * @throws TransformerException  If we fail to transform the xml-document to actual output
     */
    public static byte[] parsePath(MapModel model, String input)
            throws IllegalArgumentException, TransformerException {
    	// Examine null values
        if (model == null) throw new IllegalArgumentException("Model cannot be null.");
        if (input == null || input.equals("")) throw new IllegalArgumentException("Input string may not be " + input + ".");

        String[] inputs = input.split("&");

        // if there ain't exactly 2 arguments in the request, throw an error!
        if(!(inputs.length == 2 || inputs.length == 4))
            throw new IllegalArgumentException("Must have the format \"adr1=first address&adr2=second address\" OR \"adr1=first address&adr2=second address&id1=Xid2=Y\"");

        // The two addresses from the client
        String adr1 = inputs[0].substring(5);
        String adr2 = inputs[1].substring(5);

        // Array over all the roads that match the address.
        DynamicArray<Road> hits1 = AddressParser.getRoad(adr1);
        DynamicArray<Road> hits2 = AddressParser.getRoad(adr2);

        // Use ID's if they are defined
        if(inputs.length == 4) {
            int id1 = Integer.parseInt(inputs[2].substring(4));
            int id2 = Integer.parseInt(inputs[3].substring(4));

			// If the fromRoad has been specified by an ID, use that road
            if(hits1.length() > 1) {
				for (int i = 0; i < hits1.length(); i++)
					if (hits1.get(i).getId() == id1) {
						Road hit = hits1.get(i);
						hits1 = new DynamicArray<Road>();
						hits1.add(hit);
						break;
					}
            }

			// If the toRoad has been specified by an ID, use that road
            if(hits2.length() > 1) {
				for (int i = 0; i < hits2.length(); i++)
					if (hits2.get(i).getId() == id2) {
						Road hit = hits2.get(i);
						hits2 = new DynamicArray<Road>();
						hits2.add(hit);
						break;
					}
            }
        }

        // Instantiate the builder
        try {
            XMLBuilder builder = new XMLBuilder();

            // Creates an XML document
            Document docXML = builder.createDocument();

            // Creates a roadCollection element inside the root.
            Element roads;

            // One or both of the addresses gave zero hits. User have to give a new address.
            if(hits1.length() == 0 || hits2.length() == 0) {
                roads = docXML.createElement("error");
                roads.setAttribute("type", "1");
                docXML.appendChild(roads);

                if(hits1.length() == 0) {
                    Element element = docXML.createElement("address");
                    element.appendChild(docXML.createTextNode(adr1));
                    roads.appendChild(element);
                    Log.info("Could not find \"" + adr1 + "\" in the system");
                }

                if(hits2.length() == 0) {
                    Element element = docXML.createElement("address");
                    element.appendChild(docXML.createTextNode(adr2));
                    roads.appendChild(element);
                    Log.info("Could not find \"" + adr2 + "\" in the system");
                }

            // The addresses both gave only one hit. We can find a path.
            } else if (hits1.length() == 1 && hits2.length() == 1) {
                Log.fine("Trying to find path");
                Road[] result = model.shortestPath(hits1.get(0), hits2.get(0));

                // Initialize the roadCollection element and add namespaces
                roads = insertRoadCollection(docXML);

                // Iterates through the result array, appending the XML element of the current
                // road to the roadCollection element. This is creating the XML document.
                int prev = hits2.get(0).getId();
                roads.appendChild(hits2.get(0).toXML(docXML));
                while(result[prev] != null) {
                    roads.appendChild(result[prev].toXML(docXML));
                    prev = result[prev].getId();
                }

            // One or both of the addresses gave more than one hit. Make the user decide.
            } else {
                roads = docXML.createElement("error");
                roads.setAttribute("type", "2");
                docXML.appendChild(roads);

                Element collection = docXML.createElement("collection");
                roads.appendChild(collection);

                for (int i = 0; i < hits1.length(); i++)
                {
                    collection.appendChild(hits1.get(i).toErrorXML(docXML));
                }

                Element collection2 = docXML.createElement("collection");
                roads.appendChild(collection2);

                for (int i = 0; i < hits2.length(); i++)
                {
                    collection2.appendChild(hits2.get(i).toErrorXML(docXML));
                }
            }

            // Return the result as a byte-array
            return documentToByteArray(docXML, builder.getTransformer());
        } catch (ParserConfigurationException e) {
            Log.severe("Could not instantiate XML parser.");
            return null;
        }
    }

    /**
     * Appends a universal road-collection to the XML document containing meta-information for our XML. 
     * @param doc  The document in which to append the collection.
     * @return An Element in which roads can be inserted.             
     */
    protected static Element insertRoadCollection(Document doc) {
        Element roads = doc.createElement("roadCollection");
        roads.setAttribute("xmlns:xsi", schemaURL);
        roads.setAttribute("xsi:noNamespaceSchemaLocation", schemaFile);
        doc.appendChild(roads);
        return roads;
    }

    /**
     * Converts a document to an array of bytes.
     * @param doc  The document to transform.
     * @param transformer  The transformer used to convert the document to a result stream.
     * @return An array of bytes containing the document.
     * @throws TransformerException  If the transformation fails.
     */
    protected static byte[] documentToByteArray(Document doc, Transformer transformer) throws TransformerException{
        // Create the source
        Source source = new DOMSource(doc);

        // Instantiate output-sources
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Result result            = new StreamResult(os);

        // Transform the xml
        transformer.transform(source, result);

        // Return the result-stream as a byte-array
        return os.toByteArray();
    }
}
