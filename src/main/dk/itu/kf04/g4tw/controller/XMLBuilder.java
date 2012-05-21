package dk.itu.kf04.g4tw.controller;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;

/**
 * A class used to build XML using the javax.xml library.
 * @author Alexander Jørgensen <akir@itu.dk>
 * @author Jens Egholm <jegp@itu.dk>
 */
public class XMLBuilder {

    /**
	 * DocumentBuilder used to create fresh documents for further use.
	 */
	protected DocumentBuilderFactory dbFac;
	protected DocumentBuilder docBuilder;

	/**
	 * Transformer used when translating Java objects to XML strings
	 */
	protected TransformerFactory transformerFactory;
	protected Transformer transformer;

	/**
	 * Sets up the XMLBuilder for further use.
     * @throws TransformerConfigurationException  If the configuration went wrong.
     * @throws ParserConfigurationException  If the parser-configuration went wrong.
	 */
	public XMLBuilder() throws ParserConfigurationException, TransformerConfigurationException {
        // Creating a document builder
        dbFac = DocumentBuilderFactory.newInstance();
        docBuilder = dbFac.newDocumentBuilder();

        // Setup a transformer
        // This is defining the properties that are needed by the transformer.
        transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	}

	/**
	 * This method is used for encapsulating most XML operations to the DocumentParser and
	 * Road class.
	 *
	 * @return A freshly created document
	 */
	public Document createDocument() {
        return docBuilder.newDocument();
    }

}
