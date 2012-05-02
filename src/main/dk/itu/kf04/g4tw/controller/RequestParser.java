package dk.itu.kf04.g4tw.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import dk.itu.kf04.g4tw.model.Road;
import dk.itu.kf04.g4tw.util.DynamicArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;

/**
 *
 */
public class RequestParser {
    
    public static InputStream parseToInputStream(String input) throws IllegalArgumentException, UnsupportedEncodingException, TransformerException {
    	// Variables for the request
    	double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
    	int filter = 0;
    	XMLDocumentParser xmlParser = new XMLDocumentParser();

        // Decode the input and split it up
        String[] queries = URLDecoder.decode(input, "UTF-8").split("&");

        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<roadCollection xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                "xsi:noNamespaceSchemaLocation=\"kraX.xsd\">";

        if (queries.length != 5) {
            throw new IllegalArgumentException("Must have exactly 5 queries.");
        }

        // Iterate through the queries
        for(String query : queries) {
            String[] arr = query.split("=");
            
            // Something is wrong if there are not exactly 2 values
            if (arr.length != 2) {
                throw new IllegalArgumentException("Must have format x1=lowValue&y1=lowValue&x2=highValue&y2=highValue&filter=[1-?]");
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

        // Search the model and concatenate the results with the previous
        DynamicArray<Road> search = MapController.model.search(x1, y1, x2, y2, filter);

		// Creates an XML document
		Document docXML = xmlParser.createDocument();
		// Creates a root element for the document, and appends it, making it possible
		// to add more elements to the document.
		Element root = docXML.createElement("root");
		docXML.appendChild(root);
		// Iterates through the search array, appending the XML element of the current
		// road to the root element. This is creating the XML document.
		for(int i = 0; i < search.length(); i++) {root.appendChild(search.get(i).toXML(docXML));}
		// Parsing the java object to clean-text.
		result += xmlParser.createXMLString(docXML);
        result += "</roadCollection>";

		// calculates and prints the time taken.
		long endTime = System.currentTimeMillis()-startTime;
		System.out.println("time taken: " + endTime + "ms");
        System.out.println(search.length());

        try {
            return new ByteArrayInputStream(result.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported encoding: " + e.getMessage());
            return null;
        }
    }
}
