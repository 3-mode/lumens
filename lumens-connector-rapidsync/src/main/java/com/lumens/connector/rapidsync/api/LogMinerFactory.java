/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapidsync.api;

import com.lumens.connector.rapidsync.impl.DefaultLogMiner;
import com.lumens.connector.rapidsync.impl.DatabaseClient;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerFactory {
    public static LogMiner createLogMiner(DatabaseClient db, Config config){
        return new DefaultLogMiner(db, config);
    }
}
