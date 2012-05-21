package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.MapModel;
import dk.itu.kf04.g4tw.model.Road;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

/**
 * Tests well formed calls to the RequestParser.
 * Malformed calls are tested in {@link RequestParserMalformedTest}.
 */
public class RequestParserTest {

    MapModel model;
    
    protected int ALL = 255; // All road-types

    /**
     * Setup a model.
     */
    @Before
    public void setUp() {
        model = new MapModel();
        Road a = new Road(0, "Ferskenvej",
                new Point2D.Double(0, 0), new Point2D.Double(100, 100),
                MapModel.MINOR_ROAD, 80, 213, 1, 11, "A", "C", 6830, 6830);
        Road b = new Road(1, "Åboulevarden",
                new Point2D.Double(50, 50), new Point2D.Double(150, 150),
                MapModel.PATH, 1, 17, 2, 16, "", "", 1350, 1350);
        model.addRoad(a);
        model.addRoad(b);
    }

    /**
     * Test a request where parameters are given in the wrong order.
     * @throws Exception If something unexpected happens.
     */
    @Test
    public void queryOrderTest() throws Exception {
        //RequestParser.parseQuery(model, "y2=200&x2=200&y1=0&x1=0&filter=" + ALL);
    }

    /**
     * Test a malformed request - wrong variable names.
     * @throws Exception If something unexpected happens.
     */
    @Test public void queryMalformedNameTest() throws Exception {
        //RequestParser.parseQuery(model, "y3=1.2&x2=2&x1=2&x2=4&filter=16");
    }

}
