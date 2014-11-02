package com.lumens.connector.webservice.soap;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;

public class SoapConnectorFactory implements ConnectorFactory {

    @Override
    public Connector createConnector() {
        return new SoapConnector();
    }

    @Override
    public String getComponentType() {
        return "type-soap";
    }
}