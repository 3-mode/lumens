/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

/**
 *
 * @author shaofeng wang
 */
public interface ConnectorFactory {

    public String getIdentifier();

    public Connector createConnector();
}