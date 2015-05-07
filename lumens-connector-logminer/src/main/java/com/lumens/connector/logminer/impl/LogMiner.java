/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.logminer.LogMinerClient;
import com.lumens.connector.logminer.api.Analysis;
import static com.lumens.connector.database.DBConstants.DESCRIPTION;
import static com.lumens.connector.database.DBConstants.TYPE;
import com.lumens.connector.database.DBUtils;
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
import org.apache.logging.log4j.Logger;
import com.lumens.logsys.LogSysFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMiner implements Analysis, Constants {

    public static enum DICT_TYPE {

        ONLINE,
        STORE_IN_LOG,
        STORE_IN_FILE
    };
    private static String LAST_SCN = "0";

    private ResultSet result = null;
    private DICT_TYPE dict_type = null;
    private Dictionary dict;

    private final Logger log = LogSysFactory.getLogger(LogMiner.class);

    /**
     * Redo log file
     */
    private String DATABASE_REDO_LOG_PATH; // for example "D:\\app\\oracle\\oradata\\orcl";

    private DatabaseClient dbClient;

    public LogMiner(DatabaseClient dbClient, DICT_TYPE type) {
        this.dbClient = dbClient;
        this.dict_type = type;
    }

    @Override
    public void build() {
        if (result != null) {
            DBUtils.releaseResultSet(result);
        }

        try {
            if (dict_type == DICT_TYPE.STORE_IN_FILE) {
                dict = new Dictionary(dbClient);
                dict.createDictionary();
            }

            // adding redo log files to analyze
            RedoLog redolog = new RedoLog(dbClient);
            String buildList = redolog.buildLogMinerStringFromList(redolog.getOnlineFileList(), true);
            result = dbClient.executeGetResult(buildList + "");

            // checking added redo logs
            ResultSet addedLogs = dbClient.executeGetResult(SQL_QUERY_LOG_INFO);
            while (addedLogs.next()) {
                log.info(addedLogs.getString(3));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Fail to build log miner dictionary. Error message:" + ex.getMessage());
        }
    }

    @Override
    public void start() {
        try {
            if (dict_type == DICT_TYPE.STORE_IN_FILE) {
                dbClient.executeGetResult("BEGIN dbms_logmnr.start_logmnr(startScn=>'" + LAST_SCN + "',dictfilename=>'" + DATABASE_REDO_LOG_PATH + "\\" + dict.getDictionaryPath() + "',OPTIONS =>DBMS_LOGMNR.COMMITTED_DATA_ONLY+dbms_logmnr.NO_ROWID_IN_STMT);END;");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Fail to start log miner analysis. Error message:" + ex.getMessage());
        }
    }

    @Override
    public void end() {
        try {
            dbClient.executeGetResult(SQL_QUERY_RESULT_SIZE);
        } catch (Exception ex) {            
        }
    }

    @Override
    public void query() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
