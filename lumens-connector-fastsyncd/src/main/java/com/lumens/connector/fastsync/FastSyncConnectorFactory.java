/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.fastsync;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class FastSyncConnectorFactory implements ConnectorFactory {

    @Override
    public String getComponentType() {
        return "type-logminer";
    }

    @Override
    public Connector createConnector() {
        return new FastSyncConnector();
    }
}
