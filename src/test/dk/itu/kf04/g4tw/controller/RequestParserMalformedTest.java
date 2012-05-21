package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Road;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.net.URLEncoder;

/**
 * Tests the request parser.
 */
public class RequestParserMalformedTest {

    protected MapModel emptyModel = new MapModel();
    protected MapModel model;

    /**
     * Setup a model.
     */
    @Before public void setUp() {
        MapModel model = new MapModel();
        Road r = new Road(12, "A Road", new Point2D.Double(0, 0), new Point2D.Double(-102, 311), MapModel.MINOR_ROAD, 80, 213, 1, 11, "A", "C", 6830, 6830);
        model.addRoad(r);
    }

    ///////////////////////////// PARSE QUERY ////////////////////////////////////

    /**
     * Test a null model.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryNullModelTest() throws Exception {
        RequestParser.parseQuery(null, "");
    }

    /**
     * Test an empty request.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class) 
    public void queryEmptyTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "");
    }

    /**
     * Test a null request.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryNullTest() throws Exception {
        RequestParser.parseQuery(emptyModel, null);
    }

    /**
     * Test a malformed request - too few parameters.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryMalformedParameterTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "x1=1&y1=2&x1=3&filter=1");
    }

    /**
     * Test a malformed request - letters.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryMalformedLetterTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "x1=A&x2=2&y1=C&y2=4&filter=X");
    }
    
    /**
     * Test a malformed request - wrong order.
     * @throws Exception If something unexpected happens.
     */
    @Test public void queryMalformedOrderTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "y2=1.2&x2=2&x1=2&x2=4&filter=3");
    }
    
    /**
     * Test a malformed request - wrong variable names.
     * @throws Exception If something unexpected happens.
     */
    @Test public void queryMalformedNameTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "y3=1.2&x2=2&x1=2&x2=4&filter=16");
    }

    /**
     * Test a malformed request - wrong filter value.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryMalformedFilterValueTest1() throws Exception {
        RequestParser.parseQuery(emptyModel, "y3=1.2&x2=2&x1=2&x2=4&filter=3000");
    }

    /**
     * Test a malformed request - wrong filter value.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryMalformedFilterValueTest2() throws Exception {
        RequestParser.parseQuery(emptyModel, "y3=1.2&x2=2&x1=2&x2=4&filter=0");
    }
    
    /**
     * Test a malformed request - wrong decoding.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryMalformedDecodingTest() throws Exception {
        String input = "y3=1.2&x2=2&x1=2&x2=4&filter=0";
        String encoded = URLEncoder.encode(input, "Cp1252"); // Windows Latin-1
        RequestParser.parseQuery(emptyModel, encoded);
    }

    ///////////////////////////// PARSE PATH ////////////////////////////////////

    /**
     * Test a null model.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void pathNullModelTest() throws Exception {
        RequestParser.parsePath(null, "");
    }

    /**
     * Test an empty request.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void pathEmptyTest() throws Exception {
        RequestParser.parsePath(emptyModel, "");
    }

    /**
     * Test a null request.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void requestNullTest() throws Exception {
        RequestParser.parseQuery(emptyModel, null);
    }

    /**
     * Test a malformed request - too few parameters.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void requestMalformedParameterTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "x1=1&y1=2&x1=3&filter=1");
    }

    /**
     * Test a malformed request - letters.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void requestMalformedLetterTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "x1=A&x2=2&y1=C&y2=4&filter=X");
    }

    /**
     * Test a malformed request - wrong order.
     * @throws Exception If something unexpected happens.
     */
    @Test public void requestMalformedOrderTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "y2=1.2&x2=2&x1=2&x2=4&filter=3");
    }

    /**
     * Test a malformed request - wrong variable names.
     * @throws Exception If something unexpected happens.
     */
    @Test public void requestMalformedNameTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "y3=1.2&x2=2&x1=2&x2=4&filter=16");
    }

    /**
     * Test a malformed request - wrong filter value.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void requestMalformedFilterValueTest1() throws Exception {
        RequestParser.parseQuery(emptyModel, "y3=1.2&x2=2&x1=2&x2=4&filter=3000");
    }

    /**
     * Test a malformed request - wrong filter value.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void requestMalformedFilterValueTest2() throws Exception {
        RequestParser.parseQuery(emptyModel, "y3=1.2&x2=2&x1=2&x2=4&filter=0");
    }

    /**
     * Test a malformed request - wrong decoding.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void requestMalformedDecodingTest() throws Exception {
        String input = "y3=1.2&x2=2&x1=2&x2=4&filter=0";
        String encoded = URLEncoder.encode(input, "Cp1252"); // Windows Latin-1
        RequestParser.parseQuery(emptyModel, encoded);
    }

}
