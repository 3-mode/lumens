/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.sqlserver;

import com.lumens.connector.Operation;
import com.lumens.connector.database.Client;
import com.lumens.connector.database.client.DBConnector;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class SQLServerConnector extends DBConnector {

    @Override
    protected Client createDBClient() {
        return new SQLServerClient(ojdbcURL, connURL, user, password, pageSize);
    }

    @Override
    public Operation getOperation() {
        return new SQLServerOperation(dbClient);
    }
}
