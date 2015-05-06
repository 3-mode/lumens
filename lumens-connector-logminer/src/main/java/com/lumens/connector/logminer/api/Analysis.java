/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.api;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface Analysis {

    public void build();

    public void start();

    public void end();

    public void query();
}
