/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.impl;

import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface CachedRedoLogQuery {
    boolean hasNext();
    List<String> getNext();
}
