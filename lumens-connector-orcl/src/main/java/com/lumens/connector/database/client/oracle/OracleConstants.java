/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.database.DBConstants;

/**
 *
 * @author shaofeng wang
 */
public interface OracleConstants extends DBConstants {

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
    // ALL_TAB_COMMENTS displays comments on the tables and views accessible to the current user.
    public String TABLENAMES = "select t.table_name,t.comments, t.TABLE_TYPE from user_tab_comments t";
    public String TABLECOLUMNS = "select t.COLUMN_NAME, t.DATA_TYPE, t.DATA_LENGTH from user_tab_columns t where t.TABLE_NAME = '%s'";
}
