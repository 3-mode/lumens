/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

/**
 *
 * @author whiskey
 */
public class XmlClientFactory {

    XmlClient createDOMClient(XmlConnector cnt) {
        return new DOMClient();
    }

    XmlClient createSAXClient(XmlConnector cnt) {
        return new SAXClient();
    }
}
