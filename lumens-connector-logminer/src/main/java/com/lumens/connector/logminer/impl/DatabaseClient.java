/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.database.DBUtils;
import com.lumens.logsys.SysLogFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DatabaseClient implements Constants {

    private final Logger log = SysLogFactory.getLogger(DefaultLogMiner.class);

    protected Driver driverObj;
    public Connection conn = null;
    public String version = null;

    public DatabaseClient(String driver, String url, String username, String password) throws SQLException {
        try {
            driverObj = (Driver) DBUtils.getInstance(driver, ORACLE_CLASS);
            conn = DBUtils.getConnection(driverObj, url, username, password);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void execute(String sql) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(sql);
        callableStatement.execute();
    }

    public ResultSet executeGetResult(String sql) throws SQLException {
        return conn.createStatement().executeQuery(sql);
    }

    public String getVersion() {
        if (version != null) {
            return version;
        }
        try {
            ResultSet result = executeGetResult(SQL_GEG_VERSION);
            if (result.next()) {
                version = result.getString(1);
            }
        } catch (Exception ex) {
            log.error("Fail to get Oracle version. Error message: " + ex.getMessage());
        }

        return version;
    }

    public void release() {
        DBUtils.releaseConnection(conn);
    }
}
