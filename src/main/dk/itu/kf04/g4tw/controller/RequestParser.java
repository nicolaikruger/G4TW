package dk.itu.kf04.g4tw.controller;

import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 *
 */
public class RequestParser {
    
    public static InputStream parseToInputStream(String input) throws IllegalArgumentException, UnsupportedEncodingException {
    	// Variables for the request
    	int x1 = 0, x2 = 0, y1 = 0, y2 = 0, filter = 0;
    	
        // Decode the input and split it up
        String[] queries = URLDecoder.decode(input, "UTF-8").split("&");
                
        String result = "";

        if (queries.length != 5) {
            throw new IllegalArgumentException("Must have exactly 5 queries.");
        }

        // Iterate through the queries
        for(String query : queries) {
            String[] arr = query.split("=");
            
            // Something is wrong if there are not exactly 2 values
            if (arr.length != 2) {
                throw new IllegalArgumentException("Must have format xml?x1=lowValue&y1=lowValue&x2=highValue&y2=highValue&filter=[1-?]");
            } else {
            	// Assign input to local variables
                for(int i = 0; i < arr.length; i++) {
					String name = arr[i];
					String value = arr[++i];
					switch(name) {
						case "x1" : x1 = Integer.parseInt(value); break;
						case "x2" : x2 = Integer.parseInt(value); break;
						case "y1" : y1 = Integer.parseInt(value); break;
						case "y2" : y2 = Integer.parseInt(value); break;
						case "filter" : filter = Integer.parseInt(value); break;
						default : throw new IllegalArgumentException("Must have format xml?x1=lowValue&y1=lowValue&x2=highValue&y2=highValue&filter=[1-?]");
					}
				}
            }
        }
        
        // magi!...

        try {
            return new ByteArrayInputStream(result.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported encoding: " + e.getMessage());
            return null;
        }
    }


    String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
  				"<roadCollection xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
  				"xsi:noNamespaceSchemaLocation=\"localhost/kraX.xsd\"" +
  				"xmlns=\"http://www.w3schools.com\">";
    
}
