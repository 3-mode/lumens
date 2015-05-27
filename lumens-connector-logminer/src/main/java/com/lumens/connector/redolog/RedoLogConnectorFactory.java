/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.redolog;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RedoLogConnectorFactory implements ConnectorFactory {

    @Override
    public String getComponentType() {
        return "type-logminer";
    }

    @Override
    public Connector createConnector() {
        return new RedoLogConnector();
    }
}
