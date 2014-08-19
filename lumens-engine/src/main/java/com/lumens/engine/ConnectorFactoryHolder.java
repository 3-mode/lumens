/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.ConnectorFactory;

/**
 *
 * @author shaofeng.wang
 */
public interface ConnectorFactoryHolder {

    public ConnectorFactory getFactory(String className);
}
