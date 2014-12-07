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
    private boolean hasResultData;

    public SqlServerOperationResult(List<Element> result) {
        this.result = result;
        this.hasResultData = !result.isEmpty();
    }

    @Override
    public List<Element> getResult() {
        hasResultData = false;
        return result;
    }

    @Override
    public boolean hasResult() {
        return hasResultData;
    }
}
