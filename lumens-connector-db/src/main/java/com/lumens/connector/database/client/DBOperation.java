/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.ElementChunk;
import org.apache.logging.log4j.Logger;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.connector.database.Client;
import com.lumens.connector.database.client.oracle.OracleConstants;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.List;
import org.apache.logging.log4j.LogManager;

public abstract class DBOperation implements Operation, OracleConstants {

    private static final Logger log = LogManager.getLogger(DBOperation.class);
    private final Client client;

    public DBOperation(Client client) {
        this.client = client;
    }

    @Override
    public OperationResult execute(ElementChunk input, Format output) throws Exception {
        List<Element> dataList = input.getData();
        if (dataList != null && !dataList.isEmpty()) {
            for (Element elem : dataList) {
                Element action = elem.getChild(SQLPARAMS).getChild(ACTION);
                String operation = (action == null || action.getValue() == null) ? null : action.getValue().getString();
                if (operation == null || SELECT.equalsIgnoreCase(operation)) {
                    DBQuerySQLBuilder sql = getQuerySQLBuilder(output);
                    String SQL = sql.generateSelectSQL(elem);
                    List<Element> result = client.executeQuery(DBQuerySQLBuilder.generatePageSQL(SQL, 1, client.getPageSize()), output);
                    if (result != null && !result.isEmpty())
                        return new DBQueryResult(client, SQL, output, result);
                } else if (INSERT_ONLY.equalsIgnoreCase(operation)) {
                    DBWriteSQLBuilder sql = getWriteSQLBuilder();
                    String SQL = sql.generateInsertSQL(elem);
                    client.execute(SQL);
                } else if (UPDATE_ONLY.equalsIgnoreCase(operation)) {
                    DBWriteSQLBuilder sql = getWriteSQLBuilder();
                    String SQL = sql.generateUpdateSQL(elem);
                    client.execute(SQL);
                } else if (UPDATE_OR_INSERT.equalsIgnoreCase(operation)) {
                    throw new RuntimeException("Not supported now");
                }
            }
            // Except query, for update, delete no result for output
            return null;
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected DBWriteSQLBuilder getWriteSQLBuilder() {
        return new DBWriteSQLBuilder();
    }

    protected abstract DBQuerySQLBuilder getQuerySQLBuilder(Format output);

}
