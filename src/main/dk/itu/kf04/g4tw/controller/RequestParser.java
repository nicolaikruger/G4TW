package dk.itu.kf04.g4tw.controller;

import java.io.*;

/**
 *
 */
public class RequestParser {
    
    public static InputStream parseToInputStream(String input) {
        String result = "<html>hej</html>";
        // magi!...

        try {
            return new ByteArrayInputStream(result.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported encoding: " + e.getMessage());
            return null;
        }
    }
    
}
