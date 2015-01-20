/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextOperationResult implements OperationResult {
    private final List<Element> result;
    private boolean hasResultData;

    public TextOperationResult(List<Element> result) {
        this.hasResultData = result != null && !result.isEmpty();
        this.result = result;
    }

    @Override
    public List<Element> getData() {
        this.hasResultData = false;
        return result;
    }

    @Override
    public boolean hasData() {
        return hasResultData;
    }

    @Override
    public boolean hasNext() {
        // TODO need to handle big file data
        return false;
    }

    @Override
    public OperationResult executeNext() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
