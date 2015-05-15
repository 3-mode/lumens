/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.api;

import com.lumens.connector.logminer.impl.DefaultLogMiner;
import com.lumens.connector.logminer.impl.DatabaseClient;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerFactory {
    public static LogMiner createLogMiner(DatabaseClient db, Config config){
        return new DefaultLogMiner(db, config);
    }
}
