package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.HTTPConstants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLConnection;
import java.util.Date;

/**
 *
 */
public class WebServer implements HTTPConstants {

    /**
     * Tests whether the server already has been started.
     */
    private static boolean isInitialized = false;
    
    /**
     * The port of the webserver.
     */
    private static int port = 80;

    /**
     * The root directory of the www files.
     */
    private static String webRoot = "www/";

	/**
	 * Retrieves data from the Kraks data-set (XML).
	 */
	private static MapController map;
    
    /**
     * Initialize the server and prepare to respond on incoming requests.
     *
     * @return  boolean  A boolean flag indicating success or failure
     */
    public static boolean init() {
        // Return if webserver already has been started
        if (isInitialized) return false;

		map = new MapController();
		System.out.println(map.getXML(0,0,9999999, 9999999, 64, 128));
        // Try to initialize a ServerSocket
        try {
            ServerSocket server = new ServerSocket(port);
            isInitialized = true;

            // Loop
            while(true) {
                // Wait for a connection
                Socket s = server.accept();

                // Read request from socket input stream
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String request = in.readLine();
                // Ignore remaining input
                s.shutdownInput();

                // Get output-streams
                OutputStream out = new BufferedOutputStream(s.getOutputStream());
                PrintStream pout = new PrintStream(out);

                // Turn down bad requests
                if (request == null ||
                    !request.startsWith("GET") ||
                    request.endsWith("HTTP/1.0") || // We do not accept 1.0
                    !request.endsWith("HTTP/1.1") ||
                    request.charAt(4) != '/') {
                    pout.println("Bad request. The server could not understand your query: " + request);
                    break;
                }

                // Find the request for a file by omitting "GET /" and "HTTP/1.1"
                String fileRequest = request.substring(5, request.length() - 9);

                // Set the input stream and content-type
                InputStream input = null;
                String contentType = null;

                // Match for map requests
                if (fileRequest.startsWith("xml?")) {
                    try {
                        // Set input stream via
                        input = RequestParser.parseToInputStream(fileRequest.substring(4, fileRequest.length()));

						//String responsText =
						respond("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
								"\n" +
								"<!--\n" +
								"\t//////////////////////////////////////\n" +
								"\t//\tkraX\t\t\t\t\t\t   \t//\n" +
								"\t//\tCreated by G4tw on 2012-02-29. \t//\n" +
								"\t//////////////////////////////////////\n" +
								"-->\n" +
								"<roadCollection xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
								"\txsi:noNamespaceSchemaLocation=\"http://online-sporstudstyr.dk/kraX.xsd\" xmlns=\"http://www.w3schools.com\">\n" +
								"\t<r>\n" +
								"\t\t<n>Kruger</n>\n" +
								"\t\t<l>1.25</l>\n" +
								"\t\t<s>130.0</s>\n" +
								"\t\t<fx>5.0</fx>\n" +
								"\t\t<fy>6.8</fy>\n" +
								"\t\t<tx>10.1</tx>\n" +
								"\t\t<ty>66.6</ty>\n" +
								"\n" +
								"\t\t<n>Kruger2</n>\n" +
								"\t\t<l>1.25</l>\n" +
								"\t\t<s>30.0</s>\n" +
								"\t\t<fx>10.1</fx>\n" +
								"\t\t<fy>66.6</fy>\n" +
								"\t\t<tx>150</tx>\n" +
								"\t\t<ty>150</ty>\n" +
								"\t</r>\n" +
								"\n" +
								"</roadCollection>", input, pout);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Illegal argument: " + e.getMessage());
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("Unsupported encoding: " + e.getMessage());
                    }

                    // Set the content type
                    if (input != null) {
                        contentType = "text/xml";
                    }

                // Process request as normal
                } else {
                    // Include the webRoot and point the request
                    // to index.html if it has no target
                    fileRequest = webRoot + (fileRequest.equals("") ? "index.html" : fileRequest);

                    try {
                        // Load file
                        input = new FileInputStream(new File(fileRequest));
                        // Set content-type
                        contentType = URLConnection.guessContentTypeFromName(fileRequest);
                    } catch (FileNotFoundException e) {
                        System.out.println("File " + fileRequest + " not found.");
                    }
                }

                // Output the response
                if (input != null) {
                    respond(contentType, input, pout);
                }
                
                // Close down the output and the socket
                s.shutdownOutput();
                s.close();
            }
        // SocketException
        } catch (SocketException e) {
            System.out.println("SocketException");
            e.printStackTrace();
            isInitialized = false;
        // IOException
        } catch (IOException e) {
            System.out.println("IOException: ");
            e.printStackTrace();
        }

        // Return success or failure
        return isInitialized;
    }

    /**
     * Returns the port of the WebServer.
     * @return int  The port the WebServer listens to when initialized.
     */
    public static int getPort() { return port; }

    /**
     * Respond the given input-stream to the given output-stream.
     */
    private static void respond(String contentType, InputStream is, PrintStream os) {
        // Print HTTP status code
        os.println("HTTP/1.1 200 OK");
        os.println("Server: KraXServer/1.0");
        os.println("Date: " + new Date());
        //os.println(); // Create line between meta code and content
        if (contentType != null) {
            os.println("Content-Type: " + contentType);
        }
		os.println();
        // Send object
        try {
            byte[] buffer = new byte[1000];
            while(is.available() > 0) {
                os.write(buffer, 0, is.read(buffer));
            }
        } catch (IOException e) { // IOException
            System.out.println("IOException: ");
            e.printStackTrace();
        }

        // Flush the stream
        os.flush();
    }
    
    /**
     * Sets the port of the WebServer.
     * @param port  The port between 0 and 0xFFFF
     */
    public static void setPort(int port) {
        WebServer.port = port;
    }

}
