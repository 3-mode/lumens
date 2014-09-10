/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

/**
 *
 * @author whiskey
 *
 * <?xml version="1.0" encoding="UTF-8"?>
 * <employees>
 * <!--An XML Note -->
 * <?target text?>
 * <employee id="473774" name="Wisper Guo">
 * <sex>M</sex>
 * <age>35</age>
 * </employee>
 * <employee id="463775" name="James Wang">
 * <sex>M</sex>
 * <age>36</age>
 * </employee>
 * </employees>
 */
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import java.io.InputStream;
import org.xml.sax.SAXException;
import java.io.IOException;

public class DOMClient extends XmlClient {

    private Document xmlDoc;
    private String xmlPath;
    private DocumentBuilder builder = null;

    DOMClient() {
        super(null);
        init();
    }

    @Override
    public void init() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            xmlDoc = builder.newDocument();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void parseXml(InputStream in) {
        try {
            xmlDoc = builder.parse(in);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     <root>
     <element attrName="attrVal"> val </element>
     </root>
     */
    public Element createElement(String name, String val, String attrName, String attrVal) {
        Element elem = xmlDoc.createElement(name);

        if (null != val) {
            elem.appendChild(xmlDoc.createTextNode(val));
            if (null != attrName) {
                Attr attr = createAttr(attrName, attrVal);
                elem.setAttributeNode(attr);
            }
        }

        return elem;
    }

    /*
     <element name=val></element>
     */
    public Attr createAttr(String name, String val) {
        Attr attr = xmlDoc.createAttribute(name);
        attr.setNodeValue(val);

        return attr;
    }

    /*
     <parent>
     <child></child>
     </parent>
     */
    public void appendChild(Element parent, Element child) {
        parent.appendChild(child);
    }
}
