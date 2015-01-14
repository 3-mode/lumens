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
    public static String SQLSERVER_DATABASENAMES = "select t.name from SysDatabases t;";
    public static String SQLSERVER_TABLENAMES = "select t.name, t.id, t.xtype from SysObjects t where xtype='u'";
    public static String SQLSERVER_TABLECOLUMNS = "select t.name, t.id, t.xtype, t.xusertype, t.length from syscolumns t where id=%d ";

    // table properties
    public static String SQLSERVER_XTYPE = "XTYPE";
    public static String SQLSERVER_ID = "ID";
}
