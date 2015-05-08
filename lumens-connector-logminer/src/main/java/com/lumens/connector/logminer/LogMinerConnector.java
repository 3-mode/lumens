/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

import com.lumens.connector.logminer.api.LogMiner;
import com.lumens.connector.Operation;
import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerConnector implements Connector {

    boolean isOpen = false;
    
    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void open() {
        // start build directory       
        // check parameter availablity
        // check soruce db as well as minger db with sufficient priviledge 

        isOpen = true;
    }

    @Override
    public void close() {

    }

    @Override
    public Operation getOperation() {
        return null;
    }

    // get redo log fields from db
    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        Map<String, Format> formatList = new HashMap();
        

        return formatList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        return null;
    }

    @Override
    public void start() {
        // start log analysis
        // start processing redo log sql: query and process each sql
    }

    @Override
    public void stop() {
        // end log analysis
    }

    @Override
    public void setPropertyList(Map<String, Value> parameters) {
    }
}
