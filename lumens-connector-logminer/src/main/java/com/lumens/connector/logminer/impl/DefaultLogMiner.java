/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.logminer.api.Config;
import com.lumens.connector.logminer.api.LogMiner;
import com.lumens.connector.logminer.api.LogMiner.BUILD_TYPE;
import com.lumens.connector.logminer.api.LogMiner.DICT_TYPE;
import com.lumens.connector.database.DBUtils;
import java.sql.ResultSet;
import org.apache.logging.log4j.Logger;
import com.lumens.logsys.LogSysFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultLogMiner implements LogMiner, Constants {

    private final Logger log = LogSysFactory.getLogger(DefaultLogMiner.class);
    private String LAST_SCN = "0";
    private ResultSet result = null;
    private Config config = null;
    private Dictionary dict = null;
    private DatabaseClient dbClient = null;

    public DefaultLogMiner(DatabaseClient dbClient, Config config) {
        this.dbClient = dbClient;
        this.config = config;

        if (config.getBuildType() == BUILD_TYPE.ONLINE && config.getDictType() == DICT_TYPE.STORE_IN_REDO_LOG) {
            log.error("Should not specify option DICT_FROM_REDO_LOGS to analyze online redo logs");
            throw new RuntimeException("Should not specify option DICT_FROM_REDO_LOGS to analyze online redo logs");
        }
    }

    public void buildDictionary() {
        try {
            if (config.getDictType() == DICT_TYPE.STORE_IN_FILE) {
                dict = new Dictionary(dbClient);
                dict.createDictionary();
            }
        } catch (Exception ex) {
            log.error("Fail to build log miner dictionary. Error message:");
            log.error(ex.getMessage());
            throw new RuntimeException("Fail to build log miner dictionary. Error message:" + ex.getMessage());
        }
    }

    @Override
    public void build() {
        try {

            // adding redo log files to analyze
            RedoLog redolog = new RedoLog(dbClient);
            if (config.getDictType() == DICT_TYPE.STORE_IN_REDO_LOG && !redolog.isArchivedLogModeEnabled()) {
                log.error("Fail to build log miner dictionary. Error message: Archived log should be enabled with STORE_IN_REDO_LOG enabled");
                throw new RuntimeException("Fail to build log miner dictionary. Error message: Archived log should be enabled with STORE_IN_REDO_LOG enabled");
            }
            if (!redolog.isSupplementalLogEnabled()) {
                log.info("Tryinng to enable Suplemental Log Mode.");
                if (config.isEnabledSupplementalLogMode() && redolog.enableSupplementalLog()) {
                    log.info("Succeed enabled Suplemental Log Mode.");
                }
                if (!redolog.isSupplementalLogEnabled()) {
                    log.error("Fail to build log miner dictionary. Error message: Supplemental Log Mode should be enabled priot to start LogMiner build");
                    throw new RuntimeException("Fail to build log miner dictionary. Error message: Supplemental Log Mode should be enabled priot to start LogMiner build");
                }
            }
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
            log.error("Fail to build log miner. Error message:");
            log.error(ex.getMessage());
            throw new RuntimeException("Fail to build log miner. Error message:" + ex.getMessage());
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
    public ResultSet query(String sql
    ) {
        if (result != null) {
            DBUtils.releaseResultSet(result);
        }
        try {
            //String defaultCondition = " WHERE seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
            result = dbClient.executeGetResult(sql);
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

    @Override
    public void sync(String operation, String scn, String sql) throws Exception {
        if (Integer.parseInt(scn) < Integer.parseInt(LAST_SCN)) {
            return;
        }

        // Dictionary was changed, need to rebuild 
        if(operation.equalsIgnoreCase("DDL")){
            buildDictionary();
        }
        try {
            dbClient.execute(sql);
            LAST_SCN = scn;
        } catch (Exception ex) {
            log.error("Fail to sync to destination. Error message:");
            log.error(ex.getMessage());
            throw new RuntimeException("Fail to sync to destination. Error message:" + ex.getMessage());
        }
    }
}
