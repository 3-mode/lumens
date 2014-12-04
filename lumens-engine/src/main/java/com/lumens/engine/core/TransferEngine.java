/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.core;

import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.run.ExecuteContext;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransferEngine {
    public void execute(TransformComponent start) {
        List<ExecuteContext> result = start.execute(new TransformExecuteContext("GetPerson", Collections.EMPTY_LIST));
    }
}
