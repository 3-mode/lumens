/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class XMLConnector implements Connector {

    @Override
    public void open() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() {
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
    public Operation getOperation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPropertyList(Map<String, Value> propertyList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
