/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.impl;

import com.lumens.connector.rapsync.api.Config;
import com.lumens.connector.rapsync.api.LogMiner;
import com.lumens.connector.rapsync.api.LogMiner.LOG_TYPE;
import com.lumens.connector.rapsync.api.LogMiner.DICT_TYPE;
import com.lumens.connector.database.DBUtils;
import com.lumens.connector.rapsync.api.RedoValue;
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
    private Config config = null;
    private Dictionary dict = null;
    private DatabaseClient dbClient = null;
    private Metadata meta = null;
    private boolean isFirstBuild = true;
    private CachedRedoLog redolog = null;

    public DefaultLogMiner(DatabaseClient dbClient, Config config) {
        this.dbClient = dbClient;
        this.config = config;
        meta = new Metadata(dbClient);

        if (config.getLogType() == LOG_TYPE.ONLINE && config.getDictType() == DICT_TYPE.STORE_IN_REDO_LOG) {
            log.error("Should not specify option DICT_FROM_REDO_LOGS to analyze online redo logs");
            throw new RuntimeException("Should not specify option DICT_FROM_REDO_LOGS to analyze online redo logs");
        }

        redolog = new CachedRedoLog(dbClient);  // TODO: changed to cached redo log
    }

    @Override
    public void buildDictionary() {
        log.debug("Building dictionary.");
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
        log.debug("Building REDO Log files.");
        try {
            // adding redo log files to analyze
            if (isFirstBuild) {
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
                        log.error("Fail to build log miner dictionary. Error message: Supplemental Log Mode should be enabled prior to start LogMiner build");
                        throw new RuntimeException("Fail to build log miner dictionary. Error message: Supplemental Log Mode should be enabled prior to start LogMiner build");
                    }
                }
            }
            redolog.setLogType(config.getLogType());
            RedoLogQuery query = redolog.getQuery();
            if (!query.hasNext()) {
                log.info("No more log files to analyze.");
                return;
            }
            String buildList = redolog.buildLogMinerStringFromList(query.next(), true);

            // checking added redo logs
            if (log.isDebugEnabled()) {
                ResultSet addedLogsResult = dbClient.executeGetResult(SQL_QUERY_LOG_INFO);
                while (addedLogsResult.next()) {
                    log.debug(addedLogsResult.getString(3));
                }
                DBUtils.releaseResultSet(addedLogsResult);
                dbClient.releaseStatement();
            }

            dbClient.execute(buildList);
            isFirstBuild = false;
        } catch (Exception ex) {
            log.error("Fail to build log miner. Error message:");
            log.error(ex.getMessage());
            throw new RuntimeException("Fail to build log miner. Error message:" + ex.getMessage());
        }
    }

    @Override
    public boolean hasNextBuild() {
        return redolog.getQuery().hasNext();
    }

    @Override
    public void start() {
        log.debug("Start redolog analysis.");
        try {
            String parameter;
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
        log.debug("Start redolog query.");
        try {
            //String defaultCondition = " WHERE seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
            return dbClient.executeGetResult(sql);
        } catch (Exception ex) {
            log.error("Fail to query log miner results. Error message:");
            log.error(ex.getMessage());
            throw new RuntimeException("Fail to query log miner results. Error message:" + ex.getMessage());
        }
    }

    @Override
    public void end() {
        log.debug("End redolog analysis.");
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
        if (value.SQL_REDO.isEmpty()) {
            log.warn("Skip empty REDO log record with SCN:" + value.SCN);
            return;
        }
        if (!isSupportedOperation(value.OPERATION)) {
            log.warn("Unsupported operation: SCN is " + value.SCN + ", OPERATION is " + value.OPERATION);
            return;
        }
        String user = dbClient.getUser();
        if (!value.SEG_OWNER.isEmpty()) {
            if (!user.equalsIgnoreCase(value.SEG_OWNER)) {
                value.SQL_REDO = value.SQL_REDO.replaceAll(value.SEG_OWNER, user);
            }
        }
        // JDBC Driver bug: invalid character exception while SQL end with comma
        String SQL = value.SQL_REDO.replaceAll(";", "");

        // Dictionary was changed, need to rebuild 
        if (value.OPERATION.equalsIgnoreCase("ddl")) {
            dbClient.commit();
            buildDictionary();
        }
        if (value.OPERATION.equalsIgnoreCase("corrupted_blocks") || value.STATUS == 1343) {
            log.warn("Skip corruption data: SCN is %s, SQL is ", value.SCN, value.SQL_REDO);
            return;
        }

        boolean doAgain = false;
        do {
            try {                
                dbClient.execute(SQL);
                LAST_SCN = value.SCN;
                log.info(String.format("SCN %s synced : %s", LAST_SCN, SQL));
                dbClient.commit();
            } catch (Exception ex) {
                log.error("Fail to sync to destination. Error message:");
                log.error(ex.getMessage());
                log.error("Failed on statement:" + value.SQL_REDO);
                log.info("Trying to find out failure reason...");

                if (value.TABLE_NAME.isEmpty()) {
                    log.warn("TABLE_NAME is not mapped. Suggest to map table name so that to deal with known situation while encouner errors.");
                } else if (!meta.checkTableExist(user, value.TABLE_NAME)) {
                    log.info(String.format("Table %s not exist.", value.TABLE_NAME));
                    if (value.OPERATION.equalsIgnoreCase("delete")) {
                        log.warn("Skip sync for 'delete' operation.");
                        break;
                    }
                    if (value.OPERATION.equalsIgnoreCase("ddl")) {
                        if (value.SQL_REDO.toLowerCase().trim().startsWith("drop table")) {
                            log.warn("Skip sync for 'drop table' operation.");
                            break;
                        }
                        if (value.SQL_REDO.toLowerCase().trim().startsWith("alter table")) {
                            log.warn("Skip sync for 'alter table' operation.");
                            break;
                        }
                    }
                } else if (value.OPERATION.equalsIgnoreCase("ddl") && value.SQL_REDO.toLowerCase().trim().startsWith("create table")) {
                    log.warn("Table exist. Skip sync for 'create table' operation.");
                    break;
                }

                if ((value.OPERATION.equalsIgnoreCase("update") || value.OPERATION.equalsIgnoreCase("delete")) && !meta.checkRecordExist(value.SQL_REDO)) {
                    log.warn(String.format("Record not exist. Skip sync for ''%s", value.OPERATION));
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
