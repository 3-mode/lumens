/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.fastsync.service;

import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.fastsync.FastSyncConnectorFactory;
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
        ConnectorFactory factory = new FastSyncConnectorFactory();
        addinContext.registerService(factory.getComponentType(), factory, DescriptorUtils.processDescriptor(Activator.class, "logminer", factory.getComponentType()));
    }

    @Override
    public void stop(AddinContext context) {
    }
}
