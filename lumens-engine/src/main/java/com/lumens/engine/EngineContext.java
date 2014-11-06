/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.ConnectorFactory;
import com.lumens.processor.script.JavaScriptContext;

/**
 *
 * @author shaofeng.wang
 */
public class EngineContext {

    private static EngineContext instance;
    private ConnectorFactoryHolder fHolder;

    private EngineContext(ConnectorFactoryHolder fHolder) {
        this.fHolder = fHolder;
    }

    public static void start(ConnectorFactoryHolder fHolder) {
        instance = new EngineContext(fHolder);
        JavaScriptContext.start();
    }

    public static EngineContext getContext() {
        return instance;
    }

    public ConnectorFactory getConnectorFactory(String className) {
        if (fHolder != null)
            return fHolder.getFactory(className);
        return null;
    }
}
