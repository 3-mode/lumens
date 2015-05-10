/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface LogMinerConstants {

    public String DATABASE_DRIVER = "databaseDriver";
    public String DATABASE_SOURCE_URL = "databaseSourceUrl";
    public String DATABASE_SOURCE_USERNAME = "databaseSourceUserName";
    public String DATABASE_SOURCE_PASSWORD = "databaseSourcePassword";

    public String SQL_QUERY_COLUMNS = "select t.COLUMN_NAME, t.DATA_TYPE, t.DATA_LENGTH from user_tab_columns t where t.TABLE_NAME = 'V_$LOGMNR_CONTENTS'";

    public String BUILD_TYPE_ONLINE = "buildTypeOnline";
    public String BUILD_TYPE_OFFLINE = "buildTypeOffline";
    public String DICT_TYPE_ONLINE = "dictTypeOnline";
    public String DICT_TYPE_STORE_IN_REDO_LOG = "dictTypesToreInRedoLog";
    public String DICT_TYPE_STORE_IN_FILE = "dictTypeStoreInFile";

    public String COMMITED_DATA_ONLY = "committedDataOnly";
    public String NO_ROWID = "noRowId";
    public String START_SCN = "startScn";
    public String END_SCN = "endScn";

    // Oracle constants from OracleConstants.java
    // TODO: move OracleConstants.java to DB connector to eliminate multi dependancy on oracle connector as well as db connector
    
    public String ORACLE_CLASS = "oracle.jdbc.OracleDriver";   
    public String CHAR = "CHAR";
    public String VARCHAR2 = "VARCHAR2";
    public String NVARCHAR2 = "NVARCHAR2";
    public String CLOB = "CLOB";
    public String NUMBER = "NUMBER";
    public String DATE = "DATE";
    public String NUMBERIC = "NUMBERIC";
    public String BLOB = "BLOB";

}
