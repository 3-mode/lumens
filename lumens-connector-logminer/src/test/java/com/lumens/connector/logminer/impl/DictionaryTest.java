/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DictionaryTest {

    private static final String DATABASE_DRIVER = "file:///D:/app/oracle/11.2.0/dbhome/jdbc/lib/ojdbc6.jar";
    private static final String DATABASE_SOURCE_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
    private static final String DATABASE_SOURCE_USERNAME = "logminer";
    private static final String DATABASE_SOURCE_PASSWORD = "logminer";
    private DatabaseClient dbClient = null;

    @Before
    public void setUp() throws Exception {
        dbClient = new DatabaseClient(DATABASE_DRIVER, DATABASE_SOURCE_URL, DATABASE_SOURCE_USERNAME, DATABASE_SOURCE_PASSWORD);
    }

    @Test
    public void testDictionary() throws Exception {
        assertTrue(dbClient != null);
        Dictionary dict = new Dictionary(dbClient);
        assertTrue("dictionary should not be empty", !dict.getDictionaryPath().isEmpty());
        assertTrue("Fail to create dictionary file", dict.createDictionary());
    }

    @After
    public void tearDown() {
        dbClient.release();
    }
}
