/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapidsync.impl;

import com.lumens.connector.rapidsync.api.Config;
import com.lumens.connector.rapidsync.api.LogMiner;
import com.lumens.connector.rapidsync.api.LogMiner.BUILD_TYPE;
import com.lumens.connector.rapidsync.api.LogMiner.DICT_TYPE;
import com.lumens.connector.database.DBUtils;
import com.lumens.connector.rapidsync.api.RedoValue;
import java.sql.ResultSet;
import org.apache.logging.log4j.Logger;
import com.lumens.logsys.SysLogFactory;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultLogMiner implements LogMiner, Constants {

    private final Logger log = SysLogFactory.getLogger(DefaultLogMiner.class);
    private int LAST_SCN = 0;
    private ResultSet result = null;
    private Config config = null;
    private Dictionary dict = null;
    private DatabaseClient dbClient = null;
    private Metadata meta = null;

    public DefaultLogMiner(DatabaseClient dbClient, Config config) {
        this.dbClient = dbClient;
        this.config = config;
        meta = new Metadata(dbClient);

        if (config.getBuildType() == BUILD_TYPE.ONLINE && config.getDictType() == DICT_TYPE.STORE_IN_REDO_LOG) {
            log.error("Should not specify option DICT_FROM_REDO_LOGS to analyze online redo logs");
            throw new RuntimeException("Should not specify option DICT_FROM_REDO_LOGS to analyze online redo logs");
        }
    }

    @Override
    public void buildDictionary() {
        try {
            if (config.getDictType() == DICT_TYPE.STORE_IN_FILE) {
                if (dict == null) {
                    dict = new Dictionary(dbClient);
                }
                dict.build();
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
                if (redolog.enableSupplementalLog()) {
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
                dbClient.releaseStatement();
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
    public ResultSet query(String sql) {
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
    public void sync(RedoValue value) throws Exception {
        if (value.SCN < LAST_SCN) {
            return;
        }
        if (!isSupportedOperation(value.OPERATION)) {
            log.warn("Unsupported operation: SCN is " + value.SCN + ", OPERATION is " + value.OPERATION);
            return;
        }
        // JDBC Driver bug: invalid character exception while SQL end with comma
        String SQL = value.SQL_REDO.replaceAll(";", "");

        // Dictionary was changed, need to rebuild 
        if (value.OPERATION.equalsIgnoreCase("DDL")) {
            buildDictionary();
        }

        boolean doAgain = false;
        do {
            try {
                dbClient.execute(SQL);
                LAST_SCN = value.SCN;
                log.info(String.format("SCN %s synced", LAST_SCN));
            } catch (Exception ex) {
                log.error("Fail to sync to destination. Error message:");
                log.error(ex.getMessage());
                log.error("Failed on statement:" + value.SQL_REDO);
                log.info("Trying to find out failure reason...");

                if (!meta.checkTableExist(value.SEG_OWNER, value.TABLE_NAME)) {
                    log.info(String.format("Table %s not exist.", value.TABLE_NAME));
                    if (value.OPERATION.equalsIgnoreCase("delete")) {
                        log.info("Skip sync for 'delete' operation.");
                        break;
                    }
                    if (value.OPERATION.equalsIgnoreCase("ddl")) {
                        if (value.SQL_REDO.toLowerCase().trim().startsWith("drop table")) {
                            log.info("Skip sync for 'drop table' operation.");
                            break;
                        }
                        if (value.SQL_REDO.toLowerCase().trim().startsWith("alter table")) {
                            log.info("Skip sync for 'alter table' operation.");
                            break;
                        }
                    }
                    // Not a valid statement, CREATION DDL should get from source db
                    //if (meta.createTable(value.SEG_OWNER, value.TABLE_NAME)) {
                    //    log.info(String.format("Table %s created. Try to sync again...", value.TABLE_NAME));
                    //    doAgain = true;
                    //    continue;
                    //}
                    //else{
                    //    log.error(String.format("Fail to create table %s. SCN:%s, SEG_NAME:%s, SEG_TYPE:%s, SQL_REDO: %s", value.TABLE_NAME, value.SCN, value.SEG_NAME, value.SEG_TYPE, value.SQL_REDO));
                    //    break;
                    //}
                }

                if ((value.OPERATION.equalsIgnoreCase("update") || value.OPERATION.equalsIgnoreCase("delete")) && !meta.checkRecordExist(value.SQL_REDO)) {
                    log.error("Record not exist. Ignore error and continue.");
                    break;
                }

                log.error(String.format("Fail to sync to destination. Fail on : SCN = %s, REDO = %s", value.SCN, value.SQL_REDO));
                throw new RuntimeException("Fail to sync to destination. Error message:" + ex);
            }
        } while (doAgain);
    }

    private boolean isSupportedOperation(String operation) {
        boolean isSupport = false;
        try {
            OPERATION.valueOf(operation);
            isSupport = true;
        } catch (Exception ex) {

        }

        return isSupport;
    }
}
