/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.database.DBConstants;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface RapSyncConstants extends DBConstants {

    public String FORMAT_NAME = "SYNC";
    public String DATABASE_DRIVER = "databaseDriver";
    public String DATABASE_CONNECTION_URL = "databaseConnectionUrl";
    public String DATABASE_CONNECTION_USERNAME = "databaseSourceUserName";
    public String DATABASE_CONNECTION_PASSWORD = "databaseSourcePassword";

    public String QUERY = "QUERY";
    public String SYNC = "SYNC";

    public String SQL_QUERY_COLUMNS = "select t.COLUMN_NAME, t.DATA_TYPE, t.DATA_LENGTH from all_tab_columns t where t.TABLE_NAME = 'V_$LOGMNR_CONTENTS'";

    public String BUILD_TYPE_ONLINE = "buildTypeOnline";
    public String BUILD_TYPE_OFFLINE = "buildTypeOffline";
    public String DICT_TYPE_ONLINE = "dictTypeOnline";
    public String DICT_TYPE_STORE_IN_REDO_LOG = "dictTypesToreInRedoLog";
    public String DICT_TYPE_STORE_IN_FILE = "dictTypeStoreInFile";

    public String COMMITED_DATA_ONLY = "committedDataOnly";
    public String NO_ROWID = "noRowId";
    public String START_SCN = "startScn";
    public String END_SCN = "endScn";

    public String COLUMN_REDO = "SQL_REDO";
    public String COLUMN_SCN = "SCN";
    public String COLUMN_OPERATION = "OPERATION";
    public String COLUMN_SEG_OWNER = "SEG_OWNER";
    public String COLUMN_TABLE_NAME = "TABLE_NAME";
    public int COLUMN_SCN_LENGTH = 22;
    public int COLUMN_REDO_LENGTH = 4000;

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
