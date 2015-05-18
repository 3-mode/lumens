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
    public String SQL_GEG_VERSION = "SELECT version FROM sys.v$instance";  // need sys privelege
    public String SQL_CHECK_SUPPLEMENTAL_LOG = "SELECT supplemental_log_data_min FROM sys.v_$database";  // need sys priveledge
    public String SQL_CHECK_LOG_MODE = "SELECT log_mode FROM sys.v_$database";
    public String SQL_ENABLE_SUPPLEMENTAL_LOG = "ALTER DATABASE ADD SUPPLEMENTAL LOG DATA;";
    public String SQL_START_LOGMINER = "BEGIN sys.dbms_logmnr.start_logmnr(%s);END;";
    public String SQL_END_LOGMINER = "BEGIN sys.dbms_logmnr.end_logmnr();END;";
    public String SQL_QUERY_DIRECTORY_PATH = "SELECT directory_path FROM sys.dba_directories WHERE DIRECTORY_NAME='UTL_FILE_DIR'";
    public String SQL_QUERY_LOG_INFO = "SELECT db_name, thread_sqn, filename FROM sys.v$logmnr_logs";

    //public String SQL_QUERY_RESULT = "SELECT scn,operation,timestamp,status,sql_redo FROM v$logmnr_contents WHERE seg_owner='" + Constants.DATABASE_SOURCE_CLIENT_USERNAME + "' AND seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
    public String SQL_QUERY_RESULT = "SELECT %s FROM sys.v$logmnr_contents";
    public String SQL_QUERY_ARCHIVED_LOG = "SELECT name FROM sys.v$archived_log";
    public String SQL_QUERY_LOGFILE = "SELECT member FROM sys.v$logfile order BY member asc";
    public String SQL_QUERY_LOG_HISTORY = "SELECT * FROM sys.v$log_history";
    public String SQL_QUERY_LOG = "SELECT * FROM sys.v$log";
    public String SQL_QUERY_RESULT_SIZE = "SELECT count(sql_redo) FROM sys.v$logmnr_contents";
}
