/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.connector.database.client.oracle.OracleConnector;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class MockOracleConnector extends OracleConnector {

    public MockOracleConnector(String ojdbcURL, String connURL, String user, String password, String sessionAlter, int pageSize) {
        this.ojdbcURL = ojdbcURL;
        this.connURL = connURL;
        this.user = user;
        this.password = password;
        this.sessionAlter = sessionAlter;
        this.pageSize = pageSize;
    }
}
