/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import com.lumens.connector.ElementChunk;
import com.lumens.connector.database.client.DBWriteResult;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Xiaoxin
 */
public class RapSyncWriteResult extends DBWriteResult {

    public RapSyncWriteResult(ElementChunk input, List<Element> outList) {
        super(input, outList);
    }

    @Override
    public boolean hasData() {
        return false;
    }
}
