/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.redolog.api;

import com.lumens.connector.redolog.impl.DefaultLogMiner;
import com.lumens.connector.redolog.impl.DatabaseClient;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerFactory {
    public static LogMiner createLogMiner(DatabaseClient db, Config config){
        return new DefaultLogMiner(db, config);
    }
}
