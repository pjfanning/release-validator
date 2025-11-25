package com.github.pjfanning.releasevalidator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

class XMLHelper {
    private static DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY;

    private static DocumentBuilderFactory getDocumentBuilderFactory() throws ParserConfigurationException {
        if (DOCUMENT_BUILDER_FACTORY == null) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            dbf.setExpandEntityReferences(false);
            dbf.setXIncludeAware(false);
            String feature = "http://apache.org/xml/features/disallow-doctype-decl";
            dbf.setFeature(feature, true);
            DOCUMENT_BUILDER_FACTORY = dbf;
        }
        return DOCUMENT_BUILDER_FACTORY;
    }

    static Document parse(InputStream inputStream)
            throws ParserConfigurationException, IOException, SAXException {
        return getDocumentBuilderFactory().newDocumentBuilder().parse(inputStream);
    }
}
