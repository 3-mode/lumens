/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle.sql;

import com.lumens.connector.database.SQLBuilder;
import com.lumens.connector.database.client.oracle.OracleConstants;
import com.lumens.model.Element;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class OracleSQLBuilder implements SQLBuilder, OracleConstants {

    @Override
    public String generateInsertSQL(Element input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateUpdateSQL(Element input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateSelectSQL(Element input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateDeleteSQL(Element input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
