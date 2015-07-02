/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle.service;

import com.lumens.connector.database.client.oracle.OracleConnectorFactory;
import com.lumens.connector.ConnectorActivator;

public class Activator extends ConnectorActivator {

    public Activator() {
        super(new OracleConnectorFactory(), Activator.class);
    }
}
