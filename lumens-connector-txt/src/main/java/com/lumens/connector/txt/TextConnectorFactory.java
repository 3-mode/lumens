/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author whiskey
 */
public class TextConnectorFactory implements ConnectorFactory {

    @Override
    public String getComponentType() {
        return "type-txt";
    }

    @Override
    public Connector createConnector() {
        return new TextConnector();
    }
}
