/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.database.SQLBuilder;
import com.lumens.model.Element;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class DBSQLBuilder implements SQLBuilder {

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
