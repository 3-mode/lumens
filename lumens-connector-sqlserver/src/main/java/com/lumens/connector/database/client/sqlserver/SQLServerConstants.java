/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.sqlserver;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface SQLServerConstants {

    public String SQLSERVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    // SQL Server SQL to query tables information
    public String SQLSERVER_DATABASENAMES = "select t.name from SysDatabases t;";
    public String SQLSERVER_TABLENAMES = "select t.name, t.id, t.xtype from SysObjects t where xtype='u'";
    public String SQLSERVER_TABLECOLUMNS = "select t.name, t.id, t.xtype, t.xusertype, t.length from syscolumns t where id=%d ";
    public String SQLSERVER_PRIMARYKEY = "SELECT column_name as primarykeycolumn FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS TC INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KU ON TC.CONSTRAINT_TYPE = 'PRIMARY KEY' AND TC.CONSTRAINT_NAME = KU.CONSTRAINT_NAME AND KU.TABLE_NAME = '%s' ORDER BY KU.TABLE_NAME, KU.ORDINAL_POSITION";
    // table properties
    public String SQLSERVER_XTYPE = "XTYPE";
    public String SQLSERVER_ID = "ID";
    public String SQLSERVER_PK = "PRIMARY_KEY";

    // SQL Server datatypes 
    // Exact numberics
    public String BIGINT = "bigint";  // -2^63 (-9,223,372,036,854,775,808) to 2^63-1 (9,223,372,036,854,775,807)
    public String BIT = "bit";
    public String DECIMAL = "decimal";  // - 10^38 +1 through 10^38 - 1
    public String INT = "int";  // INT4  -2^31 (-2,147,483,648) to 2^31-1 (2,147,483,647)
    public String INT1 = "int1";
    public String INT2 = "int2";
    public String INT4 = "int4";
    public String MONEY = "money";  // -922,337,203,685,477.5808 to 922,337,203,685,477.5807
    public String NUMERIC = "numeric";
    public String SMALLINT = "smallint"; // INT2  -2^15 (-32,768) to 2^15-1 (32,767)
    public String SMALLMONEY = "smallmoney";  // - 214,748.3648 to 214,748.3647
    public String TINYINT = "tinyint";  // INT1 0 to 255

    // Approximate numberics
    public String FLOAT = "float";  // - 1.79E+308 to -2.23E-308, 0 and 2.23E-308 to 1.79E+308
    public String REAL = "real";  // - 3.40E + 38 to -1.18E - 38, 0 and 1.18E - 38 to 3.40E + 38

    // Date and Time
    public String DATETIME = "datetime";  // Date from January 1, 1753, through December 31, 9999, time from 00:00:00 through 23:59:59.997
    public String SMALLDATETIME = "smalldatetime";  // Date from 1900-01-01 through 2079-06-06, time from 00:00:00 through 23:59:59
    public String DATE = "date";  // Date from 0001-01-01 through 9999-12-31 Default value 1900-01-01
    public String TIME = "time";  // Time in a day range from 00:00:00.0000000 through 23:59:59.9999999 Default value 00:00:00
    public String DATATIMEOFFSET = "datatimeoffset";  // Date from 0001-01-01 through 9999-12-31, time from 00:00:00 through 23:59:59.9999999 
    public String DATETIME2 = "datetime2"; // Date range from 0001-01-01 through 9999-12-31, Time range from 00:00:00 through 23:59:59.9999999

    // Character Strings
    public String CHAR = "char";
    public String VARCHAR = "varchar";
    public String TEXT = "text";  //  2^31-1 (2,147,483,647)

    // Unicode character strings
    public String NCHAR = "nchar";
    public String NVARCHAR = "nvarchar";
    public String NTEXT = "ntext";  // 2^30 - 1 (1,073,741,823) bytes

    // Binary stings
    public String BINARY = "binary";  // 1 - 8,000 bytes
    public String IMAGE = "image";  // Variable-length binary data from 0 through 2^31-1 (2,147,483,647) bytes.
    public String VARBINARY = "varbinary";  //  2^31-1 bytes

    // others
    public String TIMESTAMP = "timestamp";   // It is not the same time type as SQL-92 standard but a binary type
    public String ROWVERSION = "rowversion "; // To replace timestamp from SQL Server 2000
    public String UNIQUEIDENTIFIER = "uniqueidentifier";
    public String SQL_VARIANT = "sql_variant";  //  Mostly for int, binary, and char
    public String XML = "xml";    // From SQL Server 2005, these XML values are stored in an internal format as large binary objects (BLOB) <= 2GB
    public String HIERARCHYID = "hierarchyid";  // 892 bytes, TODO: support automatically represent a tree in data processing

    // user defined datatype
    public String CLR_UDT = "clr udt";  // Depends on user defined, not support now 

    // Applies to: SQL Server (SQL Server 2008 through current version), Azure SQL Database.
    public String GEOGRAPHY = "geography";  // TODO: support this GPS latitude and longitude coordinates
    public String GEOMETRY = "geometry";
}
