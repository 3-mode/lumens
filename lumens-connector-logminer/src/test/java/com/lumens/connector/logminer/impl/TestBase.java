/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.logsys.LogSysFactory;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TestBase {

    private final Logger log = LogSysFactory.getLogger(LogMinerImpl.class);
    
    protected static final String DATABASE_DRIVER_VAL = "file:///D:/app/oracle/11.2.0/dbhome/jdbc/lib/ojdbc6.jar";
    protected static final String DATABASE_SOURCE_URL_VAL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
    protected static final String DATABASE_SOURCE_USERNAME_VAL = "logminer";
    protected static final String DATABASE_SOURCE_PASSWORD_VAL = "logminer";
    protected DatabaseClient dbClient = null;

    @Before
    public void setUp() throws Exception {
        dbClient = new DatabaseClient(DATABASE_DRIVER_VAL, DATABASE_SOURCE_URL_VAL, DATABASE_SOURCE_USERNAME_VAL, DATABASE_SOURCE_PASSWORD_VAL);
    }

    @After
    public void tearDown() {
        dbClient.release();
    }
}
