/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.fastsync;

import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class FastSyncResult implements OperationResult {

    private final List<Element> result;
    private boolean hasResultData;

    public FastSyncResult(List<Element> result) {
        this.hasResultData = result != null && !result.isEmpty();
        this.result = result;
    }

    @Override
    public boolean hasData() {
        return hasResultData;
    }

    @Override
    public List<Element> getData() {
        return result;
    }

    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OperationResult executeNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
