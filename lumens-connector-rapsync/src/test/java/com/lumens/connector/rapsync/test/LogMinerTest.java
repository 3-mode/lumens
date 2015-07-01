/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.test;

import com.lumens.connector.rapsync.api.Config;
import com.lumens.connector.rapsync.api.LogMiner;
import com.lumens.connector.rapsync.api.LogMinerFactory;
import com.lumens.connector.rapsync.impl.DefaultLogMiner;
import com.lumens.connector.rapsync.impl.DefaultLogMiner;
import java.sql.ResultSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerTest extends RapSyncTestBase {

    // Too slow for test, comment out
    //@Test
    public void testStoreInFileLogMinerRead() {
        Config config = new Config();
        config.setBuildType(LogMiner.LOG_TYPE.OFFLINE);
        config.setDictType(LogMiner.DICT_TYPE.STORE_IN_REDO_LOG);
        config.setCommittedDataOnly(true);
        config.setNoRowid(true);
        config.setContinuousMine(true);
        config.setStartSCN("3940324");
        config.setEndSCN("3949568");

        LogMiner miner = LogMinerFactory.createLogMiner(sourceDatabase, config);
        miner.buildDictionary();
        miner.build();
        miner.start();
        try (ResultSet result = miner.query("SELECT SQL_REDO, SCN, OPERATION, SEG_OWNER, TABLE_NAME FROM v$logmnr_contents WHERE ( seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE' AND seg_owner='LUMENS')  ORDER BY SCN ASC")) {
            System.out.println("Querying redo log:");
            int max = 1000;
            while (result.next() && max-- < 0) {
                System.out.println(result.getString(1));
            }
        } catch (Exception ex) {
            assertTrue("Fail to query log miner result. Error message:" + ex.getMessage(), false);
        }
    }
}
