package com.lumens.connector.webservice.service;

import com.lumens.connector.webservice.soap.SoapConnectorFactory;
import com.lumens.connector.ConnectorActivator;

public class Activator extends ConnectorActivator {

    public Activator() {
        super(new SoapConnectorFactory(), Activator.class);
    }
}
