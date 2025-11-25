package com.github.pjfanning.releasevalidator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class PomCheck {

    private final static String pomNamespaceUri = "http://maven.apache.org/POM/4.0.0";
    private final static Map<String, String> pomNamespaces = new HashMap<>();
    static {
        pomNamespaces.put("pom", pomNamespaceUri);
    }

    static String checkPom(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        Document doc = XMLHelper.parse(is);
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new SimpleNamespaceContext(pomNamespaces));
        try {
            NodeList licenses = (NodeList) xpath.evaluate(
                    "/pom:project/pom:licenses/pom:license",
                    doc, XPathConstants.NODESET);
            if (licenses.getLength() == 0) {
                return "No licenses found in POM";
            }
            for (int i = 0; i < licenses.getLength(); i++) {
                Node node = licenses.item(i);
                if (node instanceof Element) {
                    String name = getTextNS((Element) node, pomNamespaceUri, "name");
                    if (name != null && name.toLowerCase(Locale.ROOT).contains("apache")) {
                        return null;
                    }
                }
                return "Found no Apache License in Pom";
            }
        } catch (Exception e) {
            return String.format("Failed to evaluate license: ", e);
        }
        return null;
    }

    private static String getTextNS(Element parent, String namespaceURI, String localName) {
        NodeList elements = parent.getElementsByTagNameNS(namespaceURI, localName);
        if (elements.getLength() == 0) {
            throw new RuntimeException("No element found for namespace " + namespaceURI + " localName " + localName);
        } else if (elements.getLength() == 1) {
            return elements.item(0).getTextContent();
        }
        throw new RuntimeException("Found multiple elements (expected 1) for namespace " + namespaceURI + " localName " + localName);
    }
}
