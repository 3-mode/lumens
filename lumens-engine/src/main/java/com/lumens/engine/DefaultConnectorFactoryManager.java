/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.addin.AddinContext;
import com.lumens.addin.ServiceEntity;
import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DefaultConnectorFactoryManager implements ConnectorFactoryManager {

    private final AddinContext addinContext;

    public DefaultConnectorFactoryManager(AddinContext ac) {
        addinContext = ac;
    }

    @Override
    public ConnectorFactory getFactory(String componentType) {
        ServiceEntity<ConnectorFactory> se = addinContext.getService(componentType);
        return se != null ? se.getService() : null;
    }
}