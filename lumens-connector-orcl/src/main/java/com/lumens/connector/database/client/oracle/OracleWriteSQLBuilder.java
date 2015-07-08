/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.database.client.DBWriteSQLBuilder;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class OracleWriteSQLBuilder extends DBWriteSQLBuilder {

    @Override
    public String escapeString(String value) {
        if (value != null)
            return value.replace("'", "''");
        return value;
    }
}
