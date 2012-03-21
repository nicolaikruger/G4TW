package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.HTTPConstants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLConnection;

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
    private static int port = 8080;
    
    /**
     * Initialize the server and prepare to respond on incoming requests.
     *
     * @return  boolean  A boolean flag indicating success or failure
     */
    public static boolean init() {
        // Return if webserver already has been started
        if (isInitialized == true) return false;

        // Try to initialize a ServerSocket
        try {
            ServerSocket server = new ServerSocket(port);
            isInitialized = true;

            // Loop
            while (true) {
                // Wait for a connection
                Socket s = server.accept();

                // Read request from socket input stream
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String request = in.readLine();
                s.shutdownInput();

                // Split requests
                String fileRequest = request.substring(5, request.length() - 9);

                // Replace "" with www/index.html
                if (fileRequest.equals("")) fileRequest = "www/index.html";

                try {
                    // Load file
                    File file = new File(fileRequest);
                    InputStream fileIn = new FileInputStream(file);

                    // Get output-stream
                    OutputStream out = new BufferedOutputStream(s.getOutputStream());
                    PrintStream pout = new PrintStream(out);

                    // Send object
                    byte[] buffer = new byte[1000];
                    while(fileIn.available() > 0) {
                        out.write(buffer, 0, fileIn.read(buffer));
                    }
                    pout.flush();
                } catch (FileNotFoundException e) {
                    System.out.println("File " + fileRequest + " not found.");
                }
                
                // Close down the socket
                s.shutdownOutput();
                s.close();
            }
        } catch (SocketException e) { // SocketException
            System.out.println("SocketException");
            e.printStackTrace();
            isInitialized = false;
        } catch (IOException e) { // IOException
            System.out.println("IOException: ");
            e.printStackTrace();
        }
        return isInitialized;
    }

    /**
     * Returns the port of the WebServer.
     * @return int  The port the WebServer listens to when initialized.
     */
    public static int getPort() { return port; }
    
    /**
     * Sets the port of the WebServer.
     * @param port  The port between 0 and 0xFFFF
     */
    public static void setPort(int port) {
        WebServer.port = port;
    }

}
