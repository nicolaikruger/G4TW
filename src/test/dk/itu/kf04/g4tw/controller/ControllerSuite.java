package dk.itu.kf04.g4tw.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests the classes in the Model.
 *
 * @author Jens Egholm <jegp@itu.dk>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        RequestParserTest.class,
        RequestParserMalformedTest.class,
        WebServerTest.class,
        XMLBuilderTest.class
})
public class ControllerSuite { }
