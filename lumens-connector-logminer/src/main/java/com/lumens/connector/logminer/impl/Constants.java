/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.database.DBConstants;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface Constants extends DBConstants {

    public String ORACLE_CLASS = "oracle.jdbc.OracleDriver";
    // Oracle data types
    public String CHAR = "CHAR";
    public String VARCHAR2 = "VARCHAR2";
    public String NVARCHAR2 = "NVARCHAR2";
    public String CLOB = "CLOB";
    public String NUMBER = "NUMBER";
    public String DATE = "DATE";
    public String NUMBERIC = "NUMBERIC";
    public String BLOB = "BLOB";
    // Oracle SQL to query tables information
    public String TABLENAMES = "select t.table_name,t.comments, t.TABLE_TYPE from user_tab_comments t";
    public String TABLECOLUMNS = "select t.COLUMN_NAME, t.DATA_TYPE, t.DATA_LENGTH from user_tab_columns t where t.TABLE_NAME = '%s'";


    /**
     * directory
     */
    public String DICTIONARY_FILE = "dictionary.ora";
    public String SQL_QUERY_DIRECTORY_PATH = "select DIRECTORY_PATH from dba_directories where DIRECTORY_NAME='UTL_FILE_DIR'";
    public String SQL_QUERY_LOG_INFO = "SELECT db_name, thread_sqn, filename FROM v$logmnr_logs";
    //public String SQL_BUILD_RESULT = "SELECT scn,operation,timestamp,status,sql_redo FROM v$logmnr_contents WHERE seg_owner='" + Constants.DATABASE_SOURCE_CLIENT_USERNAME + "' AND seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
    public String SQL_BUILD_RESULT = "SELECT scn,operation,timestamp,status,sql_redo FROM v$logmnr_contents WHERE seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
    public String SQL_QUERY_ARCHIVED_LOG = "SELECT name FROM v$archived_log";
    public String SQL_QUERY_LOGFILE = "SELECT member FROM v$logfile";
    public String SQL_QUERY_LOG_HISTORY = "SELECT * FROM v$log_history";
    public String SQL_QUERY_LOG = "SELECT * FROM v$log";
    public String SQL_QUERY_RESULT_SIZE = "SELECT count(sql_redo) FROM v$logmnr_contents";
}
