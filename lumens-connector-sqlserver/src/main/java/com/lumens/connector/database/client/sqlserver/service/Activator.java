/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.database.client.sqlserver.service;

import com.lumens.connector.database.client.sqlserver.SqlServerConnectorFactory;
import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
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
        ConnectorFactory factory = new SqlServerConnectorFactory();
        addinContext.registerService(factory.getComponentType(), factory, DescriptorUtils.processDescriptor(Activator.class, "sqlserver", factory.getComponentType()));
    }

    @Override
    public void stop(AddinContext context) {
    }
}