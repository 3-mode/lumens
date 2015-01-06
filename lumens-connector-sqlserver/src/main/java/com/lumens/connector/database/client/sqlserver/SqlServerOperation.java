/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.ElementChunk;
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
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;

public class SqlServerOperation implements Operation, DatabaseConstants {
    private static final Logger log = LogManager.getLogger(SqlServerOperation.class);
    private final SqlServerClient client;
    private final ElementFromDbBuilder elementBuilder;

    public SqlServerOperation(SqlServerClient client) {
        this.client = client;
        this.elementBuilder = new ElementFromDbBuilder();
    }

    @Override
    public OperationResult execute(ElementChunk input, Format output) throws Exception {
        List<Element> dataList = input.getData();
        if (dataList != null && !dataList.isEmpty()) {
            List<Element> results = new ArrayList<>();
            for (Element data : dataList) {
                Element oper = data.getChild(CONST_CNTR_SQLSERVER_OPERATION);
                if (oper == null || oper.getValue() == null)
                    throw new Exception("'operation' is mandatory");
                String operation = oper.getValue().getString();
                if (CONST_CNTR_SQLSERVER_SELECT.equalsIgnoreCase(operation)) {
                    SqlServerQuerySQLBuilder sql = new SqlServerQuerySQLBuilder(output);
                    String SQL = sql.generateSelectSQL(data);
                    List<Element> result = client.executeQuery(SQL, elementBuilder, output);
                    if (result != null && !result.isEmpty())
                        results.addAll(result);
                } else if (CONST_CNTR_SQLSERVER_INSERT.equalsIgnoreCase(operation)) {
                    SqlServerWriteSQLBuilder sql = new SqlServerWriteSQLBuilder();
                    String SQL = sql.generateInsertSQL(data);
                    client.execute(SQL);
                } else if (CONST_CNTR_SQLSERVER_UPDATE.equalsIgnoreCase(operation)) {
                    SqlServerWriteSQLBuilder sql = new SqlServerWriteSQLBuilder();
                    String SQL = sql.generateUpdateSQL(data);
                    client.execute(SQL);
                }
            }
            return new SqlServerOperationResult(results);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
