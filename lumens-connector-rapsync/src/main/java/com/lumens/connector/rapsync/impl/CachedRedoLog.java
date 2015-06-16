/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.impl;

/**
 *
 * @author Xiaoxin
 */
public class CachedRedoLog extends RedoLog{

    public CachedRedoLog(DatabaseClient dbClient) {
        super(dbClient);
    }
}
