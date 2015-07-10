/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.ConnectorFactory;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DataSourceLocalConnectorFactoryManager implements ConnectorFactoryManager {

    Map<String, ConnectorFactory> factoryList = new HashMap<>();

    @Override
    public ConnectorFactory getFactory(String componentType) {
        return factoryList.get(componentType);
    }

    public void register(ConnectorFactory factory) {
        factoryList.put(factory.getComponentType(), factory);
    }

}
