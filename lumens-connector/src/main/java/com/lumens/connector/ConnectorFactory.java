/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

/**
 *
 * @author shaofeng wang
 */
public interface ConnectorFactory {

    public String ID_PROPERTY = "connector.id";
    public String NAME_PROPERTY = "connector.name";
    public String CLASS_NAME_PROPERTY = "connector.class.name";
    public String INSTANCE_ICON_PROPERTY = "connector.instance.icon";
    public String CATALOG_ICON_PROPERTY = "connector.catalog.icon";

    public Connector createConnector(String className);
}
