/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.database.DatabaseConstants;
import com.lumens.connector.database.ElementFromDbBuilder;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
import com.lumens.connector.database.client.sqlserver.sql.SqlServerWriteSQLBuilder;
import com.lumens.connector.database.client.sqlserver.sql.SqlServerQuerySQLBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.List;
import org.apache.logging.log4j.LogManager;
public class SqlServerOperation implements Operation, DatabaseConstants{
    private static Logger log = LogManager.getLogger(SqlServerOperation.class);
    private SqlServerClient client;
    private ElementFromDbBuilder elementBuilder;

    public SqlServerOperation(SqlServerClient client) {
        this.client = client;
        this.elementBuilder = new ElementFromDbBuilder();
    }

    @Override
    public OperationResult execute(Element input, Format output) throws Exception {
        if (input != null) {
            Element oper = input.getChild(CONST_CNTR_SQLSERVER_OPERATION);
            if (oper == null || oper.getValue() == null)
                throw new Exception("'operation' is mandatory");
            String operation = oper.getValue().getString();
            if (CONST_CNTR_SQLSERVER_SELECT.equalsIgnoreCase(operation)) {
                SqlServerQuerySQLBuilder sql = new SqlServerQuerySQLBuilder(output);
                String SQL = sql.generateSelectSQL(input);
                List<Element> result = client.executeQuery(SQL, elementBuilder, output);
                return new SqlServerOperationResult(result);
            } else if (CONST_CNTR_SQLSERVER_INSERT.equalsIgnoreCase(operation)) {
                SqlServerWriteSQLBuilder sql = new SqlServerWriteSQLBuilder();
                String SQL = sql.generateInsertSQL(input);
                client.execute(SQL);
                return null;
            } else if (CONST_CNTR_SQLSERVER_UPDATE.equalsIgnoreCase(operation)) {
                SqlServerWriteSQLBuilder sql = new SqlServerWriteSQLBuilder();
                String SQL = sql.generateUpdateSQL(input);
                client.execute(SQL);
                return null;
            }
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void begin() {
    }

    @Override
    public void end() {
    }

    @Override
    public void commit() {
    }  
}
