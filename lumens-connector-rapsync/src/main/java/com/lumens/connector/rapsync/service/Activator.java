/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.service;

import com.lumens.connector.ConnectorActivator;
import com.lumens.connector.rapsync.RapSyncConnectorFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class Activator extends ConnectorActivator {

    public Activator() {
        super(new RapSyncConnectorFactory(), Activator.class);
    }
}
