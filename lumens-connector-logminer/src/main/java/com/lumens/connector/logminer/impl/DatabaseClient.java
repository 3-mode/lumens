/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import com.lumens.connector.database.DBUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DatabaseClient implements Constants {

    protected Driver driverObj;
    public Connection conn = null;

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
    
    public void release(){
        DBUtils.releaseConnection(conn);
    }
}
