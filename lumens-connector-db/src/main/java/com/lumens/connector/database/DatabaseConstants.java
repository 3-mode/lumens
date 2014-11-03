package com.lumens.connector.database;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public interface DatabaseConstants {
    // Database constant  
    // TODO: to change names prefix with CONST_
    public static final String OJDBC = "OJDBC";
    public static final String CONNECTION_URL = "ConnectionURL";
    public static final String USER = "User";
    public static final String PASSWORD = "Password";
    public static final String FULL_LOAD = "FullLoad";
    public static final String SESSION_ALTER = "SessionAlter";
    
    // Connector constant
    public static String CONST_CNTR_SQLSERVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String CONST_CNTR_SQLSERVER_TYPE = "type";
    public static String CONST_CNTR_SQLSERVER_DESCRIPTION = "description";
    public static String CONST_CNTR_SQLSERVER_DATA_LENGTH = "data_length";
    public static String CONST_CNTR_SQLSERVER_DATA_TYPE = "data_type";
    // SQL Server SQL to query tables information
    public static String CONST_CNTR_SQLSERVER_TABLENAMES = "select t.name from master..SysDatabases t;";
    public static String CONST_CNTR_SQLSERVER_TABLECOLUMNS = "select t.'%s' from test..SysObjects t where xtype='u'";
    // SQL Server data types
    public static String CONST_CNTR_SQLSERVER_CHAR = "CHAR";
    public static String CONST_CNTR_SQLSERVER_VARCHAR2 = "VARCHAR2";
    public static String CONST_CNTR_SQLSERVER_NVARCHAR2 = "NVARCHAR2";
    public static String CONST_CNTR_SQLSERVER_CLOB = "CLOB";
    public static String CONST_CNTR_SQLSERVER_NUMBER = "NUMBER";
    public static String CONST_CNTR_SQLSERVER_DATE = "DATE";
    public static String CONST_CNTR_SQLSERVER_NUMBERIC = "NUMBERIC";
    public static String CONST_CNTR_SQLSERVER_BLOB = "BLOB";
    // Key field words
    public static String CONST_CNTR_SQLSERVER_SELECT = "SELECT";
    public static String CONST_CNTR_SQLSERVER_INSERT = "INSERT";
    public static String CONST_CNTR_SQLSERVER_UPDATE = "UPDATE";
    // Constants node names
    public static String CONST_CNTR_SQLSERVER_FIELDS = "fields";
    public static String CONST_CNTR_SQLSERVER_CLAUSE = "clause";
    public static String CONST_CNTR_SQLSERVER_OPERATION = "operation";    
}
