/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TestBase {

    protected static final String DATABASE_DRIVER = "file:///D:/app/oracle/11.2.0/dbhome/jdbc/lib/ojdbc6.jar";
    protected static final String DATABASE_SOURCE_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
    protected static final String DATABASE_SOURCE_USERNAME = "logminer";
    protected static final String DATABASE_SOURCE_PASSWORD = "logminer";
    protected DatabaseClient dbClient = null;

    @Before
    public void setUp() throws Exception {
        dbClient = new DatabaseClient(DATABASE_DRIVER, DATABASE_SOURCE_URL, DATABASE_SOURCE_USERNAME, DATABASE_SOURCE_PASSWORD);
    }

    @After
    public void tearDown() {
        dbClient.release();
    }
}
