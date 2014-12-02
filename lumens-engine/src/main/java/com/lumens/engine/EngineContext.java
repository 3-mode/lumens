/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author shaofeng.wang
 */
public class EngineContext {

    private static EngineContext instance;
    private final ConnectorFactoryHolder fHolder;

    private EngineContext(ConnectorFactoryHolder fHolder) {
        this.fHolder = fHolder;
    }

    public static void start(ConnectorFactoryHolder fHolder) {
        instance = new EngineContext(fHolder);
    }

    public static EngineContext getContext() {
        return instance;
    }

    public ConnectorFactory getConnectorFactory(String componentType) {
        if (fHolder != null)
            return fHolder.getFactory(componentType);
        return null;
    }
}
