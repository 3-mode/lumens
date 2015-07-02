/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt.service;

import com.lumens.connector.ConnectorActivator;
import com.lumens.connector.txt.TextConnectorFactory;

public class Activator extends ConnectorActivator {

    public Activator() {
        super(new TextConnectorFactory(), Activator.class);
    }
}