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
     * Initialize the server
     */
    public static boolean init() {
        if (isInitialized == true) return false;

        try {
            ServerSocket server = new ServerSocket(port);
            isInitialized = true;

            while (server.isBound()) {
                Socket s = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String contenttype = URLConnection.guessContentTypeFromName("C:/workspace/G4TW/index.html");
                File file = new File("www/index.html");
                InputStream fileIn = new FileInputStream(file);
                OutputStream out = new BufferedOutputStream(s.getOutputStream());
                PrintStream pout = new PrintStream(out);

                String request = in.readLine();
                System.out.println("Request: " + request);
                s.shutdownInput();

                byte[] buffer = new byte[1000];
                while(fileIn.available() > 0) {
                    out.write(buffer, 0, fileIn.read(buffer));
                }
                pout.flush();
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

}
