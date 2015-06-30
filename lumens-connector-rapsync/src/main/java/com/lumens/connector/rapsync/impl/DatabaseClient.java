/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.impl;

import com.lumens.connector.database.DBUtils;
import com.lumens.logsys.SysLogFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DatabaseClient implements Constants {

    private final Logger log = SysLogFactory.getLogger(DatabaseClient.class);

    protected Driver driverObj;
    private Connection conn = null;
    private String version = null;
    private Statement stat = null;
    private String login = null;
    private String user = null;
    private String schema = null;

    public DatabaseClient(String driver, String url, String username, String password) throws SQLException {
        try {
            driverObj = (Driver) DBUtils.getInstance(driver, ORACLE_CLASS);
            conn = DBUtils.getConnection(driverObj, url, username, password);
            user = username.split(" ")[0].trim();
            login = username.trim();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void execute(String sql) throws SQLException {
        try (CallableStatement callableStatement = conn.prepareCall(sql)) {
            callableStatement.execute();
        } catch (Exception ex) {
            DBUtils.rollback(conn);
            throw new RuntimeException(ex);
        }
    }

    public ResultSet executeGetResult(String sql) throws SQLException {
        if (stat == null) {
            stat = conn.createStatement();
        }
        return stat.executeQuery(sql);
    }

    public String getUser() {
        return user;
    }

    public String getLogin() {
        return login;
    }

    public String getSchema() {
        if (schema != null) {
            return schema;
        }
        try (ResultSet result = executeGetResult(SQL_GEG_SCHEMA)) {
            if (result.next()) {
                schema = result.getString(1);
            }
        } catch (Exception ex) {
            log.error("Fail to get Oracle schema. Error message: " + ex.getMessage());
        } finally {
            releaseStatement();
        }

        return schema;
    }

    public String getVersion() {
        if (version != null) {
            return version;
        }
        try (ResultSet result = executeGetResult(SQL_GEG_VERSION)) {
            if (result.next()) {
                version = result.getString(1);
            }
        } catch (Exception ex) {
            log.error("Fail to get Oracle version. Error message: " + ex.getMessage());
        } finally {
            releaseStatement();
        }

        return version;
    }

    public void releaseStatement() {
        DBUtils.releaseStatement(stat);
        stat = null;
    }

    public void release() {
        DBUtils.releaseConnection(conn);
        conn = null;
    }
}
