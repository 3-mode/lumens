/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.database.DBConstants;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface Constants {//extends DBConstants {

    public String ORACLE_CLASS = "oracle.jdbc.OracleDriver";

    public String DICTIONARY_FILE = "dictionary.ora";
    public String SQL_GEG_VERSION = "select * from V$INSTANCE";
    public String SQL_START_LOGMINER = "BEGIN dbms_logmnr.start_logmnr(%s);END;";
    public String SQL_END_LOGMINER = "BEGIN dbms_logmnr.end_logmnr();END;";
    public String SQL_QUERY_DIRECTORY_PATH = "select DIRECTORY_PATH from dba_directories where DIRECTORY_NAME='UTL_FILE_DIR'";
    public String SQL_QUERY_LOG_INFO = "SELECT db_name, thread_sqn, filename FROM v$logmnr_logs";

    //public String SQL_QUERY_RESULT = "SELECT scn,operation,timestamp,status,sql_redo FROM v$logmnr_contents WHERE seg_owner='" + Constants.DATABASE_SOURCE_CLIENT_USERNAME + "' AND seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
    public String SQL_QUERY_RESULT = "SELECT %s FROM v$logmnr_contents";
    public String SQL_QUERY_ARCHIVED_LOG = "SELECT name FROM v$archived_log";
    public String SQL_QUERY_LOGFILE = "SELECT member FROM v$logfile order by member asc";
    public String SQL_QUERY_LOG_HISTORY = "SELECT * FROM v$log_history";
    public String SQL_QUERY_LOG = "SELECT * FROM v$log";
    public String SQL_QUERY_RESULT_SIZE = "SELECT count(sql_redo) FROM v$logmnr_contents";
}
