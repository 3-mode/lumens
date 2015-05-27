/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.redolog.impl.Metadata;
import org.junit.Test;
import static org.junit.Assert.*;
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
        Metadata meta = new Metadata(sourceDatabase);
        String update = "update \"SYSMAN\".\"MGMT_FAILOVER_TABLE\" set \"LAST_TIME_STAMP\" = TO_DATE('17-MAY-15', 'DD-MON-RR'), \"LAST_TIME_STAMP_UTC\" = TO_TIMESTAMP('17-MAY-15 10.15.32.301000 AM') where \"LAST_TIME_STAMP\" = TO_DATE('17-MAY-15', 'DD-MON-RR') and \"LAST_TIME_STAMP_UTC\" = TO_TIMESTAMP('17-MAY-15 10.14.32.272000 AM');";
        //assertTrue("Fail to check update record exist", meta.checkRecordExist(update));
        meta.checkRecordExist(update);
        
        String deleteFrom = "delete from \"SYS\".\"SMON_SCN_TIME\" where \"THREAD\" = '0' and \"TIME_MP\" = '1430895971' and \"TIME_DP\" = TO_DATE('06-MAY-15', 'DD-MON-RR') and \"SCN_WRP\" = '0' and \"SCN_BAS\" = '4382283' and \"NUM_MAPPINGS\" = '100'";
        //assertTrue("Fail to check delete from record exist", meta.checkRecordExist(deleteFrom));
        meta.checkRecordExist(deleteFrom);
    }

    @Test
    public void testCheckTableExist() {
        Metadata meta = new Metadata(sourceDatabase);
        assertTrue("Table not exist", meta.checkTableExist("sys", "dual"));
    }
}
