/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.impl;

import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class CachedRedoLog extends RedoLog implements CachedRedoLogQuery {
    
    public CachedRedoLog(DatabaseClient dbClient) {
        super(dbClient);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public List<String> getNext() {
        return null;
    }
}
