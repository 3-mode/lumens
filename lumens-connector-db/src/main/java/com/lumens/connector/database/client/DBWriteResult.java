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
public class DBWriteResult implements OperationResult, SupportAccessory {
    private final List<Element> result;
    private final ElementChunk input;

    public DBWriteResult(ElementChunk input, List<Element> outList) {
        this.input = input;
        this.result = outList;
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

    @Override
    public ElementChunk getInput() {
        return input;
    }

    @Override
    public boolean isOneToOneForInOut() {
        return true;
    }
}
