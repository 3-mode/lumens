/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

/**
 *
 * @author shaofeng wang
 */
public interface ConnectorFactory {

    public static final String NAME_PROPERTY = "connector.name";
    public static final String INSTANCE_ICON_PROPERTY = "connector.instance.icon";
    public static final String CATALOG_ICON_PROPERTY = "connector.catalog.icon";

    public Connector createConnector(String className);
}
