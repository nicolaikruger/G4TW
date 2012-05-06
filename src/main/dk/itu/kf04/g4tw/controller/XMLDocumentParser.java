package dk.itu.kf04.g4tw.controller;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander
 * Date: 18-04-12
 * Time: 11:52
 *
 *
 */
public class XMLDocumentParser {
	/**
	 * DocumentBuilder used to create fresh documents for further use.
	 */
	DocumentBuilderFactory dbFac;
	DocumentBuilder docBuilder;

	/**
	 * Transformer used when translating Java objects to XML strings
	 */
	TransformerFactory transformerFactory;
	Transformer transformer;

	/**
	 * Sets up the XMLDocumentParser for further use.
	 */
	public XMLDocumentParser() {
		try {
			// Creating a document builder
			dbFac = DocumentBuilderFactory.newInstance();
			docBuilder = dbFac.newDocumentBuilder();

			// Setup a transformer
			// This is defining the properties that are needed by the transformer.
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used for encapsulating most XML operations to the DocumentParser and
	 * Road class.
	 *
	 * @return A freshly created document
	 */
	public Document createDocument() {return docBuilder.newDocument();}

	/**
	 * Transforms a java Document object into a clean-text XML string.
	 * This is used to translate the java to a browser-compatible response.
	 *
	 * @param doc An XML document needing a transformation to clean-text
	 * @return An XML string in clean-text of the document
	 * @throws TransformerException
	 */
	public String createXMLString(Document doc) throws TransformerException {
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = sw.toString();

		sw.flush();
		try {sw.close();} catch (IOException e) {e.printStackTrace();}

		return xmlString;
	}
}
