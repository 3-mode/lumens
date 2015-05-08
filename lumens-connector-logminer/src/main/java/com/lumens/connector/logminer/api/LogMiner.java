/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.api;

import java.sql.ResultSet;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface LogMiner {

    public void build();

    public void start();

    public void end();

    public ResultSet query();
}