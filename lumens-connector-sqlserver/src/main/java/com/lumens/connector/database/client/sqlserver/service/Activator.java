/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.database.client.sqlserver.service;

import com.lumens.connector.database.client.sqlserver.SqlServerConnectorFactory;
import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
import com.lumens.descriptor.DescriptorUtils;

public class Activator implements AddinActivator {

    private AddinContext addinContext;

    @Override
    public void start(AddinContext context) {
        addinContext = context;
        ConnectorFactory factory = new SqlServerConnectorFactory();
        addinContext.registerService(factory.getIdentifier(), factory, DescriptorUtils.processDescriptor(Activator.class, "sqlserver", factory.getIdentifier()));
    }

    @Override
    public void stop(AddinContext context) {
    }
}