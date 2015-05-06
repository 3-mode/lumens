/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.logminer.LogMinerClient;
import com.lumens.connector.logminer.api.Analysis;
import static com.lumens.connector.database.DBConstants.DESCRIPTION;
import static com.lumens.connector.database.DBConstants.TYPE;
import com.lumens.connector.database.client.AbstractClient;
import com.lumens.connector.database.client.DBConnector;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMiner implements Analysis {

    private static String LAST_SCN = "0";

    /**
     * Redo log file
     */
    private String DATABASE_LOG_FILE_PATH; // for example "D:\\app\\oracle\\oradata\\orcl";


    private LogMinerClient dbClient;

    public LogMiner(LogMinerClient dbClient) {
        this.dbClient = dbClient;
    }



    @Override
    public void build() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void end() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void query() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
