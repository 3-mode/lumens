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
    public String CHARACTER = "CHARACTER";
    public String STRING = "STRING";

    // XML
    public String XMLTYPE = "XMLType";

    // LOB Datatypes
    public String CLOB = "CLOB";  // Up to 8 terabytes
    public String NCLOB = "NCLOB";
    public String BLOB = "BLOB";  // up to 128 terabytes depends on block size
    public String LONG = "LONG"; // Supported for backward compatibility, 2 terabytes, replaced by LOB type
    public String BFILE = "BFILE"; // Size limitation is depends on OS
    public String RAW = "RAW";
    public String LONG_RAW = "LONG RAW";

    // Numeric Datatypes
    public String NUMBER = "NUMBER";  // +/- 1 x 10-130 to 9.99...9 x 10125 with up to 38 significant digits
    public String NUMBERIC = "NUMBERIC";
    
    // backwards compatible numeric Datatypes, some are not supported in new version of Oracle
    public String BINARY_FLOAT = "BINARY_FLOAT"; // 32-bit, 5 bytes
    public String BINARY_DOUBLE = "BINARY_DOUBLE"; // 64-bit, 9 bytes
    public String BINARY_INTEGER = "BINARY_INTEGER";  // 32bit
    public String NATURAL = "NATURAL";  // 32bit
    public String NATURALN = "NATURALN";  // // 32bit
    public String PLS_INTEGER = "PLS_INTEGER";  // 32 bit
    public String POSITIVE = "POSITIVE";  // 32bit
    public String POSITIVEN = "POSITIVEN";  // 32bit
    public String SIGNTYPE = "SIGNTYPE";  // -1, 0, +1
    public String INT = "INT";  // 38bit
    public String INTEGER = "INTEGER";  // 38 bit
    public String DEC = "DEC";  // 38bit
    public String DECIMAL = "DECIMAL"; // 38bit
    public String SMALLINT = "SMALLINT";  //38bit
    public String DOUBLE_PRECISION = "DOUBLE PRECISION";  // 126 bit
    public String FLOAT = "FLOAT";  // 126bit
    public String REAL = "REAL"; // 63 bit
    

    // DATE Datatype
    public String DATE = "DATE";  // Eliminated Centuries and the Year 2000 issue\
    public String DATATIME = "DATATIME"; // Daylight Savings Support, timezone support    
    public String TIMESTAMP = "TIMESTAMP";
    public String TIMESTAMP_WITH_TIME_ZONE = "TIMESTAMP WITH TIME ZONE"; // TODO: support TIMESTAMP(x) WITH TIME ZONE
    public String TIMESTAMP_WITH_LOCAL_TIME_ZONE = "TIMESTAMP WITH LOCAL TIME ZONE"; // TODO: support TIMESTAMP(x) WITH LOCAL TIME ZONE
    public String INTERVAL = "INTERVAL";  // TODO: support oracle interval
    public String INTERVAL_DAY_TO_SECOND = "INTERVAL DAY TO SECOND";
    public String INTERVAL_YEAR_TO_SECOND = "INTERVAL YEAR TO SECOND";

    // ROWID supported from 8.1 or higher 
    // UROWID Format: OOOOOOFFFBBBBBBRRR 
    // OOOOOO: The data object number that identifies the database segment, Schema objects in the same segment/a cluster of tables
    // FFF: The tablespace-relative datafile number of the datafile that contains the row
    // BBBBBB: The data block that contains the row. Block numbers are relative to their datafile, not tablespace. 
    // RRR: The row in the block.
    public String UROWID = "UROWID";   // Oracle 8i, Supports both logical and physical rowids, as well as rowids of foreign tables
    public String UNIVERSAL_ROWID = "universal rowid";
    // Format: AAAAAAAA.BBBB.CCCC => block,row,file
    // AAAAAAAA: data block that contains the row. Block numbers are relative to their datafile, not tablespace.
    // BBBB: The row in the block that contains the row. Row numbers of a given block always start with 0.
    // CCCC: The datafile that contains the row
    public String ROWID = "ROWID";  // Oracle <= 7i, physical address of each row. Use a base 64 encoding of the physical address

    // Oracle SQL to query tables information
    public String TABLENAMES = "select t.table_name,t.comments, t.TABLE_TYPE from user_tab_comments t";
    public String TABLECOLUMNS = "select t.COLUMN_NAME, t.DATA_TYPE, t.DATA_LENGTH from user_tab_columns t where t.TABLE_NAME = '%s'";
}
