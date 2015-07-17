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
    public String BIGINT = "bigint";
    public String BIT = "bit";
    public String DECIMAL = "decimal";
    public String INT = "int";  // INT4
    public String MONEY = "money";
    public String NUMERIC = "numeric";
    public String SMALLINT = "smallint"; // INT2
    public String SMALLMONEY = "smallmoney";
    public String TINYINT = "tinyint";  // INT1

    // Approximate numberics
    public String FLOAT = "float";
    public String REAL = "real";

    // Date and Time
    public String DATETIME = "datetime";
    public String SMALLDATETIME = "smalldatetime";
    public String DATE = "date";
    public String TIME = "time";
    public String DATATIMEOFFSET = "datatimeoffset";
    public String DATETIME2 = "datetime2";

    // Character Strings
    public String CHAR = "char";
    public String VARCHAR = "varchar";
    public String TEXT = "text";

    // Unicode character strings
    public String NCHAR = "nchar";
    public String NVARCHAR = "nvarchar";
    public String NTEXT = "ntext";

    // Binary stings
    public String BINARY = "binary";
    public String IMAGE = "image";
    public String VARBINARY = "varbinary";

    // others
    public String TIMESTAMP = "timestamp";   // It is not the same time type as SQL-92 standard
    public String ROWVERSION = "rowversion "; // To replace timestamp from SQL Server 2000
    public String UNIQUEIDENTIFIER = "uniqueidentifier";
    public String SQL_VARIANT = "sql_variant";
    public String XML = "xml";    
    public String HIERARCHYID = "hierarchyid";

    // user defined datatype
    public String CLR_UDT = "clr udt";
    
    // Applies To: SQL Server 2014, SQL Server 2016 Preview
    // There more types: cursor/table
    public String GEOGRAPH = "geography";
    public String CURSOR = "cursor";    
}
