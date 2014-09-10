/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author whiskey
 */
public class XmlConnectorFactory implements ConnectorFactory {

    @Override
    public String getIdentifier() {
        return "id-xml";
    }

    @Override
    public Connector createConnector() {
        return new XmlConnector();
    }
}
