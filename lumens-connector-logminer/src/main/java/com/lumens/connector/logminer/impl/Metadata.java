/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.database.DBUtils;
import com.lumens.logsys.SysLogFactory;
import java.sql.ResultSet;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class Metadata implements Constants {

    private final Logger log = SysLogFactory.getLogger(DefaultLogMiner.class);
    private DatabaseClient db;

    public Metadata(DatabaseClient db) {
        this.db = db;
    }

    public String getTableDDL(String schema, String table) {
        String ddl = null;
        String queryDDL = String.format(SQL_QUERY_TABLE_DDL, "TABLE", table, schema);
        ResultSet result = null;
        try {
            result = db.executeGetResult(queryDDL);
            if (result.next()) {
                ddl = result.getString(1);
            }
        } catch (Exception ex) {
            log.error("Fail to get table DDL. Error message:");
            log.error(ex.getMessage());
            log.info("Failure sql:" + queryDDL);
        } finally {
            DBUtils.releaseResultSet(result);
            db.releaseStatement();
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
                log.error("Fail to create table. Error message:");
                log.error(ex.getMessage());
            }
        }

        return false;
    }

    public boolean dropTable(String schema, String table) {
        try {
            db.execute(String.format(SQL_DROP_TABLE_DDL, schema, table));
            return true;
        } catch (Exception ex) {
            log.error("Fail to drop table. Error message:");
            log.error(ex.getMessage());
        }
        
        return false;
    }

    public boolean checkRecordExist(String updateORdeleteFromSQL) {
        boolean isExist = false;
        String upper = updateORdeleteFromSQL.toUpperCase().trim();
        String select = null;
        if (upper.startsWith("UPDATE")) {
            select = upper.split("SET")[0].replaceAll("UPDATE", "SELECT * FROM ");
        } else if (upper.startsWith("DELETE FROM")) {
            select = upper.split("WHERE")[0].replaceAll("DELETE FROM", "SELECT * FROM ");
        } else {
            log.error("Fail to check table record. Not a update or delete from statement:" + upper);
            return false;
        }

        String where = upper.split("WHERE")[1].replaceAll(";", "");  // remove end ; as JAVA JDBC driver does not support
        String full = select + " WHERE " + where;
        ResultSet result = null;
        try {
            result = db.executeGetResult(full);
            isExist = result.next();
        } catch (Exception ex) {
            log.error("Fail to check table record. Error message:");
            log.error(ex.getMessage());
            log.info("Failure sql:" + full);
        } finally {
            DBUtils.releaseResultSet(result);
            db.releaseStatement();
        }

        return isExist;
    }

    public boolean checkTableExist(String schema, String table) {
        boolean isExist = false;
        String check = String.format(SQL_QUERY_TABLE_EXIST, schema, table);
        ResultSet result = null;
        try {
            result = db.executeGetResult(check);
            isExist = result.next();
        } catch (Exception ex) {
            log.error("Fail to check table exist. Error message:");
            log.error(ex.getMessage());
            log.info("Failure sql:" + check);
        } finally {
            DBUtils.releaseResultSet(result);
            db.releaseStatement();
        }

        return isExist;
    }
}
