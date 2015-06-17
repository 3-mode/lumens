/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DBWriteResult implements OperationResult {
    private final List<Element> result;

    public DBWriteResult(List<Element> result) {
        this.result = result;
    }

    @Override
    public boolean hasData() {
        return true;
    }

    @Override
    public List<Element> getData() {
        return result;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public OperationResult executeNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
