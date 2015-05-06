/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RedoLogTest {

    private static final String DATABASE_DRIVER = "file:///D:/app/oracle/11.2.0/dbhome/jdbc/lib/ojdbc6.jar";
    private static final String DATABASE_SOURCE_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
    private static final String DATABASE_SOURCE_USERNAME = "logminer";
    private static final String DATABASE_SOURCE_PASSWORD = "logminer";
    private DatabaseClient dbClient = null;

    public RedoLogTest() {
    }

    @Before
    public void setUp() throws Exception {
        dbClient = new DatabaseClient(DATABASE_DRIVER, DATABASE_SOURCE_URL, DATABASE_SOURCE_USERNAME, DATABASE_SOURCE_PASSWORD);
    }

    @Test
    public void testGetOnlineFileList() throws Exception {
        assertTrue(dbClient != null);
        RedoLog redoLog = new RedoLog(dbClient);
        List<String> onlineList = redoLog.getOnlineFileList();
        assertTrue("online redo log should not be empty", onlineList.size() > 0);
        System.out.println("Online redo log list:");
        for (String item : onlineList) {
            System.out.println(item);
        }
        System.out.println("Offline redo log list:");
        for (String item : redoLog.getOfflineFileList()) {
            System.out.println(item);
        }
    }

    @After
    public void tearDown() {
        dbClient.release();
    }
}
