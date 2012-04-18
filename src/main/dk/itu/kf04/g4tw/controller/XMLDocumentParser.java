package dk.itu.kf04.g4tw.controller;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander
 * Date: 18-04-12
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
public class XMLDocumentParser {
	DocumentBuilderFactory dbFac;
	DocumentBuilder docBuilder;

	TransformerFactory transformerFactory;
	Transformer transformer;

	StringWriter sw = new StringWriter();
	StreamResult result = new StreamResult(sw);

	public XMLDocumentParser() {
		try {
			//We need a Document
			dbFac = DocumentBuilderFactory.newInstance();
			docBuilder = dbFac.newDocumentBuilder();

			//set up a transformer
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

	public Document createDocument() {
		return docBuilder.newDocument();
	}

	public String createXMLString(Document doc) throws TransformerException {
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = sw.toString();

		return xmlString;
	}
}
