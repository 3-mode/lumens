/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.sqlserver.service;

import com.lumens.connector.ConnectorActivator;
import com.lumens.connector.database.sqlserver.SQLServerConnectorFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class Activator extends ConnectorActivator {

    public Activator() {
        super(new SQLServerConnectorFactory(), Activator.class);
    }
}