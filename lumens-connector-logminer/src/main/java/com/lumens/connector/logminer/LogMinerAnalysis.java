/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface LogMinerAnalysis {

    public void build();

    public void start();

    public void end();

    public void query();
}
