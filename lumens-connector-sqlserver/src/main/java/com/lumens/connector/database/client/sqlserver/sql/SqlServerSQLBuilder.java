/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.database.client.sqlserver.sql;

import com.lumens.connector.database.SQLBuilder;
import com.lumens.connector.database.DatabaseConstants;
import com.lumens.model.Element;

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
