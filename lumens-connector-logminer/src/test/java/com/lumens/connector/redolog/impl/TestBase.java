/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.redolog.impl;

import com.lumens.connector.redolog.impl.DefaultLogMiner;
import com.lumens.connector.redolog.impl.DatabaseClient;
import com.lumens.logsys.SysLogFactory;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TestBase {

    protected final Logger log = SysLogFactory.getLogger(DefaultLogMiner.class);

    protected static final String DATABASE_DRIVER_VAL = "file:///D:/app/oracle/11.2.0/dbhome/jdbc/lib/ojdbc6.jar";
    protected static final String DATABASE_SOURCE_URL_VAL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
    protected static final String DATABASE_SOURCE_USERNAME_VAL = "lumens as sysdba";
    protected static final String DATABASE_SOURCE_PASSWORD_VAL = "lumens";
    protected static final String DATABASE_DESTINATION_URL_VAL = "jdbc:oracle:thin:@127.0.0.1:1521:logminer";
    protected static final String DATABASE_DESTINATION_USERNAME_VAL = "lumens as sysdba";
    protected static final String DATABASE_DESTINATION_PASSWORD_VAL = "lumens";
    protected DatabaseClient sourceDatabase = null;
    protected DatabaseClient destinationDatabase = null;

    @Before
    public void setUp() throws Exception {
        sourceDatabase = new DatabaseClient(DATABASE_DRIVER_VAL, DATABASE_SOURCE_URL_VAL, DATABASE_SOURCE_USERNAME_VAL, DATABASE_SOURCE_PASSWORD_VAL);
        destinationDatabase = new DatabaseClient(DATABASE_DRIVER_VAL, DATABASE_DESTINATION_URL_VAL, DATABASE_DESTINATION_USERNAME_VAL, DATABASE_DESTINATION_PASSWORD_VAL);
    }

    @After
    public void tearDown() {
        sourceDatabase.release();
        destinationDatabase.release();
    }
}
