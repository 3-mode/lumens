/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Format;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public interface Operation {

    public OperationResult execute(ElementChunk input, Format output) throws Exception;

}
