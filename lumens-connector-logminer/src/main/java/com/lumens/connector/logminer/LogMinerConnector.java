/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

import com.lumens.connector.logminer.api.LogMiner;
import com.lumens.connector.Operation;
import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.logminer.api.LogMinerFactory;
import com.lumens.connector.logminer.api.Config;
import com.lumens.connector.logminer.impl.DatabaseClient;
import com.lumens.connector.logminer.impl.LogMinerImpl;
import com.lumens.logsys.LogSysFactory;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerConnector implements Connector, LogMinerConstants {

    private final Logger log = LogSysFactory.getLogger(LogMinerImpl.class);
    private Config config = null;
    private String dbDriver = null;
    private String dbUrl = null;
    private String dbUserName = null;
    private String dbPassword = null;

    private LogMiner miner = null;
    private DatabaseClient dbConnection = null;

    boolean isOpen = false;

    public LogMinerConnector() {
        config = new Config();
        config.setBuildType(LogMiner.BUILD_TYPE.OFFLINE);
        config.setDictType(LogMiner.DICT_TYPE.STORE_IN_REDO_LOG);
        config.setCommittedDataOnly(true);
        config.setNoRowid(true);
        config.setStartSCN("0");
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void open() {
        try {
            dbConnection = new DatabaseClient(dbDriver, dbUrl, dbUserName, dbPassword);
            miner = LogMinerFactory.createLogMiner(dbConnection, config);

            isOpen = true;
        } catch (Exception ex) {
            log.error("Fail to open LogMiner connector. Error message:" + ex.getMessage());
            throw new RuntimeException("Fail to open LogMiner connector. Error message:" + ex.getMessage());
        }
    }

    @Override
    public void close() {
        miner.end();
    }

    @Override
    public Operation getOperation() {
        return new LogMinerOperation(miner);
    }

    // get redo log fields from db
    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        Map<String, Format> formatList = new HashMap();

        return formatList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        return null;
    }

    @Override
    public void start() {
        // start build directory       
        // check parameter availablity
        // check soruce db as well as minger db with sufficient priviledge 
        // start log analysis
        // start processing redo log sql: query and process each sql
    }

    @Override
    public void stop() {
    }

    @Override
    public void setPropertyList(Map<String, Value> parameters) {

        // setup connection
        if (parameters.containsKey(DATABASE_DRIVER)) {
            dbDriver = parameters.get(DATABASE_DRIVER).getString();
        }
        if (parameters.containsKey(DATABASE_SOURCE_URL)) {
            dbUrl = parameters.get(DATABASE_SOURCE_URL).getString();
        }
        if (parameters.containsKey(DATABASE_SOURCE_USERNAME)) {
            dbUserName = parameters.get(DATABASE_SOURCE_USERNAME).getString();
        }
        if (parameters.containsKey(DATABASE_SOURCE_PASSWORD)) {
            dbPassword = parameters.get(DATABASE_SOURCE_PASSWORD).getString();
        }

        // setup config
        if (parameters.containsKey(BUILD_TYPE_ONLINE)) {
            config.setBuildType(LogMiner.BUILD_TYPE.ONLINE);
        }
        if (parameters.containsKey(BUILD_TYPE_OFFLINE)) {
            config.setBuildType(LogMiner.BUILD_TYPE.OFFLINE);
        }

        if (parameters.containsKey(DICT_TYPE_ONLINE)) {
            config.setDictType(LogMiner.DICT_TYPE.ONLINE);
        }
        if (parameters.containsKey(DICT_TYPE_STORE_IN_REDO_LOG)) {
            config.setDictType(LogMiner.DICT_TYPE.STORE_IN_REDO_LOG);
        }
        if (parameters.containsKey(DICT_TYPE_STORE_IN_FILE)) {
            config.setDictType(LogMiner.DICT_TYPE.STORE_IN_FILE);
        }

        if (parameters.containsKey(COMMITED_DATA_ONLY)) {
            config.setCommittedDataOnly(true);
        }
        if (parameters.containsKey(NO_ROWID)) {
            config.setNoRowid(parameters.get(NO_ROWID).getBoolean());
        }
        if (parameters.containsKey(START_SCN)) {
            config.setStartSCN(parameters.get(START_SCN).getString());
        }
        if (parameters.containsKey(END_SCN)) {
            config.setStartSCN(parameters.get(END_SCN).getString());
        }
    }
}
