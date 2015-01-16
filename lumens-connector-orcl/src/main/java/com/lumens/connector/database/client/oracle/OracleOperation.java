/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.database.Client;
import com.lumens.connector.database.client.DBOperation;
import com.lumens.connector.database.client.DBQuerySQLBuilder;
import com.lumens.model.Format;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class OracleOperation extends DBOperation {

    public OracleOperation(Client client) {
        super(client);
    }

    @Override
    protected DBQuerySQLBuilder getQuerySQLBuilder(Format output) {
        return new OracleQuerySQLBuilder(output);
    }

}
