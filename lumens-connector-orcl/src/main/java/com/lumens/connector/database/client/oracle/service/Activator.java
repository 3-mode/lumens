/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle.service;

import com.lumens.connector.database.client.oracle.OracleConnectorFactory;
import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
import com.lumens.descriptor.DescriptorUtils;

public class Activator implements AddinActivator {

    private AddinContext addinContext;

    @Override
    public void start(AddinContext context) {
        addinContext = context;
        ConnectorFactory factory = new OracleConnectorFactory();
        addinContext.registerService(factory.getComponentType(), factory, DescriptorUtils.processDescriptor(context.getLanguage(), "orcl", factory.getComponentType(), Activator.class));
    }

    @Override
    public void stop(AddinContext context) {
    }
}
