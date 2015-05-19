/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class MetadataTest extends TestBase {

    @Test
    public void testGetTableDDL() {
        Metadata meta = new Metadata(sourceDatabase);
        String statement = meta.getTableDDL("sys", "dual");
        assertTrue("Fail to get table DDL", statement != null);
        System.out.println(statement);
    }

    @Test
    public void testCheckRecordExist() {

    }

    @Test
    public void testCheckTableExist() {
        Metadata meta = new Metadata(sourceDatabase);
        assertTrue("Table not exist", meta.checkTableExist("sys", "dual"));
    }
}
