/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SqlServerConnectorFactory implements ConnectorFactory{

    @Override
    public Connector createConnector() {
        return new SqlServerConnector();
    }

    @Override
    public String getComponentType() {
        return "type-sqlserver-jdbc";
    }   
}
