/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class RapSyncResult implements OperationResult {

    private final List<Element> result;

    public RapSyncResult(List<Element> result) {
        this.result = result;
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
        return false;
    }

    @Override
    public OperationResult executeNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
