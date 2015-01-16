/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.sqlserver;

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
}
