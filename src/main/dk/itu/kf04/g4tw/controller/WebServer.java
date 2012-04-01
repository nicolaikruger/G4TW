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
	private static  MapController map = new MapController();
    
    /**
     * Initialize the server and prepare to respond on incoming requests.
     *
     * @return  boolean  A boolean flag indicating success or failure
     */
    public static boolean init() {
        // Return if webserver already has been started
        if (isInitialized) return false;

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
						respond("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><roadCollection xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
								"xsi:noNamespaceSchemaLocation=\"kraX.xsd\">\n" +
								"\t<road>\n" +
								"\t\t\t<name>Kruger</name>\n" +
								"\t\t\t<length>1.25</length>\n" +
								"\t\t\t<speed>130.0</speed>\n" +
								"\t\t\t<fNode id=\"1\">\n" +
								"\t\t\t\t<xCoord>5.0</xCoord>\n" +
								"\t\t\t\t<yCoord>6.8</yCoord>\n" +
								"\t\t\t</fNode>\n" +
								"\t\t\t<tNode id=\"1\">\n" +
								"\t\t\t\t<xCoord>10.1</xCoord>\n" +
								"\t\t\t\t<yCoord>66.6</yCoord>\n" +
								"\t\t\t</tNode>\n" +
								"\t</road>\n" +
								"\t\n" +
								"\t<road>\n" +
								"\t\t\t<name>Kruger2</name>\n" +
								"\t\t\t<length>1.25</length>\n" +
								"\t\t\t<speed>30.0</speed>\n" +
								"\t\t\t<fNode id=\"1\">\n" +
								"\t\t\t\t<xCoord>10.1</xCoord>\n" +
								"\t\t\t\t<yCoord>66.6</yCoord>\n" +
								"\t\t\t</fNode>\n" +
								"\t\t\t<tNode id=\"1\">\n" +
								"\t\t\t\t<xCoord>150</xCoord>\n" +
								"\t\t\t\t<yCoord>150</yCoord>\n" +
								"\t\t\t</tNode>\n" +
								"\t</road>\n" +
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
