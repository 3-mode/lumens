/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.OperationResult;
import com.lumens.connector.database.Client;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DBQueryResult implements OperationResult {

    private final DBOperation operation;
    private final int pageSize;
    private final DBQuerySQLBuilder sqlBuilder;
    private final ElementChunk input;
    private final Client client;
    private final String SQL;
    private List<Element> result;
    private int pageStart;

    public DBQueryResult(DBOperation operation, DBQuerySQLBuilder sqlBuilder, ElementChunk input) {
        this.operation = operation;
        this.client = operation.getClient();
        this.sqlBuilder = sqlBuilder;
        this.input = input;
        this.pageStart = operation.getClient().getPageSize() + 1;
        this.pageSize = operation.getClient().getPageSize();
        this.SQL = sqlBuilder.generateSelectSQL(input.getData().get(input.getStart()));
        this.result = client.executeQuery(sqlBuilder.generatePageSQL(SQL, 1, pageSize), sqlBuilder.getFormat());
    }

    @Override
    public List<Element> getData() {
        return result;
    }

    @Override
    public boolean hasData() {
        return result != null && !result.isEmpty();
    }

    @Override
    public boolean hasNext() {
        // Handle current input data if query finished then check if the input data is the last in the input data list
        return (result != null && result.size() == pageSize)
               || (input.getStart() < input.getData().size());
    }

    @Override
    public OperationResult executeNext() {
        if (hasNext() && result.size() < pageSize) {
            try {
                this.input.setStart(input.getStart() + 1);
                return operation.execute(input, sqlBuilder.getFormat());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (hasNext() && result.size() == pageSize) {
            result = operation.getClient().executeQuery(sqlBuilder.generatePageSQL(SQL, pageStart, pageSize), sqlBuilder.getFormat());
            pageStart += pageSize;
        } else {
            result = null;
        }
        return this;
    }
}
