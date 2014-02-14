/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import com.lumens.addin.AddinContext;
import com.lumens.addin.ServiceEntity;
import com.lumens.connector.ConnectorFactory;
import com.lumens.engine.ConnectorFactoryHolder;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ConnectorFactoryHolderImpl implements ConnectorFactoryHolder {

    private AddinContext addinContext;

    public ConnectorFactoryHolderImpl(AddinContext ac) {
        addinContext = ac;
    }

    @Override
    public ConnectorFactory getFactory(String className) {
        ServiceEntity<ConnectorFactory> se = addinContext.getService(className);
        return se.getService();
    }
}
