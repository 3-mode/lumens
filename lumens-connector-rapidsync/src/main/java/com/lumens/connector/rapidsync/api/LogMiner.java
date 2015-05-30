/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapidsync.api;

import java.sql.ResultSet;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface LogMiner {

    public static enum DICT_TYPE {

        ONLINE,
        STORE_IN_REDO_LOG, // used for OFFLINE redo log only
        STORE_IN_FILE
    };

    public static enum BUILD_TYPE {

        ONLINE, // conflict with STORE_IN_REDO_LOG
        OFFLINE
    };

    public static enum OPERATION {

        UPDATE,
        DELETE,
        INSERT,
        DDL
    }

    public void buildDictionary();

    public void build();

    public void start();

    public void end();

    public ResultSet query(String sql);

    public void sync(RedoValue value) throws Exception;
}
