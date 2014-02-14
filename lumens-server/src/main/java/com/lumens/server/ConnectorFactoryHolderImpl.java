/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import com.lumens.connector.ConnectorFactory;
import com.lumens.engine.ConnectorFactoryHolder;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ConnectorFactoryHolderImpl implements ConnectorFactoryHolder {


    public ConnectorFactoryHolderImpl() {
    }

    @Override
    public ConnectorFactory getFactory(String className) {
        return null;
    }
}
