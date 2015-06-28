/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.OperationResult;
import com.lumens.connector.SupportAccessory;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DBQueryResult implements OperationResult, SupportAccessory {

    private final DBOperation operation;
    private final DBQuerySQLBuilder sqlBuilder;
    private final ElementChunk input;
    private List<Element> result;
    private final String SQL;
    private final int pageSize;
    private int pageStart;

    public DBQueryResult(DBOperation operation, DBQuerySQLBuilder sqlBuilder, ElementChunk input) {
        this.operation = operation;
        this.sqlBuilder = sqlBuilder;
        this.input = input;
        this.pageStart = operation.getClient().getPageSize() + 1;
        this.pageSize = operation.getClient().getPageSize();
        this.SQL = sqlBuilder.generateSelectSQL(input.getData().get(input.getStart()));
        this.result = operation.getClient().executeQuery(sqlBuilder.generatePageSQL(SQL, 1, pageSize), sqlBuilder.getFormat());
    }

    @Override
    public ElementChunk getInput() {
        return input;
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
               || ((input.getStart() + 1) < input.getData().size());
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

    @Override
    public boolean isOneToOneForInOut() {
        return false;
    }
}
