/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.OperationResult;
import com.lumens.connector.rapsync.api.LogMiner;
import com.lumens.connector.database.client.DBElementBuilder;
import com.lumens.model.Element;
import java.util.List;
import java.sql.ResultSet;

/**
 *
 * @author Xiaoxin
 */
public class RapSyncQueryResult implements OperationResult {

    private List<Element> result;
    private final ElementChunk input;
    private final LogMiner miner;
    private final RapSyncQuerySQLBuilder builder;
    private final RapSyncOperation operation;
    private int pageStart;

    public RapSyncQueryResult(RapSyncOperation operation, LogMiner miner, RapSyncQuerySQLBuilder builder, ElementChunk input) throws Exception {
        this.input = input;
        this.miner = miner;
        this.operation = operation;
        this.pageStart = input.getStart();
        this.builder = builder;

        String sql = String.format(builder.generateSelectSQL(input.getData().get(input.getStart())), builder.getPageSize(), 1);
        ResultSet resultSet = miner.query(sql);
        this.result = new DBElementBuilder().buildElement(builder.getFormat(), resultSet);
    }

    @Override
    public boolean hasData() {
        return result != null && !result.isEmpty();
    }

    @Override
    public List<Element> getData() {
        return result;
    }

    @Override
    public boolean hasNext() {
        return (result != null && result.size() == 1000)
                || (input.getStart() < input.getData().size());
    }

    @Override
    public OperationResult executeNext() {
        if (hasNext() && result.size() < builder.getPageSize()) {
            try {
                this.input.setStart(input.getStart() + 1);
                return operation.execute(input, builder.getFormat());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (hasNext() && result.size() == builder.getPageSize()) {
            String sql = String.format(builder.generateSelectSQL(input.getData().get(input.getStart())), builder.getPageSize() + pageStart, pageStart);
            ResultSet resultSet = miner.query(sql);
            try {
                this.result = new DBElementBuilder().buildElement(builder.getFormat(), resultSet);
                pageStart += builder.getPageSize();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else {
            result = null;
        }
        return this;
    }
}
