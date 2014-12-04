/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SqlServerOperationResult implements OperationResult {
    private final List<Element> result;
    private boolean isEof;

    public SqlServerOperationResult(List<Element> result) {
        this.result = result;
    }

    @Override
    public List<Element> getResult() {
        isEof = true;
        return result;
    }

    @Override
    public boolean isLastChunk() {
        return isEof;
    }
}
