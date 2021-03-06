/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface SupportCarrier {

    public boolean isOneToOneForInOut();

    public ElementChunk getInput();
}
