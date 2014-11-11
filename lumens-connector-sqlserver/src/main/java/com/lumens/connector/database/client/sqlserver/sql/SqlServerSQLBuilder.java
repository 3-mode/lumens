/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.database.client.sqlserver.sql;

import com.lumens.connector.database.SQLBuilder;
import com.lumens.connector.database.DatabaseConstants;
import com.lumens.model.Element;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SqlServerSQLBuilder implements SQLBuilder, DatabaseConstants{
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
