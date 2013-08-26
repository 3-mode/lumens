/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

/**
 *
 * @author shaofeng wang
 */
public interface Cacheable {
    public void setCacheSize(int size);

    public int getCacheSize();
}
