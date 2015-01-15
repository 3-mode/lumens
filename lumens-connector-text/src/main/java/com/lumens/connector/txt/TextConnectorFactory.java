/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextConnectorFactory implements ConnectorFactory {

    @Override
    public String getComponentType() {
        return "type-text";
    }

    @Override
    public Connector createConnector() {
        return new TextConnector();
    }
}
