package com.lumens.connector.webservice.service;

import com.lumens.LumensException;
import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.webservice.WebServiceConnector;

public class SoapConnectorFactory implements ConnectorFactory {

    @Override
    public Connector createConnector(String className) {
        if (WebServiceConnector.class.getName().equals(className))
            return new WebServiceConnector();
        throw new LumensException(String.format("Unsupported class '%s'", className));
    }
}