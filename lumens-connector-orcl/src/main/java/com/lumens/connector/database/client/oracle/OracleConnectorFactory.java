/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;

public class OracleConnectorFactory implements ConnectorFactory {

    @Override
    public Connector createConnector() {
        return new OracleConnector();
    }

    @Override
    public String getComponentType() {
        return "type-oracle-jdbc";
    }
}