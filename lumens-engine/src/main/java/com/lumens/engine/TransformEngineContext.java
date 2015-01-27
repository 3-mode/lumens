/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author shaofeng.wang
 */
public class TransformEngineContext {

    private static TransformEngineContext instance;
    private final ConnectorFactoryHolder fHolder;

    private TransformEngineContext(ConnectorFactoryHolder fHolder) {
        this.fHolder = fHolder;
    }

    public static void start(ConnectorFactoryHolder fHolder) {
        instance = new TransformEngineContext(fHolder);
    }

    public static TransformEngineContext getContext() {
        return instance;
    }

    public ConnectorFactory getConnectorFactory(String componentType) {
        if (fHolder != null)
            return fHolder.getFactory(componentType);
        return null;
    }
}
