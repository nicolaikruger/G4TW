package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.MapModel;
import org.junit.Test;

import java.net.URLEncoder;

/**
 * Tests malformed calls to the request parser which always should result in exceptions.
 */
public class RequestParserMalformedTest {

    protected MapModel emptyModel = new MapModel();

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
        RequestParser.parsePath(emptyModel, null);
    }

    /**
     * Test a malformed request - too few parameters.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void requestMalformedParameterTest() throws Exception {
        RequestParser.parsePath(emptyModel, "adr1=Tomsgårdsvej 12");
    }

    /**
     * Test a malformed request - wrong decoding.
     * @throws Exception If something unexpected happens.
     */
    @Test (expected = IllegalArgumentException.class)
    public void requestMalformedDecodingTest() throws Exception {
        String input = "adr1=Ællingekjæret 18&adr2=Øresundslæ 150 A";
        String encoded = URLEncoder.encode(input, "Cp1252"); // Windows Latin-1
        RequestParser.parsePath(emptyModel, encoded);
    }

}
