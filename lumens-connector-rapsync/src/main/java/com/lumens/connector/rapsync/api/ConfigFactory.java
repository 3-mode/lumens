/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.api;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class ConfigFactory {

    public static Config createDefaultConfig() {
        Config config = new Config();
        config.setBuildType(LogMiner.LOG_TYPE.ONLINE);
        config.setDictType(LogMiner.DICT_TYPE.ONLINE);
        config.setCommittedDataOnly(true);
        config.setNoRowid(true);
        config.setStartSCN("0");
        
        return config;
    }
}
