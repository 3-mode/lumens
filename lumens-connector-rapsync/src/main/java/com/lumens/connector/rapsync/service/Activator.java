/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.service;

import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.rapsync.RapSyncConnectorFactory;
import com.lumens.descriptor.DescriptorUtils;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class Activator implements AddinActivator {

    private AddinContext addinContext;

    @Override
    public void start(AddinContext context) {
        addinContext = context;
        ConnectorFactory factory = new RapSyncConnectorFactory();
        addinContext.registerService(factory.getComponentType(), factory, DescriptorUtils.processDescriptor(Activator.class, "rapsync", factory.getComponentType()));
    }

    @Override
    public void stop(AddinContext context) {
    }
}
