/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

/**
 *
 * @author whiskey
 */
public class XmlClientFactory {

    static XmlClient createDOMClient(XmlConnector cntr){
        return new DomClient(cntr);
    }

    static XmlClient createSAXClient(XmlConnector cntr){
        return new SaxClient(cntr);
    }
}
