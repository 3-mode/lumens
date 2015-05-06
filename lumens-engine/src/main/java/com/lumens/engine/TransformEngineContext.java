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
    private final ConnectorFactoryManager fManager;
    private boolean isLogElement;

    private TransformEngineContext(ConnectorFactoryManager fManager) {
        this.fManager = fManager;
    }

    public static void start(ConnectorFactoryManager fManager) {
        instance = new TransformEngineContext(fManager);
    }

    public static TransformEngineContext getContext() {
        return instance;
    }

    public ConnectorFactory getConnectorFactory(String componentType) {
        if (fManager != null)
            return fManager.getFactory(componentType);
        return null;
    }

    public void setLogElement(boolean isLogElement) {
        this.isLogElement = isLogElement;
    }

    public boolean isLogElement() {
        return this.isLogElement;
    }
}
