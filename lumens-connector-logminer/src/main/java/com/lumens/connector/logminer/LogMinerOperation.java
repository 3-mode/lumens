/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.connector.logminer.api.LogMiner;
import com.lumens.model.Format;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerOperation implements Operation {

    private LogMiner miner = null;

    public LogMinerOperation(LogMiner miner) {
        this.miner = miner;
    }

    @Override
    public OperationResult execute(ElementChunk input, Format output) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
