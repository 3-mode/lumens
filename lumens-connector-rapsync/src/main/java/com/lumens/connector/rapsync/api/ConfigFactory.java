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
        config.setBuildType(LogMiner.BUILD_TYPE.OFFLINE);
        config.setDictType(LogMiner.DICT_TYPE.STORE_IN_REDO_LOG);
        config.setCommittedDataOnly(true);
        config.setNoRowid(true);
        config.setStartSCN("0");
        
        return config;
    }
}
