package com.lumens.connector.webservice.service;

import com.lumens.connector.webservice.soap.SoapConnectorFactory;
import com.lumens.addin.AddinActivator;
import com.lumens.addin.AddinContext;
import com.lumens.connector.ConnectorFactory;
import com.lumens.descriptor.DescriptorUtils;

public class Activator implements AddinActivator {

    private AddinContext addinContext = null;

    @Override
    public void start(AddinContext ctx) {
        addinContext = ctx;
        ConnectorFactory factory = new SoapConnectorFactory();
        addinContext.registerService(factory.getIdentifier(), factory, DescriptorUtils.processDescriptor(Activator.class, "soap", factory.getIdentifier()));
    }

    @Override
    public void stop(AddinContext ctx) {
    }
}