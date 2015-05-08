/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.logminer.api.Config;
import com.lumens.connector.logminer.api.LogMiner;
import com.lumens.connector.database.DBUtils;
import java.sql.ResultSet;
import org.apache.logging.log4j.Logger;
import com.lumens.logsys.LogSysFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerImpl implements LogMiner, Constants {

    public static enum DICT_TYPE {

        ONLINE,
        STORE_IN_REDO_LOG,  // used for OFFLINE redo log only
        STORE_IN_FILE
    };

    public static enum BUILD_TYPE {

        ONLINE,  // conflict with STORE_IN_REDO_LOG
        OFFLINE
    };

    private final Logger log = LogSysFactory.getLogger(LogMinerImpl.class);
    private String LAST_SCN = "0";
    private ResultSet result = null;
    private Config config = null;
    private Dictionary dict = null;
    private DatabaseClient dbClient = null;

    public LogMinerImpl(DatabaseClient dbClient, Config config) {
        this.dbClient = dbClient;
        this.config = config;

        if (config.getBuildType() == BUILD_TYPE.ONLINE && config.getDictType() == DICT_TYPE.STORE_IN_REDO_LOG) {
            log.error("Should not specify option DICT_FROM_REDO_LOGS to analyze online redo logs");
            throw new RuntimeException("Should not specify option DICT_FROM_REDO_LOGS to analyze online redo logs");
        }
    }

    @Override
    public void build() {
        try {
            if (config.getDictType() == DICT_TYPE.STORE_IN_FILE) {
                dict = new Dictionary(dbClient);
                dict.createDictionary();
            }

            // adding redo log files to analyze
            RedoLog redolog = new RedoLog(dbClient);
            String buildList = redolog.buildLogMinerStringFromList(config.getBuildType() == BUILD_TYPE.ONLINE ? redolog.getOnlineFileList() : redolog.getOfflineFileList(), true);
            dbClient.execute(buildList + "");

            // checking added redo logs
            if (log.isDebugEnabled()) {
                ResultSet addedLogsResult = dbClient.executeGetResult(SQL_QUERY_LOG_INFO);
                while (addedLogsResult.next()) {
                    log.debug(addedLogsResult.getString(3));
                }
                DBUtils.releaseResultSet(addedLogsResult);
            }            
        } catch (Exception ex) {
            log.error("Fail to build log miner dictionary. Error message:");
            log.error(ex.getMessage());
            throw new RuntimeException("Fail to build log miner dictionary. Error message:" + ex.getMessage());
        }
    }

    @Override
    public void start() {
        try {
            String parameter = null;
            if (config.getDictType() == DICT_TYPE.STORE_IN_FILE) {
                parameter = String.format(config.buildParameters(), dict.getDictionaryPath(), DICTIONARY_FILE);
            } else {
                parameter = config.buildParameters();
            }
            dbClient.execute(String.format(SQL_START_LOGMINER, parameter));
        } catch (Exception ex) {
            log.error("Fail to start log miner analysis. Error message:");
            log.error(ex.getMessage());
            throw new RuntimeException("Fail to start log miner analysis. Error message:" + ex.getMessage());
        }
    }

    @Override
    public ResultSet query() {
        if (result != null) {
            DBUtils.releaseResultSet(result);
        }
        try {
            result = dbClient.executeGetResult(SQL_QUERY_RESULT);
            return result;
        } catch (Exception ex) {
            log.error("Fail to query log miner results. Error message:");
            log.error(ex.getMessage());
            throw new RuntimeException("Fail to query log miner results. Error message:" + ex.getMessage());
        }
    }

    @Override
    public void end() {
        try {
            dbClient.execute(SQL_END_LOGMINER);
        } catch (Exception ex) {
            log.error("Logminer connector end with exception:");
            log.error(ex.getMessage());
        }
    }
}
