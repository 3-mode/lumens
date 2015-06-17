/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.impl;

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
    public String SQL_ENABLE_SUPPLEMENTAL_LOG = "ALTER DATABASE ADD SUPPLEMENTAL LOG DATA";
    public String SQL_START_LOGMINER = "BEGIN sys.dbms_logmnr.start_logmnr(%s);END;";
    public String SQL_QUERY_CURRENT_SCN = "SELECT current_scn FROM v$database";
    public String SQL_QUERY_MIN_SCN = "SELECT scn FROM SYS.SMON_SCN_TIME WHERE TIME_MP=(SELECT MIN(TIME_MP) FROM SYS.SMON_SCN_TIME)";
    public String SQL_QUERY_TIMESTAMP_TO_SCN = "SELECT timestamp_to_scn(%s) from dual";
    public String SQL_QUERY_VALID_SCN = "SELECT scn from sys.smon_scn_time where scn='%s'";
    public String SQL_END_LOGMINER = "BEGIN sys.dbms_logmnr.end_logmnr();END;";
    public String SQL_QUERY_DIRECTORY_PATH = "SELECT directory_path FROM sys.dba_directories WHERE DIRECTORY_NAME='UTL_FILE_DIR'";
    public String SQL_QUERY_LOG_INFO = "SELECT db_name, thread_sqn, filename FROM sys.v$logmnr_logs";
    public String SQL_QUERY_TABLE_DDL = "SELECT DBMS_METADATA.GET_DDL(UPPER('%s'),UPPER('%s'),UPPER('%s')) FROM DUAL";
    public String SQL_DROP_TABLE_DDL = "DROP TABLE %s.%s";
    public String SQL_EMPTY_TABLE_DDL = "TRUNCATE TABLE %s.%s";
    public String SQL_CREATE_TABLE_DDL = "CREATE TABLE %s.%s";
    public String SQL_QUERY_TABLE_EXIST = "SELECT * FROM sys.all_tab_comments t WHERE t.owner=UPPER('%s') AND t.table_name=UPPER('%s')";

    //public String SQL_QUERY_RESULT = "SELECT scn,operation,timestamp,status,sql_redo FROM v$logmnr_contents WHERE seg_owner='" + Constants.DATABASE_SOURCE_CLIENT_USERNAME + "' AND seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
    public String SQL_QUERY_RESULT = "SELECT %s FROM sys.v$logmnr_contents";
    public String SQL_QUERY_ARCHIVED_LOG = "SELECT name, status FROM sys.v$archived_log WHERE DELETED='NO' ORDER BY sequence# asc";
    public String SQL_QUERY_LOGFILE = "SELECT member FROM sys.v$logfile order BY member asc";
    public String SQL_QUERY_ARCHIVED_LOG_SIZE = "SELECT SUM(BLOCKS*BLOCK_SIZE)/1024/1024 ARCHIVED_LOG_SIZE FROM V$ARCHIVED_LOG WHERE DELETED='NO' ORDER BY sequence# asc";
    public String SQL_QUERY_ARCHIVED_LOG_COUNT = "SELECT COUNT(*) FROM V$ARCHIVED_LOG WHERE DELETED='NO'";
    public String SQL_QUERY_ONLINE_LOG_COUNT = "SELECT COUNT(*) FROM V$LOGFILE";
    public String SQL_QUERY_LOG_HISTORY = "SELECT * FROM sys.v$log_history";
    public String SQL_QUERY_LOG = "SELECT * FROM sys.v$log";
    public String SQL_QUERY_RESULT_SIZE = "SELECT count(sql_redo) FROM sys.v$logmnr_contents";
}
