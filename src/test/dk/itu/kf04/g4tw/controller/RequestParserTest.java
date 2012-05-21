package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.MapModel;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Jegp
 * Date: 21/05/12
 * Time: 18:48
 * To change this template use File | Settings | File Templates.
 */
public class RequestParserTest {

    protected MapModel emptyModel = new MapModel();

    /**
     * Test a malformed request - wrong order.
     * @throws Exception If something unexpected happens.
     */
    @Test
    public void queryMalformedOrderTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "y2=1.2&x2=2&x1=2&x2=4&filter=3");
    }

    /**
     * Test a malformed request - wrong variable names.
     * @throws Exception If something unexpected happens.
     */
    @Test public void queryMalformedNameTest() throws Exception {
        RequestParser.parseQuery(emptyModel, "y3=1.2&x2=2&x1=2&x2=4&filter=16");
    }

}
