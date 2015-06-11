/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.OperationResult;
import com.lumens.connector.rapsync.api.LogMiner;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Xiaoxin
 */
public class RapSyncQueryResult implements OperationResult {

    private List<Element> result;
    private final ElementChunk input;
    private final LogMiner miner;

    public RapSyncQueryResult(LogMiner miner, ElementChunk input) {
        this.input = input;
        this.miner = miner;
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
        return (result != null && result.size() == 1000)
                || (input.getStart() < input.getData().size());
    }

    @Override
    public OperationResult executeNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
