package dk.itu.kf04.g4tw.controller;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

/**
 * Tests the respond-part of the WebServer.
 * Due to the high amount of (n)io it is quite complex to test the server-part of the class.
 * What we <i>can</i> test though is that the server responds as expected.
 *
 * @author Jens Egholm <jegp@itu.dk>
 */
public class WebServerTest {

    /**
     * The respond method to test.
     */
    protected Method respond;
    
    /**
     * Instantiate the server.
     */
    @Before public void setUp() {
        try {
            // Respond method
            Method respond = WebServer.class.getDeclaredMethod("respond", String.class, byte[].class, PrintStream.class);
            respond.setAccessible(true);
            this.respond = respond;
        } catch (NoSuchMethodException e) {
            System.err.println("Could not find the respond method to test in WebServer.");
        }
    }

    /**
     * Test the response.
     */
    @Test public void respondTest() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);

        // "Hello World" in bytes
        String message = "Hello World";
        byte[] is = message.getBytes();

        // Write the response
        try {
            respond.invoke(null, "text/javascript", is, ps);

            // Test
            assertTrue(os.toString().endsWith(message));

        } catch (Exception e) {
            assertTrue("Unable to test the respond method: " + e.getMessage(), false);
        }

        // Clean up
        ps.close();
    }

}
