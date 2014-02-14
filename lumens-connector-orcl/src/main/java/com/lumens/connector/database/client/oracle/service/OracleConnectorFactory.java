package com.lumens.connector.database.client.oracle.service;

import com.lumens.LumensException;
import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.database.client.oracle.OracleConnector;

public class OracleConnectorFactory implements ConnectorFactory {

    @Override
    public Connector createConnector(String className) {
        if (OracleConnector.class.getName().equals(className))
            return new OracleConnector();
        throw new LumensException(String.format("Unsupported class '%s'", className));
    }
}