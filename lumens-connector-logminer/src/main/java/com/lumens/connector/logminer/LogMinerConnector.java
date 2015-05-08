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
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerConnector implements Connector, LogMinerConstants {

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
            throw new RuntimeException("Fail to open connector");
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
    }
}
