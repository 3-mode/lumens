/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class MockConnector implements Connector {

    @Override
    public void open() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Operation getOperation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPropertyList(Map<String, Value> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
