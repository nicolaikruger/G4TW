package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.MapModel;

import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Logger;

/**
 * A WebServer that can respond to incoming HTTP requests.
 * @author Nicolai Krüger <nkrk@itu.dk>
 * @author Jens Egholm <jegp@itu.dk>
 */
public class WebServer implements Closeable {

    /**
     * Number of attempts to start the server. 
     */
    protected int attempts = 0;
    
    /**
     * Tests whether the server already has been started.
     */
    protected static boolean isInitialized = false;

    /**
     * The log for this class.
     */
    protected static Logger Log = Logger.getLogger(WebServer.class.getName());

    /**
     * The model to perform the requests on
     */
    protected MapModel model;
    
    /**
     * The port of the webserver.
     */
    protected int port;

    /**
     * The server thread.
     */
    protected ServerSocket server;

    /**
     * The root directory of the www files.
     */
    protected String webRoot = "www" + System.getProperty("file.separator");

    /**
     * Creates a new webserver with the given model on the given point
     * @param model  The model to perform the searches on.
     * @param port  The port to listen on.
     * @throws IllegalArgumentException If the model is null or the port is out of range
     */
    public WebServer(MapModel model, int port) throws IllegalArgumentException {
        // Check arguments
        if (model == null)
            throw new IllegalArgumentException("Cannot start server with empty model.");

        if (port <= 0 || port > 1 << 16)
            throw new IllegalArgumentException("Cannot set port to " + port + ".");

        // Store model and port
        this.model = model;
        this.port = port;
        
        // Init
        init();
    }

    /**
     * Close the server. 
     */
    public void close() {
        try {
            server.close();      
            Log.info("Server has been shut down.");
        } catch (IOException e) {
            Log.warning("Exception occurred when attempting to shut down server: " + e.getMessage());
        }
    }

    /**
     * Handles a given request and respond appropriate output to the given PrintStream.
     * @param request  The request in string-format
     * @param out  The output stream to output content to
     */
    protected void handleRequest(String request, PrintStream out) {

        // Turn down bad requests
        if (request == null ||
            !request.startsWith("GET") ||
            request.endsWith("HTTP/1.0") || // We do not accept 1.0
            !request.endsWith("HTTP/1.1") ||
            request.charAt(4) != '/') {

            // Print the bad request and return
            out.println("Bad request. The server could not understand your query: " + request);
            Log.info("Received bad request: " + request);
            return;
        }

        // Find the request for a file by omitting "GET /" and "HTTP/1.1"
        String fileRequest = request.substring(5, request.length() - 9);

        // Set the byte-stream and content-type
        byte[] input = null;
        String contentType = null;

        fileRequest = fileRequest.toLowerCase();

        // Match for map requests
        if (fileRequest.startsWith("xml?")) {
            try {
                // Set input stream via
                input = RequestParser.parseQuery(model, fileRequest.substring(4, fileRequest.length()));
            } catch (IllegalArgumentException e) {
                Log.warning("Illegal argument: " + e.getMessage());
            } catch (UnsupportedEncodingException e) {
                Log.warning("Unsupported encoding: " + e.getMessage());
            } catch (TransformerException e) {
                Log.warning("Transformer exception: " + e.getMessage());
            }

            // Set the content type
            if (input != null) {
                contentType = "text/xml";
            }

        // Match for path request
        } else if(fileRequest.startsWith("path?")) {
            try {
                fileRequest = URLDecoder.decode(fileRequest, "UTF-8");
                fileRequest = fileRequest.substring(5);
                input = RequestParser.parsePath(model, fileRequest);
            } catch (UnsupportedEncodingException e) {
                Log.warning("Unsupported encoding: " + e.getMessage());
            } catch (TransformerException e) {
                Log.warning("Transformer exception: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.warning("Malformed request received: " + fileRequest);
            }

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
                input = Files.readAllBytes(Paths.get(fileRequest));
                // Set content-type
                if(fileRequest.endsWith(".js"))			contentType = "text/javascript";
                else if(fileRequest.endsWith(".xml"))	contentType = "text/xml";
                else		                   			contentType = URLConnection.guessContentTypeFromName(fileRequest);
            } catch (IOException e) {
                Log.warning("Exception while handling " + fileRequest + ": " + e.getMessage());
            } catch (InvalidPathException e) {
                Log.warning("Invalid path: "+  fileRequest);
            }
        }

        // Output the response
        if (input != null) {
            respond(contentType, input, out);
        }
    }

    /**
     * Initializes the web-server.
     */
    public void init() {
        // Return if web-server already has been started
        if (isInitialized) {
            Log.warning("A web-server is already running. Cannot start.");
            return;
        }
        
        // Try to initialize a ServerSocket
        try {
            ServerSocket server = new ServerSocket(port);
            isInitialized = true; 
            
            // Log success
            Log.info("Server has been started at port " + port);
            
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
                
                // Handle the incoming request
                handleRequest(request, pout);
                
                // Close down the output and the socket
                s.shutdownOutput();
                s.close();
            }
        // BindException - attempt to restart server
        } catch (BindException e) {
            if (attempts < 5) {
                Log.warning("Unable to bind to local address or port. Retrying...");
                attempts++;
                port++;
                init();
            } else {
                Log.severe("Unable to bind to local address or port after 5 retries.");
                isInitialized = false;
            }
        // SocketException
        } catch (SocketException e) {
            Log.warning("SocketException while starting the server: " + e);
            isInitialized = false;
        // IOException
        } catch (IOException e) {
            Log.warning("IOException while starting the server: " + e);
            isInitialized = false;
        }
    }

    /**
     * Respond the given input-stream to the given output-stream.
     *
     * @param contentType  The type of the content to respond
     * @param is  The input stream of the response
     * @param os  The output stream to the client
     */
    protected static void respond(String contentType, byte[] is, PrintStream os) {
        // Avoid printing null data
        if (is != null) {
            // Print HTTP meta-content
            os.println("HTTP/1.1 200 OK");
            os.println("Server: KraXServer/1.0");
            os.println("Date: " + new Date());
            // Print content-type
            if (contentType != null) {
                os.println("Content-Type: " + contentType);
            }

            // Create a line between meta-content and content
            os.println();

            // Send object
            try {
                os.write(is);
            } catch (IOException e) { // IOException
                Log.warning("IOException while writing to client: " + e.getMessage());
            }
        }

        // Flush the stream - closing it will close the connection!
        os.flush();
    }
}
