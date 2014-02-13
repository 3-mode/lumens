package com.lumens.connector.webservice;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.model.LumensException;

public class SoapConnectorFactory implements ConnectorFactory {

    @Override
    public Connector createConnector(String className) {
        if (WebServiceConnector.class.getName().equals(className))
            return new WebServiceConnector();
        throw new LumensException(String.format("Unsupported class '%s'", className));
    }
}