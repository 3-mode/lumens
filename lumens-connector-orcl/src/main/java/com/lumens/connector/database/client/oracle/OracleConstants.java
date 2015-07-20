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
    // Character Datatypes
    public String CHAR = "CHAR";  // 1 and 2000 bytes 
    public String NCHAR = "NCHAR";
    public String VARCHAR = "VARCHAR";
    public String VARCHAR2 = "VARCHAR2";  // 1 and 4000 bytes
    public String NVARCHAR2 = "NVARCHAR2";

    // LOB Datatypes
    public String CLOB = "CLOB";  // Up to 8 terabytes
    public String NCLOB = "NCLOB";
    public String BLOB = "BLOB";  // up to 128 terabytes depends on block size
    public String LONG = "LONG"; // Supported for backward compatibility, 2 terabytes, replaced by LOB type
    public String BFILE = "BFILE"; // Size limitation is depends on OS

    // Numeric Datatypes
    public String NUMBER = "NUMBER";  // +/- 1 x 10-130 to 9.99...9 x 10125 with up to 38 significant digits
    public String TIMESTAMP = "TIMESTAMP";

    // DATE Datatype
    public String DATE = "DATE";  // Eliminated Centuries and the Year 2000 issue\
    public String DATATIME = "DATATIME"; // Daylight Savings Support, timezone support
    public String NUMBERIC = "NUMBERIC";
    public String TIMESTAMP_WITH_TIME_ZONE = "TIMESTAMP WITH TIME ZONE"; // TODO: support TIMESTAMP(x) WITH TIME ZONE
    public String TIMESTAMP_WITH_LOCAL_TIME_ZONE = "TIMESTAMP WITH LOCAL TIME ZONE"; // TODO: support TIMESTAMP(x) WITH LOCAL TIME ZONE

    // Oracle SQL to query tables information
    public String TABLENAMES = "select t.table_name,t.comments, t.TABLE_TYPE from user_tab_comments t";
    public String TABLECOLUMNS = "select t.COLUMN_NAME, t.DATA_TYPE, t.DATA_LENGTH from user_tab_columns t where t.TABLE_NAME = '%s'";
}
