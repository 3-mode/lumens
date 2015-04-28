/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface LogMinerConstants {

    /**
     * source db configure
     */
    public static String DATABASE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static String DATABASE_SOURCE_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
    public static String DATABASE_SOURCE_USERNAME = "logminer";
    public static String DATABASE_SOURCE_PASSWORD = "logminer";
    public static String DATABASE_SOURCE_CLIENT_USERNAME = "logminer";

    /**
     * destination db configure
     */
    public static String DATABASE_TARGET_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
    public static String DATABASE_TARGET_USERNAME = "logminer2";
    public static String DATABASE_TARGET_PASSWORD = "logminer2";

    /**
     * redo log file path
     */
    public static String DATABASE_LOG_FILE_PATH = "D:\\app\\oracle\\oradata\\orcl";

    /**
     * directory path
     */
    public static String DATABASE_DICTIONARY_PATH = "D:\\app\\oracle\\oradata\\dict";

    public String SQL_CREATE_DIECTIONARY = "BEGIN dbms_logmnr_d.build(dictionary_filename => 'dictionary.ora', dictionary_location =>'" + DATABASE_DICTIONARY_PATH + "'); END;";
    public String SQL_QUERY_LOG_INFO = "SELECT db_name, thread_sqn, filename FROM v$logmnr_logs";
    //public String SQL_BUILD_RESULT = "SELECT scn,operation,timestamp,status,sql_redo FROM v$logmnr_contents WHERE seg_owner='" + Constants.DATABASE_SOURCE_CLIENT_USERNAME + "' AND seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
    public String SQL_BUILD_RESULT = "SELECT scn,operation,timestamp,status,sql_redo FROM v$logmnr_contents WHERE seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
    public String SQL_QUERY_ARCHIVED_LOG = "SELECT name FROM v$archived_log";
    public String SQL_QUERY_LOGFILE = "SELECT member FROM v$logfile";
    public String SQL_QUERY_LOG_HISTORY = "SELECT * FROM v$log_history";
    public String SQL_QUERY_LOG = "SELECT * FROM v$log";
    public String SQL_QUERY_RESULT_SIZE = "SELECT count(sql_redo) FROM v$logmnr_contents";
}
