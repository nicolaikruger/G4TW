package dk.itu.kf04.g4tw.controller;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import static junit.framework.Assert.*;

/**
 * A test for the XMLBuilder class.
 * 
 * @author Jens Egholm <jegp@itu.dk>
 */
public class XMLBuilderTest {
    
    XMLBuilder builder;
    
    @Before
    public void setUp() {
        try {
            builder = new XMLBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println("Error in instantiating XML builder.");
        } catch (TransformerConfigurationException e) {
            System.err.println("Error in configuring XML transformer.");
        }
    }
    
    @Test
    public void createDocumentTest() {
        Document d = builder.createDocument();
        
        assertNotNull(d);
    }
}
