package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Road;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.awt.geom.Point2D;
import java.io.*;

import static junit.framework.Assert.assertNotNull;

/**
 * Tests well formed calls to the RequestParser.
 * Malformed calls are tested in {@link RequestParserMalformedTest}.
 */
public class RequestParserTest extends RequestParser {

    MapModel emptyModel = new MapModel();
    MapModel model;
    
    protected int ALL = 255; // All road-types

    protected ByteArrayOutputStream os;
    
    protected Road a = new Road(0, "Ferskenvej",
            new Point2D.Double(0, 0), new Point2D.Double(100, 100),
            MapModel.MINOR_ROAD, 80, 213, 1, 11, "A", "C", 6830, 6830);
    protected Road b = new Road(1, "Åboulevarden",
            new Point2D.Double(50, 50), new Point2D.Double(150, 150),
            MapModel.PATH, 1, 17, 2, 16, "", "", 1350, 1350);

    /**
     * Setup a model.
     */
    @Before
    public void setUp() {
        model = new MapModel();
        model.addRoad(a);
        model.addRoad(b);

        // Output stream
        os = new ByteArrayOutputStream();
    }

    /**
     * Test empty return.
     * @throws Exception If something unexpected happens.
     */
    @Test
    public void queryEmptyTest() throws Exception {
        //byte[] result = parseQuery(emptyModel, "x1=0&y1=0&x2=100&y2=100&filter=255");


    }

    /**
     * Test a request where parameters are given in the wrong order.
     * @throws Exception If something unexpected happens.
     */
    @Test
    public void queryOrderTest() throws Exception {
        //byte[] result = RequestParser.parseQuery(model, "y2=200&x2=200&y1=0&x1=0&filter=" + ALL);

        //assertNotNull(result);
    }

    /**
     * Test a malformed request - wrong variable names.
     * @throws Exception If something unexpected happens.
     */
    @Test public void queryMalformedNameTest() throws Exception {
        //RequestParser.parseQuery(model, "y3=1.2&x2=2&x1=2&x2=4&filter=16");
    }

}
