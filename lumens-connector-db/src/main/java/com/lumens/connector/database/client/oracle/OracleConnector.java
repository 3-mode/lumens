/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.Operation;
import com.lumens.connector.database.Client;
import com.lumens.connector.database.client.DBConnector;
import com.lumens.model.Value;
import java.util.Map;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class OracleConnector extends DBConnector {
    protected String sessionAlter;

    @Override
    public Operation getOperation() {
        return new OracleOperation(dbClient);
    }

    @Override
    public void setPropertyList(Map<String, Value> propertyList) {
        if (propertyList.containsKey(SESSION_ALTER)) {
            sessionAlter = propertyList.get(SESSION_ALTER).getString();
        }
        super.setPropertyList(propertyList);
    }

    @Override
    protected Client createDBClient() {
        return new OracleClient(ojdbcURL, connURL, user, password, sessionAlter, pageSize);
    }

}
