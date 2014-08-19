/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

/**
 *
 * @author shaofeng wang
 */
public interface OracleConstants {

    public String ORACLE_CLASS = "oracle.jdbc.OracleDriver";
    public String TYPE = "type";
    public String DESCRIPTION = "description";
    public String DATA_LENGTH = "data_length";
    public String DATA_TYPE = "data_type";
    // Oracle SQL to query tables information
    public String TABLENAMES = "select t.table_name,t.comments, t.TABLE_TYPE from user_tab_comments t";
    public String TABLECOLUMNS = "select t.COLUMN_NAME, t.DATA_TYPE, t.DATA_LENGTH from user_tab_columns t where t.TABLE_NAME = '%s'";
    // Oracle data types
    public String CHAR = "CHAR";
    public String VARCHAR2 = "VARCHAR2";
    public String NVARCHAR2 = "NVARCHAR2";
    public String CLOB = "CLOB";
    public String NUMBER = "NUMBER";
    public String DATE = "DATE";
    public String NUMBERIC = "NUMBERIC";
    public String BLOB = "BLOB";
    // Key field words
    public String SELECT = "SELECT";
    public String INSERT = "INSERT";
    public String UPDATE = "UPDATE";
    // Constants node names
    public String FIELDS = "fields";
    public String CLAUSE = "clause";
    public String OPERATION = "operation";
}
