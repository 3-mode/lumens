/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

import java.io.InputStream;

/**
 *
 * @author whiskey
 */
public class XmlClient {

    XmlConnector xml;

    XmlClient(XmlConnector cnt) {
        xml = cnt;
    }

    String getVersion() {
        return null;
    }

    //void open();
    //void close();
    public void init() {
    }

    public void parseXml(InputStream in) {
    }
}
