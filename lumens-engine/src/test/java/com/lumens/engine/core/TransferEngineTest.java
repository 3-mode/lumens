/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.core;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransferEngineTest {

    public TransferEngineTest() {
    }

    @Test
    public void runSimpleTransformTask() {
        TransferEngine te = new TransferEngine();
        te.execute();
        assertTrue(true);
    }
}
