/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author whiskey
 */
public class Activator implements AddinActivator {
    // Create factory and register service

    @Override
    public void start(AddinContext ctx) {

        ConnectorFactory fact = new XmlConnectorFactory();
        ctx.registerService(fact.getIdentifier(), fact, null);
    }

    @Override
    public void stop(AddinContext ctx) {
    }
}
