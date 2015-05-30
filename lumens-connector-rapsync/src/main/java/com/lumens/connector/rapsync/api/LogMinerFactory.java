/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.api;

import com.lumens.connector.rapsync.impl.DefaultLogMiner;
import com.lumens.connector.rapsync.impl.DatabaseClient;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerFactory {
    public static LogMiner createLogMiner(DatabaseClient db, Config config){
        return new DefaultLogMiner(db, config);
    }
}
