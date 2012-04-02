package dk.itu.kf04.g4tw.controller;

import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 *
 */
public class RequestParser {
    
    public static InputStream parseToInputStream(String input) throws IllegalArgumentException, UnsupportedEncodingException {
        // Decode the input and split it up
        String[] queries = URLDecoder.decode(input, "UTF-8").split("&");
        String result = "";

        if (queries.length == 0) {
            throw new IllegalArgumentException("Must have more than zero queries.");
        }

        // Iterate through the queries
        for(String query : queries) {
            String[] arr = query.split("=");

            // Something is wrong if there are not exactly 2 values
            if (arr.length != 2) {
                throw new IllegalArgumentException("Must have format name1=value1&name2=value2");
            } else {
                String name = arr[0];
                String value = arr[1];

                if (name.equals("address")) {
                    result = Arrays.toString(AddressParser.parseAddress(value));
                } else if (name.equals("x")) {
                    //  ...
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
