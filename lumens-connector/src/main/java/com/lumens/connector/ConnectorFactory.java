/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

/**
 *
 * @author shaofeng wang
 */
public interface ConnectorFactory {

    public String DESCRIPTOR = "descriptor";
    public String TYPE_PROPERTY = "type";
    public String ID_PROPERTY = "id";
    public String NAME_PROPERTY = "name";
    public String CLASS_NAME_PROPERTY = "class_name";
    public String INSTANCE_ICON_PROPERTY = "instance_icon";
    public String ITEM_ICON_PROPERTY = "item_icon";
    public String PROPS_PROPERTY = "property";
    public String I18N_PROPERTY = "i18n";

    public Connector createConnector(String className);
}
