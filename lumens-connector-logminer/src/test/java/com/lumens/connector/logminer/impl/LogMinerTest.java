/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.logminer.api.Config;
import java.sql.ResultSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerTest extends TestBase {

    @Test
    public void testStoreInFileLogMinerRead() {
        Config config = new Config();
        config.setBuildType(LogMinerImpl.BUILD_TYPE.OFFLINE);
        config.setDictType(LogMinerImpl.DICT_TYPE.STORE_IN_REDO_LOG);
        config.setCommittedDataOnly(true);
        config.setNoRowid(true);
        config.setStartSCN("0");
        
        LogMinerImpl miner = new LogMinerImpl(dbClient, config);
        miner.build();
        miner.start();
        ResultSet result = miner.query();
        try {
            System.out.println("Querying redo log:");
            while (result.next()) {                
                System.out.println(result.getString(5));
            }
        } catch (Exception ex) {
            assertTrue("Fail to query log miner result. Error message:" + ex.getMessage(), false);
        }
    }
}
