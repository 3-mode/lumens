/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import java.sql.ResultSet;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class Metadata implements Constants {

    private DatabaseClient db;

    public Metadata(DatabaseClient db) {
        this.db = db;
    }

    public String getTableDDL(String schema, String table) {
        String ddl = null;
        String queryDdl = String.format(SQL_QUERY_TABLE_DDL, "TABLE", table, schema);
        try {
            ResultSet result = db.executeGetResult(queryDdl);
            if (result.next()) {
                ddl = result.getString(1);
            }
        } catch (Exception ex) {

        }

        return ddl;
    }

    public boolean createTable(String schema, String table) {
        String ddl = getTableDDL(schema, table);
        if (ddl != null) {
            try {
                db.execute(ddl);
                return checkTableExist(schema, table);
            } catch (Exception ex) {
            }
        }

        return false;
    }

    public boolean checkRecordExist(String updateSQL) {
        String upper = updateSQL.toUpperCase();
        String select = upper.split("SET")[0].replaceAll("UPDATE", "SELECT * FROM ");
        String where = upper.split("WHERE")[1];
        try {
            ResultSet result = db.executeGetResult(select + " WHERE " + where);
            return result.next();
        } catch (Exception ex) {
        }
        return false;
    }

    public boolean checkTableExist(String schema, String table) {
        try {
            ResultSet result = db.executeGetResult(String.format(SQL_QUERY_TABLE_EXIST, schema, table));
            return result.next();
        } catch (Exception ex) {
            System.out.println("Exception");
        }

        return false;
    }
}
