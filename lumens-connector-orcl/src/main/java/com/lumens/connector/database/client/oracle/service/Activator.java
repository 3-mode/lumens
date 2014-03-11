package com.lumens.connector.database.client.oracle.service;

import com.lumens.addin.AbstractActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.database.client.oracle.OracleConnector;

public class Activator extends AbstractActivator {

    private AddinContext addinContext;

    @Override
    public void start(AddinContext context) {
        addinContext = context;
        addinContext.registerService(OracleConnector.class.getName(), new OracleConnectorFactory(), super.processDescriptor(Activator.class));
    }

    @Override
    public void stop(AddinContext context) {
    }
}
